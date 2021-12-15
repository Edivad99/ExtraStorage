package edivad.extrastorage.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LootTableGenerator extends LootTableProvider {
    public LootTableGenerator(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
    {
        return ImmutableList.of(Pair.of(ESBlockLootTables::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext)
    {
    }

    @Override
    public String getName()
    {
        return "Extra Storage Loot Tables";
    }

    private static class ESBlockLootTables extends BlockLoot {

        @Override
        protected void addTables()
        {
            Registration.CRAFTER_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), AdvancedCrafterLootFunction.builder()));
            Registration.ITEM_STORAGE_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction.builder()));
            Registration.FLUID_STORAGE_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction.builder()));
            dropSelf(Registration.ADVANCED_EXPORTER.get());
            dropSelf(Registration.ADVANCED_IMPORTER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            List<Block> res = new ArrayList<>();
            res.addAll(Registration.CRAFTER_BLOCK.values().stream().map(RegistryObject::get).collect(Collectors.toList()));
            res.addAll(Registration.ITEM_STORAGE_BLOCK.values().stream().map(RegistryObject::get).collect(Collectors.toList()));
            res.addAll(Registration.FLUID_STORAGE_BLOCK.values().stream().map(RegistryObject::get).collect(Collectors.toList()));
            res.addAll(Arrays.asList(Registration.ADVANCED_EXPORTER.get(), Registration.ADVANCED_IMPORTER.get()));
            return res;
        }

        private void genBlockItemLootTableWithFunction(Block block, LootItemFunction.Builder builder) {
            add(block, LootTable.lootTable().withPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(block)
                                    .apply(builder))
                            .when(ExplosionCondition.survivesExplosion())));
        }
    }
}
