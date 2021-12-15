package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenSynchronizationParameters;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.container.AdvancedStorageBlockContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedStorageBlockScreen extends StorageScreen<AdvancedStorageBlockContainerMenu>
{
    public AdvancedStorageBlockScreen(AdvancedStorageBlockContainerMenu container, Inventory inventory, Component title)
    {
        super(
                container,
                inventory,
                title,
                "gui/storage.png",
                new StorageScreenSynchronizationParameters(null,
                    AdvancedStorageBlockEntity.REDSTONE_MODE,
                    AdvancedStorageBlockEntity.COMPARE,
                    AdvancedStorageBlockEntity.WHITELIST_BLACKLIST,
                    AdvancedStorageBlockEntity.PRIORITY,
                    AdvancedStorageBlockEntity.ACCESS_TYPE
                ),
                AdvancedStorageBlockEntity.STORED::getValue,
                () -> (long) ((AdvancedStorageBlockEntity) container.getBlockEntity()).getItemStorageType().getCapacity());
    }
}
