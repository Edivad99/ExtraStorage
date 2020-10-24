package edivad.extrastorage.client.screen;

import com.refinedmods.refinedstorage.screen.StorageScreen;
import com.refinedmods.refinedstorage.screen.StorageScreenTileDataParameters;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainer;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class AdvancedFluidStorageBlockScreen extends StorageScreen<AdvancedFluidStorageBlockContainer>
{
    public AdvancedFluidStorageBlockScreen(AdvancedFluidStorageBlockContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(
                container,
                inventory,
                title,
                "gui/storage.png",
                new StorageScreenTileDataParameters(null,
                        AdvancedFluidStorageBlockTile.REDSTONE_MODE,
                        AdvancedFluidStorageBlockTile.COMPARE,
                        AdvancedFluidStorageBlockTile.WHITELIST_BLACKLIST,
                        AdvancedFluidStorageBlockTile.PRIORITY,
                        AdvancedFluidStorageBlockTile.ACCESS_TYPE
                ),
                AdvancedFluidStorageBlockTile.STORED::getValue,
                () -> (long) ((AdvancedFluidStorageBlockTile) container.getTile()).getFluidStorageType().getCapacity());
    }
}
