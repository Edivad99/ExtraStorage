package edivad.extrastorage.storage.advancedstorageblock;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage.common.storage.StorageTypes;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AdvancedItemStorageBlockBlockProvider implements StorageBlockProvider {

  private final AdvancedItemStorageVariant variant;
  private final Component displayName;

  public AdvancedItemStorageBlockBlockProvider(AdvancedItemStorageVariant variant) {
    this.variant = variant;
    this.displayName = Component.translatable("block.%s.block_%s"
        .formatted(ExtraStorage.ID, variant.getName()));
  }

  @Override
  public SerializableStorage createStorage(Runnable runnable) {
    return StorageTypes.ITEM.create(variant.getCapacity(), runnable);
  }

  @Override
  public Component getDisplayName() {
    return displayName;
  }

  @Override
  public long getEnergyUsage() {
    return variant.getEnergyUsage();
  }

  @Override
  public ResourceFactory getResourceFactory() {
    return RefinedStorageApi.INSTANCE.getItemResourceFactory();
  }

  @Override
  public BlockEntityType<?> getBlockEntityType() {
    return ESBlockEntities.ITEM_STORAGE.get(variant).get();
  }

  @Override
  public MenuType<?> getMenuType() {
    return ESContainer.ITEM_STORAGE.get(variant).get();
  }
}
