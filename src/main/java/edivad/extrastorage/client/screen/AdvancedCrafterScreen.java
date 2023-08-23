package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedCrafterScreen extends BaseScreen<AdvancedCrafterContainerMenu> {

  private final CrafterTier tier;
  private final ResourceLocation texture;

  public AdvancedCrafterScreen(AdvancedCrafterContainerMenu container, Inventory inventory,
      Component title) {
    super(container, 211, 173 + (container.getBlockEntity().getTier().ordinal() * 36), inventory,
        title);
    this.tier = container.getBlockEntity().getTier();
    this.texture = ExtraStorage.rl("textures/gui/" + tier.getID() + ".png");
  }

  @Override
  public void onPostInit(int i, int i1) {
  }

  @Override
  public void tick(int i, int i1) {
  }

  @Override
  public void renderBackground(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
    if (imageHeight <= 256) {
      guiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight);
    } else {
      guiGraphics.blit(texture, x, y, 0, 0, imageWidth, imageHeight, 512, 512);
    }
  }

  @Override
  public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    renderString(guiGraphics, 7, 7, RenderUtils.shorten(title.getString(), 26));
    renderString(guiGraphics, 7, 78 + (36 * tier.ordinal()),
        Component.translatable("container.inventory").getString());
  }
}
