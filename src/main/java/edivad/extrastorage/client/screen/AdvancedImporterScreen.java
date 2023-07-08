package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.WhitelistBlacklistSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.container.AdvancedImporterContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedImporterScreen extends BaseScreen<AdvancedImporterContainerMenu> {
  private static final ResourceLocation TEXTURE =
      ExtraStorage.rl("textures/gui/advanced_exporter_importer.png");

  public AdvancedImporterScreen(AdvancedImporterContainerMenu container, Inventory inventory,
      Component title) {
    super(container, 211, 155, inventory, title);
  }

  @Override
  public void onPostInit(int x, int y) {
    addSideButton(new RedstoneModeSideButton(this, AdvancedImporterBlockEntity.REDSTONE_MODE));
    addSideButton(new TypeSideButton(this, AdvancedImporterBlockEntity.TYPE));
    addSideButton(
        new WhitelistBlacklistSideButton(this, AdvancedImporterBlockEntity.WHITELIST_BLACKLIST));
    addSideButton(new ExactModeSideButton(this, AdvancedImporterBlockEntity.COMPARE));
  }

  @Override
  public void tick(int x, int y) {
  }

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
    guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void renderForeground(GuiGraphics guiGraphics, int i, int i1) {
    renderString(guiGraphics, 7, 7, RenderUtils.shorten(title.getString(), 26));
    renderString(guiGraphics, 7, 60, Component.translatable("container.inventory").getString());
  }
}
