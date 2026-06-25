package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlockItem;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedFluidStorageBlockBlockItem;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedStorageBlockItem;
import edivad.extrastorage.storage.expandedstoragedisk.ExpandedStorageDiskFluid;
import edivad.extrastorage.storage.expandedstoragedisk.ExpandedStorageDiskItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {

  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_STORAGE = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredItem<AdvancedAutocrafterBlockItem>> CRAFTER = new HashMap<>();
  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_STORAGE_PART = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_STORAGE_PART = new HashMap<>();
  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_DISK = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_DISK = new HashMap<>();
  private static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(ExtraStorage.ID);
  public static final DeferredItem<Item> ADVANCED_EXPORTER =
      ITEMS.register("advanced_exporter",
          identifier -> new BaseBlockItem(identifier, ESBlocks.ADVANCED_EXPORTER.get()));
  public static final DeferredItem<Item> ADVANCED_IMPORTER =
      ITEMS.register("advanced_importer",
          identifier -> new BaseBlockItem(identifier, ESBlocks.ADVANCED_IMPORTER.get()));

  public static final DeferredItem<Item> RAW_NEURAL_PROCESSOR =
      ITEMS.registerItem("raw_neural_processor", Item::new);
  public static final DeferredItem<Item> NEURAL_PROCESSOR =
      ITEMS.registerItem("neural_processor", Item::new);

  static {
    for (var variant : AdvancedItemStorageVariant.values()) {
      ITEM_STORAGE.put(variant, ITEMS.register("block_" + variant.getName(),
          identifier ->
              new AdvancedStorageBlockItem(identifier, ESBlocks.ITEM_STORAGE.get(variant).get(), variant)));
      ITEM_STORAGE_PART.put(variant, ITEMS.registerItem(variant.getName() + "_item_storage_part", Item::new));
      ITEM_DISK.put(variant,
          ITEMS.register(variant.getName() + "_item_storage_disk",
              identifier -> new ExpandedStorageDiskItem(identifier, variant)));
    }
    for (var variant : AdvancedFluidStorageVariant.values()) {
      var variantName = variant.getName() + "_fluid";
      FLUID_STORAGE.put(variant, ITEMS.register("block_" + variantName,
          identifier ->
              new AdvancedFluidStorageBlockBlockItem(identifier, ESBlocks.FLUID_STORAGE.get(variant).get(), variant)));
      FLUID_STORAGE_PART.put(variant, ITEMS.registerItem(variantName + "_storage_part", Item::new));
      FLUID_DISK.put(variant,
          ITEMS.register(variantName + "_storage_disk",
              identifier -> new ExpandedStorageDiskFluid(identifier, variant)));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, ITEMS.registerItem(tier.getID(), properties ->
          new AdvancedAutocrafterBlockItem(ESBlocks.CRAFTER.get(tier).get(), properties.useBlockDescriptionPrefix())));
    }
  }

  public static void register(IEventBus modEventBus) {
    ITEMS.register(modEventBus);
  }
}
