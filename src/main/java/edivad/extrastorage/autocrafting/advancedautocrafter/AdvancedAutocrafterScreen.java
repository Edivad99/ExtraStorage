package edivad.extrastorage.autocrafting.advancedautocrafter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import com.refinedmods.refinedstorage.common.Platform;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AdvancedAutocrafterScreen extends AbstractBaseScreen<AdvancedAutocrafterContainerMenu>
    implements AdvancedAutocrafterContainerMenu.Listener, PatternOutputRenderingScreen {

  private static final Component EMPTY_PATTERN_SLOT = IdentifierUtil.createTranslationAsHeading(
      "gui", "autocrafter.empty_pattern_slot"
  );

  private static final Component CHAINED = IdentifierUtil.createTranslation("gui", "autocrafter.chained");
  private static final Component CHAINED_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.chained.help");
  private static final Component CHAINED_HEAD_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.chained.head_help");
  private static final Component NOT_CHAINED = IdentifierUtil.createTranslation("gui", "autocrafter.not_chained");
  private static final Component NOT_CHAINED_HELP = IdentifierUtil.createTranslation("gui", "autocrafter.not_chained.help");
  private static final Component EDIT = IdentifierUtil.createTranslation("gui", "autocrafter.edit_name");
  private static final Component CURRENTLY_LOCKED = IdentifierUtil.createTranslation("gui", "autocrafter.currently_locked");

  private static final ResourceLocation NAME_BACKGROUND = IdentifierUtil.createIdentifier("widget/autocrafter_name");
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
  private final ResourceLocation texture;

  public AdvancedAutocrafterScreen(AdvancedAutocrafterContainerMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, new TextMarquee(title, getTitleMaxWidth(menu)));
    this.imageWidth = 211;
    this.imageHeight = 173 + (menu.getTier().ordinal() * 36);
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
  protected void renderBg(GuiGraphics graphics, final float delta, final int mouseX, final int mouseY) {
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;
    if (imageHeight <= 256) {
      graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
    } else {
      graphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight, 512, 512);
    }
    this.renderResourceSlots(graphics);
    if (editName) {
      graphics.blitSprite(NAME_BACKGROUND, leftPos + 7, topPos + 5, 162, 12);
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
  public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks) {
    super.render(graphics, mouseX, mouseY, partialTicks);
    if (nameField != null && editName) {
      nameField.render(graphics, mouseX, mouseY, partialTicks);
    }
  }

  @Override
  protected void renderLabels(final GuiGraphics graphics, final int mouseX, final int mouseY) {
    if (editName) {
      renderPlayerInventoryTitle(graphics);
      return;
    }
    super.renderLabels(graphics, mouseX, mouseY);
    final Component title = getChainingTitle(menu);
    graphics.drawString(font, title, getChainingTitleX(title), titleLabelY, 4210752, false);
  }

  private int getChainingTitleX(final Component title) {
    return imageWidth - 41 - font.width(title);
  }

  @Override
  public boolean charTyped(final char unknown1, final int unknown2) {
    return (nameField != null && editName && nameField.charTyped(unknown1, unknown2))
        || super.charTyped(unknown1, unknown2);
  }

  @Override
  public boolean keyPressed(final int key, final int scanCode, final int modifiers) {
    if (nameField != null && editName) {
      if (nameField.isFocused() && saveOrCancel(key)) {
        return true;
      }
      return nameField.keyPressed(key, scanCode, modifiers);
    }
    return super.keyPressed(key, scanCode, modifiers);
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
  protected void renderTooltip(final GuiGraphics graphics, final int x, final int y) {
    if (hoveredSlot instanceof PatternSlot patternSlot
        && !patternSlot.hasItem()
        && getMenu().getCarried().isEmpty()) {
      graphics.renderTooltip(font, EMPTY_PATTERN_SLOT, x, y);
      return;
    }
    final Component chainingTitle = getChainingTitle(getMenu());
    final int chainingTitleX = getChainingTitleX(chainingTitle);
    if (isHovering(chainingTitleX, titleLabelY, font.width(chainingTitle), font.lineHeight, x, y)) {
      final Component chainingTooltip = getChainingTooltip();
      Platform.INSTANCE.renderTooltip(
          graphics,
          List.of(HelpClientTooltipComponent.createAlwaysDisplayed(chainingTooltip)),
          x,
          y
      );
      return;
    }
    super.renderTooltip(graphics, x, y);
  }

  @Override
  protected ResourceLocation getTexture() {
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
