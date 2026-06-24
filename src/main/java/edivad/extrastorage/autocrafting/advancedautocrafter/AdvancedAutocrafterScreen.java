package edivad.extrastorage.autocrafting.advancedautocrafter;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslationAsHeading;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.refinedmods.refinedstorage.common.api.autocrafting.PatternOutputRenderingScreen;
import com.refinedmods.refinedstorage.common.autocrafting.PatternSlot;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.tooltip.HelpClientTooltipComponent;
import com.refinedmods.refinedstorage.common.support.widget.History;
import com.refinedmods.refinedstorage.common.support.widget.SearchFieldWidget;
import com.refinedmods.refinedstorage.common.support.widget.TextMarquee;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AdvancedAutocrafterScreen extends AbstractBaseScreen<AdvancedAutocrafterContainerMenu>
    implements AdvancedAutocrafterContainerMenu.Listener, PatternOutputRenderingScreen {

  private static final List<ClientTooltipComponent> EMPTY_PATTERN_SLOT = List.of(ClientTooltipComponent.create(
      createTranslationAsHeading("gui", "autocrafter.empty_pattern_slot").getVisualOrderText()
  ));

  private static final Component CHAINED = IdentifierUtil.createTranslation("gui", "autocrafter.chained");
  private static final Component CHAINED_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.chained.help");
  private static final Component CHAINED_HEAD_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.chained.head_help");
  private static final Component NOT_CHAINED = IdentifierUtil.createTranslation("gui", "autocrafter.not_chained");
  private static final Component NOT_CHAINED_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.not_chained.help");
  private static final Component EDIT = IdentifierUtil.createTranslation("gui", "autocrafter.edit_name");
  private static final Component CURRENTLY_LOCKED = IdentifierUtil.createTranslation("gui", "autocrafter.currently_locked");

  private static final Identifier NAME_BACKGROUND = IdentifierUtil.createIdentifier("widget/autocrafter_name");
  private static final List<String> CRAFTER_NAME_HISTORY = new ArrayList<>();

  private final Inventory playerInventory;

  @Nullable
  private LockModeSideButtonWidget lockModeSideButtonWidget;

  @Nullable
  private EditBox nameField;
  @Nullable
  private Button editButton;
  private boolean editName;

  private final CrafterTier tier;
  private final Identifier texture;

  public AdvancedAutocrafterScreen(AdvancedAutocrafterContainerMenu menu, Inventory inventory,
      Component title) {
    var height = 173 + (menu.getTier().ordinal() * 36);
    super(menu, inventory, new TextMarquee(title, getTitleMaxWidth(menu)), 211, height);
    this.inventoryLabelY = this.imageHeight - 94;
    this.playerInventory = inventory;
    this.tier = menu.getTier();
    this.texture = ExtraStorage.rl("textures/gui/" + tier.getID() + ".png");
  }

  private static int getTitleMaxWidth(final AdvancedAutocrafterContainerMenu menu) {
    final int chainingTitleWidth = Minecraft.getInstance().font.width(getChainingTitle(menu));
    final int editButtonWidth = getEditButtonWidth();
    return TITLE_MAX_WIDTH - chainingTitleWidth - editButtonWidth - 10;
  }

  private int getEditButtonX() {
    return leftPos + titleLabelX + titleMarquee.getEffectiveWidth(font) + 2;
  }

  private static int getEditButtonWidth() {
    return Minecraft.getInstance().font.width(EDIT) + 8;
  }

  private static Component getChainingTitle(final AdvancedAutocrafterContainerMenu menu) {
    return (menu.isPartOfChain() || menu.isHeadOfChain()) ? CHAINED : NOT_CHAINED;
  }

  private Component getChainingTooltip() {
    if (!getMenu().isPartOfChain() && !getMenu().isHeadOfChain()) {
      return NOT_CHAINED_HELP;
    }
    return getMenu().isHeadOfChain() ? CHAINED_HEAD_HELP : CHAINED_HELP;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
      float partialTicks) {
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    if (imageHeight <= 256) {
      graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
    } else {
      graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight, 512, 512);
    }
//    this.renderResourceSlots(graphics);
    super.extractBackground(graphics, mouseX, mouseY, partialTicks);
    if (editName) {
      graphics.blitSprite(GUI_TEXTURED, NAME_BACKGROUND, leftPos + 7, topPos + 5, 162, 12);
    }
  }

  @Override
  protected void init() {
    super.init();
    getMenu().setListener(this);

    tryAddLockModeSideButton();
    addSideButton(new AutocrafterPrioritySideButtonWidget(
        getMenu().getProperty(AutocrafterPropertyTypes.PRIORITY),
        playerInventory,
        this
    ));
    addSideButton(new VisibleToTheAutocrafterManagerSideButtonWidget(
        getMenu().getProperty(AutocrafterPropertyTypes.VISIBLE_TO_THE_AUTOCRAFTER_MANAGER)
    ));

    nameField = new SearchFieldWidget(
        font,
        leftPos + 8 + 1,
        topPos + 6 + 1,
        159 - 6,
        new History(CRAFTER_NAME_HISTORY)
    );
    nameField.setValue(title.getString());
    nameField.setBordered(false);
    nameField.setCanLoseFocus(false);
    addWidget(nameField);

    editButton = addRenderableWidget(Button.builder(EDIT, button -> setEditName(true))
        .pos(getEditButtonX(), topPos + titleLabelY - 3)
        .size(getEditButtonWidth(), 14)
        .build());
    editButton.active = getMenu().canChangeName();

    setEditName(false);
  }

  private void tryAddLockModeSideButton() {
    if (getMenu().isPartOfChain()) {
      return;
    }
    lockModeSideButtonWidget = new LockModeSideButtonWidget(
        getMenu().getProperty(AutocrafterPropertyTypes.LOCK_MODE)
    );
    lockedChanged(getMenu().isLocked());
    addSideButton(lockModeSideButtonWidget);
  }

  private void setEditName(final boolean editName) {
    this.editName = editName;
    if (nameField != null) {
      nameField.visible = editName;
      nameField.setFocused(editName);
      nameField.setCanLoseFocus(!editName);
      if (editName) {
        setFocused(nameField);
      } else {
        setFocused(null);
      }
    }
    if (editButton != null) {
      editButton.visible = !editName;
    }
  }

  @Override
  public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
    super.extractContents(graphics, mouseX, mouseY, partialTicks);
    if (nameField != null && editName) {
      nameField.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    if (editName) {
      renderPlayerInventoryTitle(graphics);
      return;
    }
    super.extractLabels(graphics, mouseX, mouseY);
    final Component title = getChainingTitle(menu);
    graphics.text(font, title, getChainingTitleX(title), titleLabelY, 4210752, false);
  }

  private int getChainingTitleX(final Component title) {
    return imageWidth - 41 - font.width(title);
  }

  @Override
  public boolean charTyped(CharacterEvent event) {
    return (nameField != null && editName && nameField.charTyped(event))
        || super.charTyped(event);
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    if (nameField != null && editName) {
      if (nameField.isFocused() && saveOrCancel(event.key())) {
        return true;
      }
      return nameField.keyPressed(event);
    }
    return super.keyPressed(event);
  }

  private boolean saveOrCancel(final int key) {
    if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
      getMenu().changeName(Objects.requireNonNull(nameField).getValue());
      setEditName(false);
      return true;
    } else if (key == GLFW.GLFW_KEY_ESCAPE) {
      setEditName(false);
      Objects.requireNonNull(nameField).setValue(titleMarquee.getText().getString());
      return true;
    }
    return false;
  }

  @Override
  protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
    if (hoveredSlot instanceof PatternSlot patternSlot
        && !patternSlot.hasItem()
        && getMenu().getCarried().isEmpty()) {
      graphics.tooltip(font, EMPTY_PATTERN_SLOT, x, y, DefaultTooltipPositioner.INSTANCE, null);
      return;
    }
    final Component chainingTitle = getChainingTitle(getMenu());
    final int chainingTitleX = getChainingTitleX(chainingTitle);
    if (isHovering(chainingTitleX, titleLabelY, font.width(chainingTitle), font.lineHeight, x, y)) {
      final Component chainingTooltip = getChainingTooltip();
      graphics.tooltip(
          font,
          List.of(HelpClientTooltipComponent.createAlwaysDisplayed(chainingTooltip)),
          x,
          y,
          DefaultTooltipPositioner.INSTANCE,
          null
      );
      return;
    }
    super.extractTooltip(graphics, x, y);
  }

  @Override
  protected Identifier getTexture() {
    return this.texture;
  }

  @Override
  public void nameChanged(final Component name) {
    titleMarquee.setText(name);
    if (nameField != null) {
      nameField.setValue(name.getString());
    }
    if (editButton != null) {
      editButton.setX(getEditButtonX());
    }
  }

  @Override
  public void lockedChanged(final boolean locked) {
    if (lockModeSideButtonWidget == null) {
      return;
    }
    if (locked) {
      lockModeSideButtonWidget.setWarning(CURRENTLY_LOCKED);
      return;
    }
    lockModeSideButtonWidget.setWarning(null);
  }

  @Override
  public boolean canDisplayOutput(ItemStack stack) {
    return getMenu().containsPattern(stack);
  }
}
