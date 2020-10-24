package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenTileDataParameters;
import edivad.extrastorage.container.AdvancedStorageBlockContainer;
import edivad.extrastorage.tiles.AdvancedStorageBlockTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class AdvancedStorageBlockScreen extends StorageScreen<AdvancedStorageBlockContainer>
{
    public AdvancedStorageBlockScreen(AdvancedStorageBlockContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(
                container,
                inventory,
                title,
                "gui/storage.png",
                new StorageScreenTileDataParameters(null,
                    AdvancedStorageBlockTile.REDSTONE_MODE,
                    AdvancedStorageBlockTile.COMPARE,
                    AdvancedStorageBlockTile.WHITELIST_BLACKLIST,
                    AdvancedStorageBlockTile.PRIORITY,
                    AdvancedStorageBlockTile.ACCESS_TYPE
                ),
                AdvancedStorageBlockTile.STORED::getValue,
                () -> (long) ((AdvancedStorageBlockTile) container.getTile()).getItemStorageType().getCapacity());
    }
}
