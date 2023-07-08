package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.setup.ESContainer;
import net.minecraft.world.entity.player.Player;

public class AdvancedStorageBlockContainerMenu extends BaseContainerMenu {

  public AdvancedStorageBlockContainerMenu(int windowId, Player player,
      AdvancedStorageBlockEntity storageBlockEntity) {
    super(ESContainer.ITEM_STORAGE.get(storageBlockEntity.getItemStorageType()).get(),
        storageBlockEntity, player, windowId);

    for (int i = 0; i < 9; ++i) {
      addSlot(new FilterSlot(storageBlockEntity.getNode().getFilters(), i, 8 + (18 * i), 20));
    }

    addPlayerInventory(8, 141);

    transferManager.addItemFilterTransfer(player.getInventory(),
        storageBlockEntity.getNode().getFilters());
  }
}
