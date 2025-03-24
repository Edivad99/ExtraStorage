package edivad.extrastorage.storage.advancedstorageblock;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

import com.refinedmods.refinedstorage.common.api.support.resource.ResourceRendering;
import com.refinedmods.refinedstorage.common.storage.AbstractProgressStorageScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedStorageBlockScreen extends AbstractProgressStorageScreen<AdvancedStorageBlockContainerMenu> {

  private static final ResourceLocation TEXTURE = createIdentifier("textures/gui/storage.png");

  private final ResourceRendering resourceRendering;


  public AdvancedStorageBlockScreen(AdvancedStorageBlockContainerMenu menu,
      Inventory inventory, Component title, ResourceRendering resourceRendering) {
    super(menu, inventory, title, 80);
    this.resourceRendering = resourceRendering;
  }

  @Override
  protected ResourceLocation getTexture() {
    return TEXTURE;
  }

  @Override
  protected String formatAmount(long qty) {
    return resourceRendering.formatAmount(qty);
  }
}
