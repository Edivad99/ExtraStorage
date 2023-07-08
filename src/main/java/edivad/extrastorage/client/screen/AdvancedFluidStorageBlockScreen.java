package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenSynchronizationParameters;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedFluidStorageBlockScreen extends
    StorageScreen<AdvancedFluidStorageBlockContainerMenu> {

  public AdvancedFluidStorageBlockScreen(AdvancedFluidStorageBlockContainerMenu container,
      Inventory inventory, Component title) {
    super(
        container,
        inventory,
        title,
        new ResourceLocation("refinedstorage", "textures/gui/storage.png"),
        new StorageScreenSynchronizationParameters(null,
            AdvancedFluidStorageBlockEntity.REDSTONE_MODE,
            AdvancedFluidStorageBlockEntity.COMPARE,
            AdvancedFluidStorageBlockEntity.WHITELIST_BLACKLIST,
            AdvancedFluidStorageBlockEntity.PRIORITY,
            AdvancedFluidStorageBlockEntity.ACCESS_TYPE
        ),
        AdvancedFluidStorageBlockEntity.STORED::getValue,
        () -> (long) ((AdvancedFluidStorageBlockEntity) container.getBlockEntity()).getFluidStorageType()
            .getCapacity());
  }
}
