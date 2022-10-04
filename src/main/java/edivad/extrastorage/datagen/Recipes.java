package edivad.extrastorage.datagen;

import com.refinedmods.refinedstorage.RSBlocks;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.item.ProcessorItem;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        for (var type : ItemStorageType.values()) {
            if (type.equals(ItemStorageType.TIER_5)) {
                partRecipe(Registration.ITEM_STORAGE_PART.get(type), RSItems.ITEM_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType.SIXTY_FOUR_K).get(), consumer);
            } else {
                partRecipe(Registration.ITEM_STORAGE_PART.get(type), TagGenerator.Items.PARTS_ITEM.get(ItemStorageType.values()[type.ordinal() - 1]), consumer);
            }
            diskRecipe(Registration.ITEM_DISK.get(type), TagGenerator.Items.PARTS_ITEM.get(type), consumer);
            storageBlockRecipe(Registration.ITEM_STORAGE.get(type), TagGenerator.Items.PARTS_ITEM.get(type), consumer);
        }
        for (var type : FluidStorageType.values()) {
            if (type.equals(FluidStorageType.TIER_5)) {
                partRecipe(Registration.FLUID_STORAGE_PART.get(type), RSItems.FLUID_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get(), consumer);
            } else {
                partRecipe(Registration.FLUID_STORAGE_PART.get(type), TagGenerator.Items.PARTS_FLUID.get(FluidStorageType.values()[type.ordinal() - 1]), consumer);
            }
            diskRecipe(Registration.FLUID_DISK.get(type), TagGenerator.Items.PARTS_FLUID.get(type), consumer);
            storageBlockRecipe(Registration.FLUID_STORAGE.get(type), TagGenerator.Items.PARTS_FLUID.get(type), consumer);
        }

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.IRON).get())
                .pattern("aca")
                .pattern(" b ")
                .pattern("a a")
                .define('a', Tags.Items.INGOTS_IRON)
                .define('b', ItemTags.create(new ResourceLocation("refinedstorage", "crafter")))
                .define('c', Tags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(RSBlocks.CRAFTER.get(DyeColor.LIGHT_BLUE).get()),
                        has(RSBlocks.CRAFTER.get(DyeColor.LIGHT_BLUE).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.GOLD).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('b', Registration.CRAFTER.get(CrafterTier.IRON).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', Tags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(Registration.CRAFTER.get(CrafterTier.IRON).get()),
                        has(Registration.CRAFTER.get(CrafterTier.IRON).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.DIAMOND).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('b', Registration.CRAFTER.get(CrafterTier.GOLD).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', Tags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(Registration.CRAFTER.get(CrafterTier.GOLD).get()),
                        has(Registration.CRAFTER.get(CrafterTier.GOLD).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.NETHERITE).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .define('b', Registration.CRAFTER.get(CrafterTier.DIAMOND).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', Tags.Items.CHESTS_WOODEN)
                .unlockedBy(getHasName(Registration.CRAFTER.get(CrafterTier.DIAMOND).get()),
                        has(Registration.CRAFTER.get(CrafterTier.DIAMOND).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.ADVANCED_EXPORTER.get())
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" a ")
                .define('a', net.minecraft.world.item.Items.REDSTONE_TORCH)
                .define('b', RSBlocks.EXPORTER.get())
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())
                .unlockedBy(getHasName(RSBlocks.EXPORTER.get()),
                        has(RSBlocks.EXPORTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.ADVANCED_IMPORTER.get())
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" a ")
                .define('a', Items.REDSTONE_TORCH)
                .define('b', RSBlocks.IMPORTER.get())
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())
                .unlockedBy(getHasName(RSBlocks.IMPORTER.get()),
                        has(RSBlocks.IMPORTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.RAW_NEURAL_PROCESSOR_ITEM.get())
                .pattern("cbd")
                .pattern("bab")
                .pattern("efe")
                .define('a', Items.CRAFTING_TABLE)
                .define('b', Items.QUARTZ)
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_ADVANCED).get())
                .define('d', RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_IMPROVED).get())
                .define('e', Tags.Items.OBSIDIAN)
                .define('f', RSItems.PROCESSOR_BINDING.get())
                .unlockedBy(getHasName(RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_ADVANCED).get()),
                        has(RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_ADVANCED).get()))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(Registration.RAW_NEURAL_PROCESSOR_ITEM.get()),
                        Registration.NEURAL_PROCESSOR_ITEM.get(),
                        1.25F, 200)
                .unlockedBy("has_part", has(Registration.RAW_NEURAL_PROCESSOR_ITEM.get())).save(consumer);
    }

    private void partRecipe(RegistryObject<Item> result, TagKey<Item> previousPart, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
                .pattern("DID")
                .pattern("GRG")
                .pattern("DGD")
                .define('G', previousPart)
                .define('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_previous_part", has(previousPart))
                .save(consumer, new ResourceLocation(Main.MODID, "part/" + result.getId().getPath()));
    }

    private void partRecipe(RegistryObject<Item> result, Item previousPart, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
                .pattern("DID")
                .pattern("GRG")
                .pattern("DGD")
                .define('G', previousPart)
                .define('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_previous_part", has(previousPart))
                .save(consumer, new ResourceLocation(Main.MODID, "part/" + result.getId().getPath()));
    }

    private void diskRecipe(RegistryObject<Item> result, TagKey<Item> part, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
                .pattern("GRG")
                .pattern("RSR")
                .pattern("III")
                .define('G', Tags.Items.GLASS)
                .define('S', part)
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "disk/shaped/" + result.getId().getPath()));

        ShapelessRecipeBuilder.shapeless(result.get())
                .requires(RSItems.STORAGE_HOUSING.get())
                .requires(part)
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "disk/shapeless/" + result.getId().getPath()));
    }

    private void storageBlockRecipe(RegistryObject<Item> result, TagKey<Item> part, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
                .pattern("EPE")
                .pattern("EME")
                .pattern("ERE")
                .define('M', RSBlocks.MACHINE_CASING.get())
                .define('R', Items.REDSTONE)
                .define('P', part)
                .define('E', RSItems.QUARTZ_ENRICHED_IRON.get())
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "storage_block/" + result.getId().getPath()));
    }
}
