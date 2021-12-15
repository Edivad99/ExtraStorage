package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import net.minecraft.world.entity.player.Player;

public class AdvancedFluidStorageBlockContainerMenu extends BaseContainerMenu
{
    public AdvancedFluidStorageBlockContainerMenu(int windowId, Player player, AdvancedFluidStorageBlockEntity tile)
    {
        super(Registration.FLUID_STORAGE_CONTAINER.get(tile.getFluidStorageType()).get(),tile, player, windowId);

        for (int i = 0; i < 9; ++i) {
            addSlot(new FluidFilterSlot(tile.getNode().getFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);

        transferManager.addFluidFilterTransfer(player.getInventory(), tile.getNode().getFilters());
    }
}
