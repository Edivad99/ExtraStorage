package edivad.extrastorage.setup;

import java.util.Collection;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESBlocks {

  public static final Map<ItemStorageType, DeferredBlock<AdvancedStorageBlock>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, DeferredBlock<AdvancedFluidStorageBlock>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredBlock<AdvancedCrafterBlock>> CRAFTER = new HashMap<>();
  private static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(ExtraStorage.ID);
  public static final DeferredBlock<AdvancedExporterBlock> ADVANCED_EXPORTER =
      BLOCKS.register("advanced_exporter", AdvancedExporterBlock::new);
  public static final DeferredBlock<AdvancedImporterBlock> ADVANCED_IMPORTER =
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

  public static Collection<DeferredHolder<Block, ? extends Block>> entries() {
    return BLOCKS.getEntries();
  }

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
