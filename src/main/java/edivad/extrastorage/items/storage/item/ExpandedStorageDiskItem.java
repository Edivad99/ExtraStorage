package edivad.extrastorage.items.storage.item;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import edivad.extrastorage.items.storage.ExpandedStorageDisk;
import edivad.extrastorage.setup.ESItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExpandedStorageDiskItem extends ExpandedStorageDisk {

  private final ItemStorageType type;

  public ExpandedStorageDiskItem(ItemStorageType type) {
    super();
    this.type = type;
  }

  public static Item getPartById(ItemStorageType type) {
    return ESItems.ITEM_STORAGE_PART.get(type).get();
  }

  @Override
  protected Item getPart() {
    return getPartById(this.type);
  }

  @Override
  public int getCapacity(ItemStack itemStack) {
    return this.type.getCapacity();
  }

  @Override
  public StorageType getType() {
    return StorageType.ITEM;
  }
}
