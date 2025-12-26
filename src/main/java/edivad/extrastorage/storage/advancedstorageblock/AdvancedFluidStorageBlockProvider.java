package edivad.extrastorage.storage.advancedstorageblock;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceFactory;
import com.refinedmods.refinedstorage.common.storage.StorageTypes;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AdvancedFluidStorageBlockProvider implements StorageBlockProvider {

  private final AdvancedFluidStorageVariant variant;
  private final Component displayName;

  public AdvancedFluidStorageBlockProvider(AdvancedFluidStorageVariant variant) {
    this.variant = variant;
    this.displayName = Component.translatable("block.%s.block_%s_fluid"
        .formatted(ExtraStorage.ID, variant.getName()));
  }

  @Override
  public SerializableStorage createStorage(Runnable runnable) {
    return StorageTypes.FLUID.create(variant.getCapacity(), runnable);
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
    return RefinedStorageApi.INSTANCE.getFluidResourceFactory();
  }

  @Override
  public BlockEntityType<?> getBlockEntityType() {
    return ESBlockEntities.FLUID_STORAGE.get(variant).get();
  }

  @Override
  public MenuType<?> getMenuType() {
    return ESContainer.FLUID_STORAGE.get(variant).get();
  }
}
