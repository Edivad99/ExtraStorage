package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.AdvancedFluidStorageBlockItem;
import edivad.extrastorage.items.storage.AdvancedStorageBlockItem;
import edivad.extrastorage.items.storage.fluid.ExpandedStorageDiskFluid;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ExpandedStorageDiskItem;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ESItems {

  public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, RegistryObject<BaseBlockItem>> CRAFTER = new HashMap<>();
  public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE_PART = new HashMap<>();
  public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE_PART = new HashMap<>();
  public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_DISK = new HashMap<>();
  public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_DISK = new HashMap<>();
  private static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(ForgeRegistries.ITEMS, ExtraStorage.ID);
  public static final RegistryObject<Item> ADVANCED_EXPORTER =
      ITEMS.register("advanced_exporter",
          () -> new BaseBlockItem(ESBlocks.ADVANCED_EXPORTER.get(), new Item.Properties()));
  public static final RegistryObject<Item> ADVANCED_IMPORTER =
      ITEMS.register("advanced_importer",
          () -> new BaseBlockItem(ESBlocks.ADVANCED_IMPORTER.get(), new Item.Properties()));

  public static final RegistryObject<Item> RAW_NEURAL_PROCESSOR =
      ITEMS.register("raw_neural_processor", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> NEURAL_PROCESSOR =
      ITEMS.register("neural_processor", () -> new Item(new Item.Properties()));

  static {
    for (var type : ItemStorageType.values()) {
      ITEM_STORAGE.put(type,
          ITEMS.register("block_" + type.getName(),
              () -> new AdvancedStorageBlockItem(ESBlocks.ITEM_STORAGE.get(type).get(),
                  new Item.Properties())));
      ITEM_STORAGE_PART.put(type,
          ITEMS.register("storagepart_" + type.getName(), () -> new Item(new Item.Properties())));
      ITEM_DISK.put(type,
          ITEMS.register("disk_" + type.getName(), () -> new ExpandedStorageDiskItem(type)));
    }
    for (var type : FluidStorageType.values()) {
      FLUID_STORAGE.put(type,
          ITEMS.register("block_" + type.getName() + "_fluid",
              () -> new AdvancedFluidStorageBlockItem(ESBlocks.FLUID_STORAGE.get(type).get(),
                  new Item.Properties())));
      FLUID_STORAGE_PART.put(type,
          ITEMS.register("storagepart_" + type.getName() + "_fluid",
              () -> new Item(new Item.Properties())));
      FLUID_DISK.put(type,
          ITEMS.register("disk_" + type.getName() + "_fluid",
              () -> new ExpandedStorageDiskFluid(type)));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, ITEMS.register(tier.getID(),
          () -> new BaseBlockItem(ESBlocks.CRAFTER.get(tier).get(), new Item.Properties())));
    }
  }

  public static void register(IEventBus modEventBus) {
    ITEMS.register(modEventBus);
  }
}
