package edivad.extrastorage.data.loot.pack;

import java.util.Set;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import edivad.extrastorage.setup.ESBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ExtraStorageBlockLoot extends BlockLootSubProvider {

  protected ExtraStorageBlockLoot(HolderLookup.Provider registries) {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
  }

  @Override
  protected void generate() {
    ESBlocks.CRAFTER.values().forEach(block -> drop(block.get()));
    ESBlocks.ITEM_STORAGE.values().forEach(
        block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction::new));
    ESBlocks.FLUID_STORAGE.values().forEach(
        block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction::new));
    drop(ESBlocks.ADVANCED_EXPORTER.get());
    drop(ESBlocks.ADVANCED_IMPORTER.get());
  }

  private void genBlockItemLootTableWithFunction(Block block, LootItemFunction.Builder builder) {
    add(block, LootTable.lootTable().withPool(
        LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(block)
                .apply(builder))
            .when(ExplosionCondition.survivesExplosion())));
  }

  private void drop(final Block block) {
    add(block, createSingleItemTable(block)
        .apply(copyName()));
  }

  private static CopyComponentsFunction.Builder copyName() {
    return CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
        .include(DataComponents.CUSTOM_NAME);
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return ESBlocks.entries().stream().map(DeferredHolder::get).map(Block.class::cast).toList();
  }
}
