package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.setup.Registration;
import net.minecraft.world.entity.player.Player;

public class AdvancedStorageBlockContainerMenu extends BaseContainerMenu
{
    public AdvancedStorageBlockContainerMenu(int windowId, Player player, AdvancedStorageBlockEntity tile)
    {
        super(Registration.ITEM_STORAGE_CONTAINER.get(tile.getItemStorageType()).get(),tile, player, windowId);

        for (int i = 0; i < 9; ++i) {
            addSlot(new FilterSlot(tile.getNode().getFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);

        transferManager.addItemFilterTransfer(player.getInventory(), tile.getNode().getFilters());
    }
}
