package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESBlockEntities {

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExtraStorage.ID);

  public static final Map<ItemStorageType, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedStorageBlockEntity>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedFluidStorageBlockEntity>>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedCrafterBlockEntity>>> CRAFTER = new HashMap<>();

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedExporterBlockEntity>> ADVANCED_EXPORTER =
      BLOCK_ENTITIES.register("advanced_exporter",
          () -> registerSynchronizationParameters(AdvancedExporterBlockEntity.SPEC,
              BlockEntityType.Builder.of(AdvancedExporterBlockEntity::new,
                      ESBlocks.ADVANCED_EXPORTER.get())
                  .build(null)));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedImporterBlockEntity>> ADVANCED_IMPORTER =
      BLOCK_ENTITIES.register("advanced_importer",
          () -> registerSynchronizationParameters(AdvancedImporterBlockEntity.SPEC,
              BlockEntityType.Builder.of(AdvancedImporterBlockEntity::new,
                      ESBlocks.ADVANCED_IMPORTER.get())
                  .build(null)));

  static {
    for (var type : ItemStorageType.values()) {
      ITEM_STORAGE.put(type, BLOCK_ENTITIES.register("block_" + type.getName(),
          () -> registerSynchronizationParameters(AdvancedStorageBlockEntity.SPEC,
              BlockEntityType.Builder.of(
                  (pos, state) -> new AdvancedStorageBlockEntity(type, pos, state),
                  ESBlocks.ITEM_STORAGE.get(type).get()).build(null))));
    }
    for (var type : FluidStorageType.values()) {
      FLUID_STORAGE.put(type, BLOCK_ENTITIES.register("block_" + type.getName() + "_fluid",
          () -> registerSynchronizationParameters(AdvancedFluidStorageBlockEntity.SPEC,
              BlockEntityType.Builder.of(
                  (pos, state) -> new AdvancedFluidStorageBlockEntity(type, pos, state),
                  ESBlocks.FLUID_STORAGE.get(type).get()).build(null))));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, BLOCK_ENTITIES.register(tier.getID(),
          () -> registerSynchronizationParameters(AdvancedCrafterBlockEntity.SPEC,
              BlockEntityType.Builder.of(
                  (pos, state) -> new AdvancedCrafterBlockEntity(tier, pos, state),
                  ESBlocks.CRAFTER.get(tier).get()).build(null))));
    }
  }

  private static <T extends BlockEntity> BlockEntityType<T> registerSynchronizationParameters(
      BlockEntitySynchronizationSpec spec, BlockEntityType<T> t) {
    spec.getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    return t;
  }

  public static void register(IEventBus modEventBus) {
    BLOCK_ENTITIES.register(modEventBus);
  }
}
