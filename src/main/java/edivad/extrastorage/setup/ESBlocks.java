package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.AdvancedCrafterBlock;
import edivad.extrastorage.blocks.AdvancedExporterBlock;
import edivad.extrastorage.blocks.AdvancedFluidStorageBlock;
import edivad.extrastorage.blocks.AdvancedImporterBlock;
import edivad.extrastorage.blocks.AdvancedStorageBlock;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ESBlocks {

  private static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(ForgeRegistries.BLOCKS, ExtraStorage.MODID);

  public static final Map<ItemStorageType, RegistryObject<AdvancedStorageBlock>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, RegistryObject<AdvancedFluidStorageBlock>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, RegistryObject<AdvancedCrafterBlock>> CRAFTER = new HashMap<>();
  public static final RegistryObject<AdvancedExporterBlock> ADVANCED_EXPORTER =
      BLOCKS.register("advanced_exporter", AdvancedExporterBlock::new);
  public static final RegistryObject<AdvancedImporterBlock> ADVANCED_IMPORTER =
      BLOCKS.register("advanced_importer", AdvancedImporterBlock::new);

  static {
    for (var type : ItemStorageType.values()) {
      ITEM_STORAGE.put(type,
          BLOCKS.register("block_" + type.getName(), () -> new AdvancedStorageBlock(type)));
    }
    for (var type : FluidStorageType.values()) {
      FLUID_STORAGE.put(type,
          BLOCKS.register("block_" + type.getName() + "_fluid",
              () -> new AdvancedFluidStorageBlock(type)));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, BLOCKS.register(tier.getID(), () -> new AdvancedCrafterBlock(tier)));
    }
  }

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
