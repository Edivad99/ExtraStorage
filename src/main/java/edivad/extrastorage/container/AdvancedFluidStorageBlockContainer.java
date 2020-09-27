package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainer;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import net.minecraft.entity.player.PlayerEntity;

public class AdvancedFluidStorageBlockContainer extends BaseContainer
{
    public AdvancedFluidStorageBlockContainer(int windowId, PlayerEntity player, AdvancedFluidStorageBlockTile tile)
    {
        super(Registration.FLUID_STORAGE_CONTAINER.get(tile.getFluidStorageType()).get(),tile, player, windowId);

        for (int i = 0; i < 9; ++i) {
            addSlot(new FluidFilterSlot(tile.getNode().getFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);

        transferManager.addFluidFilterTransfer(player.inventory, tile.getNode().getFilters());
    }
}
