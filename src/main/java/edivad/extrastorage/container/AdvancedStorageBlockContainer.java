package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainer;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import net.minecraft.world.entity.player.Player;

public class AdvancedStorageBlockContainer extends BaseContainer
{
    public AdvancedStorageBlockContainer(int windowId, Player player, AdvancedStorageBlockEntity tile)
    {
        super(Registration.ITEM_STORAGE_CONTAINER.get(tile.getItemStorageType()).get(),tile, player, windowId);

        for (int i = 0; i < 9; ++i) {
            addSlot(new FilterSlot(tile.getNode().getFilters(), i, 8 + (18 * i), 20));
        }

        addPlayerInventory(8, 141);

        transferManager.addItemFilterTransfer(player.getInventory(), tile.getNode().getFilters());
    }
}
