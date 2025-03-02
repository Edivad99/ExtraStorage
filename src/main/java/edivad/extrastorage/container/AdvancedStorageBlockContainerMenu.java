package edivad.extrastorage.container;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

import com.refinedmods.refinedstorage.common.api.storage.StorageBlockData;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage.common.storage.AbstractStorageContainerMenu;
import com.refinedmods.refinedstorage.common.storage.StorageAccessor;
import com.refinedmods.refinedstorage.common.storage.StorageConfigurationContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.ResourceSlot;
import com.refinedmods.refinedstorage.common.support.containermenu.ResourceSlotType;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public class AdvancedStorageBlockContainerMenu extends AbstractStorageContainerMenu implements StorageAccessor {

  private static final int FILTER_SLOT_X = 8;
  private static final int FILTER_SLOT_Y = 20;

  private long stored;
  private long capacity;

  public AdvancedStorageBlockContainerMenu(MenuType<?> type, int windowId, Player player,
      final StorageBlockData storageBlockData, final ResourceFactory resourceFactory) {
    super(type, windowId);
    this.stored = storageBlockData.stored();
    this.capacity = storageBlockData.capacity();
    addSlots(player, ResourceContainerImpl.createForFilter(resourceFactory, storageBlockData.resources()));
  }

  public AdvancedStorageBlockContainerMenu(MenuType<?> type, int syncId, Player player,
      ResourceContainer resourceContainer, StorageConfigurationContainer configContainer) {
    super(type, syncId, player, configContainer);
    addSlots(player, resourceContainer);
  }

  private void addSlots(final Player player, final ResourceContainer resourceContainer) {
    for (int i = 0; i < resourceContainer.size(); ++i) {
      addSlot(createFilterSlot(resourceContainer, i));
    }
    addPlayerInventory(player.getInventory(), 8, 141);

    transferManager.addFilterTransfer(player.getInventory());
  }

  private Slot createFilterSlot(final ResourceContainer resourceContainer, final int i) {
    final int x = FILTER_SLOT_X + (18 * i);
    return new ResourceSlot(
        resourceContainer,
        i,
        createTranslation("gui", "storage.filter_help"),
        x,
        FILTER_SLOT_Y,
        ResourceSlotType.FILTER
    );
  }

  @Override
  public double getProgress() {
    if (capacity == 0) {
      return 0;
    }
    return (double) getStored() / (double) getCapacity();
  }

  @Override
  public boolean hasCapacity() {
    return capacity > 0;
  }

  @Override
  public long getCapacity() {
    return capacity;
  }

  @Override
  public long getStored() {
    return stored;
  }
}
