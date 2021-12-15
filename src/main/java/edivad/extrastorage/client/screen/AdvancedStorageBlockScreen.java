package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenTileDataParameters;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.container.AdvancedStorageBlockContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedStorageBlockScreen extends StorageScreen<AdvancedStorageBlockContainer>
{
    public AdvancedStorageBlockScreen(AdvancedStorageBlockContainer container, Inventory inventory, Component title)
    {
        super(
                container,
                inventory,
                title,
                "gui/storage.png",
                new StorageScreenTileDataParameters(null,
                    AdvancedStorageBlockEntity.REDSTONE_MODE,
                    AdvancedStorageBlockEntity.COMPARE,
                    AdvancedStorageBlockEntity.WHITELIST_BLACKLIST,
                    AdvancedStorageBlockEntity.PRIORITY,
                    AdvancedStorageBlockEntity.ACCESS_TYPE
                ),
                AdvancedStorageBlockEntity.STORED::getValue,
                () -> (long) ((AdvancedStorageBlockEntity) container.getTile()).getItemStorageType().getCapacity());
    }
}
