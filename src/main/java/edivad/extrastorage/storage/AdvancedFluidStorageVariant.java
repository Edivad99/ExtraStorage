package edivad.extrastorage.storage;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import edivad.extrastorage.setup.ESItems;
import lombok.Getter;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

@Getter
public enum AdvancedFluidStorageVariant implements StorageVariant, StringRepresentable {
  TIER_5(16384),
  TIER_6(65536),
  TIER_7(262144),
  TIER_8(1048576);

  private final long capacityInBuckets;
  private final String name;
  private final long energyUsage;

  AdvancedFluidStorageVariant(int capacityInBuckets) {
    this.name = capacityInBuckets + "b";
    this.capacityInBuckets = capacityInBuckets;
    this.energyUsage = (this.ordinal() + 5) * 2;
  }

  @Override
  public Long getCapacity() {
    return capacityInBuckets * Platform.INSTANCE.getBucketAmount();
  }

  @Override
  public Item getStoragePart() {
    return ESItems.FLUID_STORAGE_PART.get(this).get();
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
