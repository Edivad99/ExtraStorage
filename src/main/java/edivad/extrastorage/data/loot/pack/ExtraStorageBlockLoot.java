package edivad.extrastorage.data.loot.pack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import edivad.extrastorage.setup.ESBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

public class ExtraStorageBlockLoot extends BlockLootSubProvider {

  protected ExtraStorageBlockLoot() {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags());
  }

  @Override
  protected void generate() {
    ESBlocks.CRAFTER.values().forEach(
        block -> genBlockItemLootTableWithFunction(block.get(),
            AdvancedCrafterLootFunction.builder()));
    ESBlocks.ITEM_STORAGE.values().forEach(
        block -> genBlockItemLootTableWithFunction(block.get(),
            StorageBlockLootFunction.builder()));
    ESBlocks.FLUID_STORAGE.values().forEach(
        block -> genBlockItemLootTableWithFunction(block.get(),
            StorageBlockLootFunction.builder()));
    dropSelf(ESBlocks.ADVANCED_EXPORTER.get());
    dropSelf(ESBlocks.ADVANCED_IMPORTER.get());
  }

  private void genBlockItemLootTableWithFunction(Block block, LootItemFunction.Builder builder) {
    add(block, LootTable.lootTable().withPool(
        LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1))
            .add(LootItem.lootTableItem(block)
                .apply(builder))
            .when(ExplosionCondition.survivesExplosion())));
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    List<Block> res = new ArrayList<>();
    res.addAll(ESBlocks.CRAFTER.values().stream().map(RegistryObject::get).toList());
    res.addAll(ESBlocks.ITEM_STORAGE.values().stream().map(RegistryObject::get).toList());
    res.addAll(
        ESBlocks.FLUID_STORAGE.values().stream().map(RegistryObject::get).toList());
    res.addAll(
        Arrays.asList(ESBlocks.ADVANCED_EXPORTER.get(), ESBlocks.ADVANCED_IMPORTER.get()));
    return res;
  }
}
