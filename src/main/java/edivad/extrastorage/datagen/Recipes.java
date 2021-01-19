package edivad.extrastorage.datagen;

import com.refinedmods.refinedstorage.RSBlocks;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.item.ProcessorItem;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
    public Recipes(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for(ItemStorageType type : ItemStorageType.values())
        {
            if(type.equals(ItemStorageType.TIER_5))
                partRecipe(Registration.ITEM_STORAGE_PART.get(type).get(), RSItems.ITEM_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType.SIXTY_FOUR_K).get(), consumer);
            else
                partRecipe(Registration.ITEM_STORAGE_PART.get(type).get(), Tag.Items.PARTS_ITEM.get(ItemStorageType.values()[type.ordinal()-1]), consumer);

            diskRecipe(Registration.ITEM_DISK.get(type).get(), Tag.Items.PARTS_ITEM.get(type), consumer);
            storageBlockRecipe(Registration.ITEM_STORAGE.get(type).get(), Tag.Items.PARTS_ITEM.get(type), consumer);
        }
        for(FluidStorageType type : FluidStorageType.values())
        {
            if(type.equals(FluidStorageType.TIER_5))
                partRecipe(Registration.FLUID_STORAGE_PART.get(type).get(), RSItems.FLUID_STORAGE_PARTS.get(com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get(), consumer);
            else
                partRecipe(Registration.FLUID_STORAGE_PART.get(type).get(), Tag.Items.PARTS_FLUID.get(FluidStorageType.values()[type.ordinal()-1]), consumer);

            diskRecipe(Registration.FLUID_DISK.get(type).get(), Tag.Items.PARTS_FLUID.get(type), consumer);
            storageBlockRecipe(Registration.FLUID_STORAGE.get(type).get(), Tag.Items.PARTS_FLUID.get(type), consumer);
        }

        ShapedRecipeBuilder.shapedRecipe(Registration.CRAFTER.get(CrafterTier.IRON).get())//
                .patternLine("a a")//
                .patternLine("cbc")//
                .patternLine("a a")//
                .key('a', Items.IRON_INGOT)//
                .key('b', RSBlocks.CRAFTER.get(DyeColor.LIGHT_BLUE).get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())//
                .addCriterion("has_part", hasItem(RSBlocks.CRAFTER.get(DyeColor.LIGHT_BLUE).get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.CRAFTER.get(CrafterTier.GOLD).get())//
                .patternLine("a a")//
                .patternLine("cbc")//
                .patternLine("a a")//
                .key('a', Blocks.GOLD_BLOCK)//
                .key('b', Registration.CRAFTER.get(CrafterTier.IRON).get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())//
                .addCriterion("has_part", hasItem(Registration.CRAFTER.get(CrafterTier.IRON).get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.CRAFTER.get(CrafterTier.DIAMOND).get())//
                .patternLine("a a")//
                .patternLine("cbc")//
                .patternLine("a a")//
                .key('a', Blocks.DIAMOND_BLOCK)//
                .key('b', Registration.CRAFTER.get(CrafterTier.GOLD).get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())//
                .addCriterion("has_part", hasItem(Registration.CRAFTER.get(CrafterTier.GOLD).get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.CRAFTER.get(CrafterTier.NETHERITE).get())//
                .patternLine("a a")//
                .patternLine("cbc")//
                .patternLine("a a")//
                .key('a', Blocks.NETHERITE_BLOCK)//
                .key('b', Registration.CRAFTER.get(CrafterTier.DIAMOND).get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())//
                .addCriterion("has_part", hasItem(Registration.CRAFTER.get(CrafterTier.DIAMOND).get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.ADVANCED_EXPORTER.get())//
                .patternLine(" a ")//
                .patternLine("cbc")//
                .patternLine(" a ")//
                .key('a', Items.REDSTONE_TORCH)//
                .key('b', RSBlocks.EXPORTER.get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())//
                .addCriterion("has_part", hasItem(RSBlocks.EXPORTER.get()))//
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.ADVANCED_IMPORTER.get())//
                .patternLine(" a ")//
                .patternLine("cbc")//
                .patternLine(" a ")//
                .key('a', Items.REDSTONE_TORCH)//
                .key('b', RSBlocks.IMPORTER.get())//
                .key('c', RSItems.PROCESSORS.get(ProcessorItem.Type.IMPROVED).get())//
                .addCriterion("has_part", hasItem(RSBlocks.IMPORTER.get()))//
                .build(consumer);
    }

    private void partRecipe(Item result, ITag.INamedTag<Item> previousPart, Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(result)//
                .patternLine("DID")//
                .patternLine("GRG")//
                .patternLine("DGD")//
                .key('G', previousPart)//
                .key('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())//
                .key('I', RSItems.QUARTZ_ENRICHED_IRON.get())//
                .key('R', Items.REDSTONE)//
                .addCriterion("has_previous_part", hasItem(previousPart))//
                .build(consumer, new ResourceLocation(Main.MODID, "part/" + result.getRegistryName().getPath()));
    }

    private void partRecipe(Item result, Item previousPart, Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(result)//
                .patternLine("DID")//
                .patternLine("GRG")//
                .patternLine("DGD")//
                .key('G', previousPart)//
                .key('D', RSItems.PROCESSORS.get(ProcessorItem.Type.ADVANCED).get())//
                .key('I', RSItems.QUARTZ_ENRICHED_IRON.get())//
                .key('R', Items.REDSTONE)//
                .addCriterion("has_previous_part", hasItem(previousPart))//
                .build(consumer, new ResourceLocation(Main.MODID, "part/" + result.getRegistryName().getPath()));
    }

    private void diskRecipe(Item result, ITag.INamedTag<Item> part, Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(result)//
                .patternLine("GRG")//
                .patternLine("RSR")//
                .patternLine("III")//
                .key('G', Tags.Items.GLASS)//
                .key('S', part)//
                .key('I', RSItems.QUARTZ_ENRICHED_IRON.get())//
                .key('R', Items.REDSTONE)//
                .addCriterion("has_part", hasItem(part))//
                .build(consumer, new ResourceLocation(Main.MODID, "disk/shaped/" + result.getRegistryName().getPath()));

        ShapelessRecipeBuilder.shapelessRecipe(result)//
                .addIngredient(RSItems.STORAGE_HOUSING.get())//
                .addIngredient(part)//
                .addCriterion("has_part", hasItem(part))//
                .build(consumer, new ResourceLocation(Main.MODID, "disk/shapeless/" + result.getRegistryName().getPath()));
    }

    private void storageBlockRecipe(Item result, ITag.INamedTag<Item> part, Consumer<IFinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shapedRecipe(result)//
                .patternLine("EPE")//
                .patternLine("EME")//
                .patternLine("ERE")//
                .key('M', RSBlocks.MACHINE_CASING.get())//
                .key('R', Items.REDSTONE)//
                .key('P', part)//
                .key('E', RSItems.QUARTZ_ENRICHED_IRON.get())//
                .addCriterion("has_part", hasItem(part))//
                .build(consumer, new ResourceLocation(Main.MODID, "storage_block/" + result.getRegistryName().getPath()));
    }
}
