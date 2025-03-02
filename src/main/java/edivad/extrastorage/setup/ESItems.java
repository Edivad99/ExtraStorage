package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.AdvancedFluidStorageBlockItem;
import edivad.extrastorage.items.storage.AdvancedStorageBlockItem;
import edivad.extrastorage.items.storage.fluid.AdvancedFluidStorageVariant;
import edivad.extrastorage.items.storage.fluid.ExpandedStorageDiskFluid;
import edivad.extrastorage.items.storage.item.AdvancedItemStorageVariant;
import edivad.extrastorage.items.storage.item.ExpandedStorageDiskItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {

  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_STORAGE = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredItem<BaseBlockItem>> CRAFTER = new HashMap<>();
  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_STORAGE_PART = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_STORAGE_PART = new HashMap<>();
  public static final Map<AdvancedItemStorageVariant, DeferredItem<Item>> ITEM_DISK = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredItem<Item>> FLUID_DISK = new HashMap<>();
  private static final DeferredRegister.Items ITEMS =
      DeferredRegister.createItems(ExtraStorage.ID);
  public static final DeferredItem<Item> ADVANCED_EXPORTER =
      ITEMS.register("advanced_exporter",
          () -> new BaseBlockItem(ESBlocks.ADVANCED_EXPORTER.get(), null));
  public static final DeferredItem<Item> ADVANCED_IMPORTER =
      ITEMS.register("advanced_importer",
          () -> new BaseBlockItem(ESBlocks.ADVANCED_IMPORTER.get(), null));

  public static final DeferredItem<Item> RAW_NEURAL_PROCESSOR =
      ITEMS.registerItem("raw_neural_processor", Item::new);
  public static final DeferredItem<Item> NEURAL_PROCESSOR =
      ITEMS.registerItem("neural_processor", Item::new);

  static {
    for (var variant : AdvancedItemStorageVariant.values()) {
      ITEM_STORAGE.put(variant, ITEMS.register("block_" + variant.getName(),
          () -> new AdvancedStorageBlockItem(ESBlocks.ITEM_STORAGE.get(variant).get(), variant)));
      ITEM_STORAGE_PART.put(variant, ITEMS.registerItem("storagepart_" + variant.getName(), Item::new));
      ITEM_DISK.put(variant,
          ITEMS.register("disk_" + variant.getName(), () -> new ExpandedStorageDiskItem(variant)));
    }
    for (var variant : AdvancedFluidStorageVariant.values()) {
      var variantName = variant.getName() + "_fluid";
      FLUID_STORAGE.put(variant, ITEMS.register("block_" + variantName,
          () -> new AdvancedFluidStorageBlockItem(ESBlocks.FLUID_STORAGE.get(variant).get(), variant)));
      FLUID_STORAGE_PART.put(variant, ITEMS.registerItem("storagepart_" + variantName, Item::new));
      FLUID_DISK.put(variant,
          ITEMS.register("disk_" + variantName, () -> new ExpandedStorageDiskFluid(variant)));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, ITEMS.register(tier.getID(),
          () -> new BaseBlockItem(ESBlocks.CRAFTER.get(tier).get(), null)));
    }
  }

  public static void register(IEventBus modEventBus) {
    ITEMS.register(modEventBus);
  }
}
