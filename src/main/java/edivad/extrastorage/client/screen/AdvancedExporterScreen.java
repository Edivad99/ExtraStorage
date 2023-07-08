package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.container.AdvancedExporterContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedExporterScreen extends BaseScreen<AdvancedExporterContainerMenu> {
  private static final ResourceLocation TEXTURE =
      ExtraStorage.rl("textures/gui/advanced_exporter_importer.png");
  private boolean hasRegulatorMode;

  public AdvancedExporterScreen(AdvancedExporterContainerMenu container, Inventory inventory,
      Component title) {
    super(container, 211, 155, inventory, title);
    this.hasRegulatorMode = hasRegulatorMode();
  }

  private boolean hasRegulatorMode() {
    return menu.getBlockEntity().getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);
  }

  @Override
  public void onPostInit(int x, int y) {
    addSideButton(new RedstoneModeSideButton(this, AdvancedExporterBlockEntity.REDSTONE_MODE));
    addSideButton(new TypeSideButton(this, AdvancedExporterBlockEntity.TYPE));
    addSideButton(new ExactModeSideButton(this, AdvancedExporterBlockEntity.COMPARE));
  }

  @Override
  public void tick(int x, int y) {
    boolean updatedHasRegulatorMode = hasRegulatorMode();
    if (hasRegulatorMode != updatedHasRegulatorMode) {
      hasRegulatorMode = updatedHasRegulatorMode;
      menu.initSlots();
    }
  }

  @Override
  public void renderBackground(GuiGraphics poseStack, int x, int y, int mouseX, int mouseY) {
    poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void renderForeground(GuiGraphics guiGraphics, int i, int i1) {
    renderString(guiGraphics, 7, 7, RenderUtils.shorten(title.getString(), 26));
    renderString(guiGraphics, 7, 60, Component.translatable("container.inventory").getString());
  }
}
