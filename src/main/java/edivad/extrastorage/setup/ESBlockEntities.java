package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.advancedexporter.AdvancedExporterBlockEntity;
import edivad.extrastorage.advancedimporter.AdvancedImporterBlockEntity;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlockEntity;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedFluidStorageBlockProvider;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedItemStorageBlockBlockProvider;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedStorageBlockBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESBlockEntities {

  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ExtraStorage.ID);

  public static final Map<AdvancedItemStorageVariant, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedStorageBlockBlockEntity>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedStorageBlockBlockEntity>>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedAutocrafterBlockEntity>>> CRAFTER = new HashMap<>();

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedExporterBlockEntity>> ADVANCED_EXPORTER =
      BLOCK_ENTITIES.register("advanced_exporter",
          () -> new BlockEntityType<>(AdvancedExporterBlockEntity::new,
                  ESBlocks.ADVANCED_EXPORTER.get()));

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AdvancedImporterBlockEntity>> ADVANCED_IMPORTER =
      BLOCK_ENTITIES.register("advanced_importer",
          () -> new BlockEntityType<>(AdvancedImporterBlockEntity::new,
                  ESBlocks.ADVANCED_IMPORTER.get()));

  static {
    for (var type : AdvancedItemStorageVariant.values()) {
      ITEM_STORAGE.put(type, BLOCK_ENTITIES.register("block_" + type.getName(),
          () -> new BlockEntityType<>((pos, state) ->
                  new AdvancedStorageBlockBlockEntity(pos, state, new AdvancedItemStorageBlockBlockProvider(type)),
              ESBlocks.ITEM_STORAGE.get(type).get())));
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      FLUID_STORAGE.put(type, BLOCK_ENTITIES.register("block_" + type.getName() + "_fluid",
          () -> new BlockEntityType<>((pos, state) ->
                  new AdvancedStorageBlockBlockEntity(pos, state, new AdvancedFluidStorageBlockProvider(type)),
              ESBlocks.FLUID_STORAGE.get(type).get())));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, BLOCK_ENTITIES.register(tier.getID(),
          () -> new BlockEntityType<>((pos, state) ->
                  new AdvancedAutocrafterBlockEntity(tier, pos, state),
              ESBlocks.CRAFTER.get(tier).get())));
    }
  }

  public static void register(IEventBus modEventBus) {
    BLOCK_ENTITIES.register(modEventBus);
  }
}
