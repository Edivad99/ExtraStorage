package edivad.extrastorage.setup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.common.content.BlockConstants;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.AdvancedAutocrafterBlock;
import edivad.extrastorage.blocks.AdvancedExporterBlock;
import edivad.extrastorage.blocks.AdvancedFluidStorageBlockProvider;
import edivad.extrastorage.blocks.AdvancedImporterBlock;
import edivad.extrastorage.blocks.AdvancedItemStorageBlockProvider;
import edivad.extrastorage.blocks.AdvancedStorageBlock;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.AdvancedFluidStorageVariant;
import edivad.extrastorage.items.storage.item.AdvancedItemStorageVariant;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESBlocks {

  private static final DeferredRegister.Blocks BLOCKS =
      DeferredRegister.createBlocks(ExtraStorage.ID);

  public static final Map<AdvancedItemStorageVariant, DeferredBlock<AdvancedStorageBlock<?>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredBlock<AdvancedStorageBlock<?>>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredBlock<AdvancedAutocrafterBlock>> CRAFTER = new HashMap<>();
  public static final DeferredBlock<AdvancedExporterBlock> ADVANCED_EXPORTER =
      BLOCKS.register("advanced_exporter", AdvancedExporterBlock::new);
  public static final DeferredBlock<AdvancedImporterBlock> ADVANCED_IMPORTER =
      BLOCKS.register("advanced_importer", AdvancedImporterBlock::new);

  static {
    for (var type : AdvancedItemStorageVariant.values()) {
      ITEM_STORAGE.put(type,
          BLOCKS.register("block_" + type.getName(),
              () -> new AdvancedStorageBlock<>(BlockConstants.PROPERTIES,
                  new AdvancedItemStorageBlockProvider(type))));
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      FLUID_STORAGE.put(type,
          BLOCKS.register("block_%s_fluid".formatted(type.getName()),
              () -> new AdvancedStorageBlock<>(BlockConstants.PROPERTIES,
                  new AdvancedFluidStorageBlockProvider(type))));
    }
    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier, BLOCKS.register(tier.getID(), () -> new AdvancedAutocrafterBlock(tier)));
    }
  }

  public static Collection<DeferredHolder<Block, ? extends Block>> entries() {
    return BLOCKS.getEntries();
  }

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
