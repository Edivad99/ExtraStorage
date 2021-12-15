package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenTileDataParameters;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedFluidStorageBlockScreen extends StorageScreen<AdvancedFluidStorageBlockContainer>
{
    public AdvancedFluidStorageBlockScreen(AdvancedFluidStorageBlockContainer container, Inventory inventory, Component title)
    {
        super(
                container,
                inventory,
                title,
                "gui/storage.png",
                new StorageScreenTileDataParameters(null,
                        AdvancedFluidStorageBlockEntity.REDSTONE_MODE,
                        AdvancedFluidStorageBlockEntity.COMPARE,
                        AdvancedFluidStorageBlockEntity.WHITELIST_BLACKLIST,
                        AdvancedFluidStorageBlockEntity.PRIORITY,
                        AdvancedFluidStorageBlockEntity.ACCESS_TYPE
                ),
                AdvancedFluidStorageBlockEntity.STORED::getValue,
                () -> (long) ((AdvancedFluidStorageBlockEntity) container.getTile()).getFluidStorageType().getCapacity());
    }
}
