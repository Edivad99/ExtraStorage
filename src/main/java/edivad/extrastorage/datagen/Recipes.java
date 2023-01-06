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
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

        for(ItemStorageType type : ItemStorageType.values())
        {
            if(type.equals(ItemStorageType.TIER_5))
                partRecipe(Registration.ITEM_STORAGE_PART.get(type).get(), RSItems.ITEM_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType.SIXTY_FOUR_K).get(), consumer);
            else
                partRecipe(Registration.ITEM_STORAGE_PART.get(type).get(), TagGenerator.Items.PARTS_ITEM.get(ItemStorageType.values()[type.ordinal()-1]), consumer);

            diskRecipe(Registration.ITEM_DISK.get(type).get(), TagGenerator.Items.PARTS_ITEM.get(type), consumer);
            storageBlockRecipe(Registration.ITEM_STORAGE.get(type).get(), TagGenerator.Items.PARTS_ITEM.get(type), consumer);
        }
        for(FluidStorageType type : FluidStorageType.values())
        {
            if(type.equals(FluidStorageType.TIER_5))
                partRecipe(Registration.FLUID_STORAGE_PART.get(type).get(), RSItems.FLUID_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get(), consumer);
            else
                partRecipe(Registration.FLUID_STORAGE_PART.get(type).get(), TagGenerator.Items.PARTS_FLUID.get(FluidStorageType.values()[type.ordinal()-1]), consumer);

            diskRecipe(Registration.FLUID_DISK.get(type).get(), TagGenerator.Items.PARTS_FLUID.get(type), consumer);
            storageBlockRecipe(Registration.FLUID_STORAGE.get(type).get(), TagGenerator.Items.PARTS_FLUID.get(type), consumer);
        }

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.IRON).get())
                .pattern("aca")
                .pattern(" b ")
                .pattern("a a")
                .define('a', ItemTags.create(new ResourceLocation("forge", "ingots/iron")))
                .define('b', ItemTags.create(new ResourceLocation("refinedstorage","crafter")))
                .define('c', ItemTags.create(new ResourceLocation("forge","chests/wooden")))
                .unlockedBy("has_part", has(RSBlocks.CRAFTER.get(DyeColor.LIGHT_BLUE).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.GOLD).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', ItemTags.create(new ResourceLocation("forge", "storage_blocks/gold")))
                .define('b', Registration.CRAFTER.get(CrafterTier.IRON).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', ItemTags.create(new ResourceLocation("forge", "chests/wooden")))
                .unlockedBy("has_part", has(Registration.CRAFTER.get(CrafterTier.IRON).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.DIAMOND).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', ItemTags.create(new ResourceLocation("forge", "storage_blocks/diamond")))
                .define('b', Registration.CRAFTER.get(CrafterTier.GOLD).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', ItemTags.create(new ResourceLocation("forge", "chests/wooden")))
                .unlockedBy("has_part", has(Registration.CRAFTER.get(CrafterTier.GOLD).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.CRAFTER.get(CrafterTier.NETHERITE).get())
                .pattern("ada")
                .pattern("cbc")
                .pattern("a a")
                .define('a', ItemTags.create(new ResourceLocation("forge", "storage_blocks/netherite")))
                .define('b', Registration.CRAFTER.get(CrafterTier.DIAMOND).get())
                .define('c', Registration.NEURAL_PROCESSOR_ITEM.get())
                .define('d', ItemTags.create(new ResourceLocation("forge", "chests/wooden")))
                .unlockedBy("has_part", has(Registration.CRAFTER.get(CrafterTier.DIAMOND).get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.ADVANCED_EXPORTER.get())
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" a ")
                .define('a', net.minecraft.world.item.Items.REDSTONE_TORCH)
                .define('b', RSBlocks.EXPORTER.get())
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())
                .unlockedBy("has_part", has(RSBlocks.EXPORTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.ADVANCED_IMPORTER.get())
                .pattern(" a ")
                .pattern("cbc")
                .pattern(" a ")
                .define('a', Items.REDSTONE_TORCH)
                .define('b', RSBlocks.IMPORTER.get())
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())
                .unlockedBy("has_part", has(RSBlocks.IMPORTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Registration.RAW_NEURAL_PROCESSOR_ITEM.get())
                .pattern("cbd")
                .pattern("bab")
                .pattern("efe")
                .define('a', Items.CRAFTING_TABLE)
                .define('b', Items.QUARTZ)
                .define('c', RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_ADVANCED).get())
                .define('d', RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_IMPROVED).get())
                .define('e', ItemTags.create(new ResourceLocation("forge", "obsidian")))
                .define('f', RSItems.PROCESSOR_BINDING.get())
                .unlockedBy("has_part", has(RSItems.PROCESSORS.get(ProcessorItem.Type.RAW_ADVANCED).get()))
                .save(consumer);

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(Registration.RAW_NEURAL_PROCESSOR_ITEM.get()),
                Registration.NEURAL_PROCESSOR_ITEM.get(),
                1.25F, 200)
                .unlockedBy("has_part", has(Registration.RAW_NEURAL_PROCESSOR_ITEM.get())).save(consumer);
    }

    private void partRecipe(Item result, TagKey<Item> previousPart, Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(result)
                .pattern("DID")
                .pattern("GRG")
                .pattern("DGD")
                .define('G', previousPart)
                .define('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_previous_part", has(previousPart))
                .save(consumer, new ResourceLocation(Main.MODID, "part/" + result.getRegistryName().getPath()));
    }

    private void partRecipe(Item result, Item previousPart, Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(result)
                .pattern("DID")
                .pattern("GRG")
                .pattern("DGD")
                .define('G', previousPart)
                .define('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_previous_part", has(previousPart))
                .save(consumer, new ResourceLocation(Main.MODID, "part/" + result.getRegistryName().getPath()));
    }

    private void diskRecipe(Item result, TagKey<Item> part, Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(result)
                .pattern("GRG")
                .pattern("RSR")
                .pattern("III")
                .define('G', Tags.Items.GLASS)
                .define('S', part)
                .define('I', RSItems.QUARTZ_ENRICHED_IRON.get())
                .define('R', Items.REDSTONE)
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "disk/shaped/" + result.getRegistryName().getPath()));

        ShapelessRecipeBuilder.shapeless(result)
                .requires(RSItems.STORAGE_HOUSING.get())
                .requires(part)
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "disk/shapeless/" + result.getRegistryName().getPath()));
    }

    private void storageBlockRecipe(Item result, TagKey<Item> part, Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(result)
                .pattern("EPE")
                .pattern("EME")
                .pattern("ERE")
                .define('M', RSBlocks.MACHINE_CASING.get())
                .define('R', Items.REDSTONE)
                .define('P', part)
                .define('E', RSItems.QUARTZ_ENRICHED_IRON.get())
                .unlockedBy("has_part", has(part))
                .save(consumer, new ResourceLocation(Main.MODID, "storage_block/" + result.getRegistryName().getPath()));
    }
}
