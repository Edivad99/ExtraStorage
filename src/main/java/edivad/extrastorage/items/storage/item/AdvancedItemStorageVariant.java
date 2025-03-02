package edivad.extrastorage.items.storage.item;

import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import edivad.extrastorage.setup.ESItems;
import lombok.Getter;
import net.minecraft.world.item.Item;

@Getter
public enum AdvancedItemStorageVariant implements StorageVariant {
  TIER_5(256),
  TIER_6(1024),
  TIER_7(4096),
  TIER_8(16384);

  private final Long capacity;
  private final String name;
  private final long energyUsage;

  AdvancedItemStorageVariant(int capacity) {
    this.name = capacity + "k";
    this.capacity = capacity * 1000L;
    this.energyUsage = (this.ordinal() + 5) * 2;
  }

  @Override
  public Item getStoragePart() {
    return ESItems.ITEM_STORAGE_PART.get(this).get();
  }
}
