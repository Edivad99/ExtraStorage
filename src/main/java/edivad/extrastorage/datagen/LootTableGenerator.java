package edivad.extrastorage.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import edivad.extrastorage.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return ImmutableList.of(Pair.of(ESBlockLootTables::new, LootParameterSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
    {
    }

    @Override
    public String getName()
    {
        return "Extra Storage Loot Tables";
    }

    private static class ESBlockLootTables extends BlockLootTables {

        @Override
        protected void addTables()
        {
            Registration.CRAFTER_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), AdvancedCrafterLootFunction.builder()));
            Registration.ITEM_STORAGE_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction.builder()));
            Registration.FLUID_STORAGE_BLOCK.values().forEach(block -> genBlockItemLootTableWithFunction(block.get(), StorageBlockLootFunction.builder()));
            registerDropSelfLootTable(Registration.ADVANCED_EXPORTER.get());
            registerDropSelfLootTable(Registration.ADVANCED_IMPORTER.get());
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

        private void genBlockItemLootTableWithFunction(Block block, ILootFunction.IBuilder builder) {
            registerLootTable(block, LootTable.builder().addLootPool(
                    LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(block)
                                    .acceptFunction(builder))
                            .acceptCondition(SurvivesExplosion.builder())));
        }
    }
}
