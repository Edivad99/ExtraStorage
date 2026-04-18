package edivad.extrastorage.data.recipes;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.misc.ProcessorItem;
import com.refinedmods.refinedstorage.common.storage.FluidStorageVariant;
import com.refinedmods.refinedstorage.common.storage.ItemStorageVariant;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.data.ExtraStorageTags;
import edivad.extrastorage.data.recipes.builder.StorageContainerUpgradeRecipeBuilder;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;

public class ExtraStorageRecipeProvider extends RecipeProvider {

  public ExtraStorageRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {

    for (var type : AdvancedItemStorageVariant.values()) {
      if (type.equals(AdvancedItemStorageVariant.TIER_5)) {
        partRecipe(ESItems.ITEM_STORAGE_PART.get(type),
            com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getItemStoragePart(ItemStorageVariant.SIXTY_FOUR_K),
            recipeOutput);
      } else {
        partRecipe(ESItems.ITEM_STORAGE_PART.get(type),
            ExtraStorageTags.Items.PARTS_ITEM.get(
                AdvancedItemStorageVariant.values()[type.ordinal() - 1]),
            recipeOutput);
      }
      diskRecipe(ESItems.ITEM_DISK.get(type), ExtraStorageTags.Items.PARTS_ITEM.get(type),
          recipeOutput);
      storageBlockRecipe(ESItems.ITEM_STORAGE.get(type),
          ExtraStorageTags.Items.PARTS_ITEM.get(type), recipeOutput);
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      if (type.equals(AdvancedFluidStorageVariant.TIER_5)) {
        partRecipe(ESItems.FLUID_STORAGE_PART.get(type),
            com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getFluidStoragePart(FluidStorageVariant.FOUR_THOUSAND_NINETY_SIX_B),
            recipeOutput);
      } else {
        partRecipe(ESItems.FLUID_STORAGE_PART.get(type),
            ExtraStorageTags.Items.PARTS_FLUID.get(
                AdvancedFluidStorageVariant.values()[type.ordinal() - 1]),
            recipeOutput);
      }
      diskRecipe(ESItems.FLUID_DISK.get(type), ExtraStorageTags.Items.PARTS_FLUID.get(type),
          recipeOutput);
      storageBlockRecipe(ESItems.FLUID_STORAGE.get(type),
          ExtraStorageTags.Items.PARTS_FLUID.get(type), recipeOutput);
    }

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
            ESItems.CRAFTER.get(CrafterTier.IRON).get())
        .pattern("aca")
        .pattern(" b ")
        .pattern("a a")
        .define('a', Tags.Items.INGOTS_IRON)
        .define('b', com.refinedmods.refinedstorage.common.content.Tags.AUTOCRAFTERS)
        .define('c', Tags.Items.CHESTS_WOODEN)
        .unlockedBy(getHasName(Blocks.INSTANCE.getAutocrafter().get(DyeColor.LIGHT_BLUE)),
            has(Blocks.INSTANCE.getAutocrafter().get(DyeColor.LIGHT_BLUE)))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
            ESItems.CRAFTER.get(CrafterTier.GOLD).get())
        .pattern("ada")
        .pattern("cbc")
        .pattern("a a")
        .define('a', Tags.Items.STORAGE_BLOCKS_GOLD)
        .define('b', ESItems.CRAFTER.get(CrafterTier.IRON).get())
        .define('c', ESItems.NEURAL_PROCESSOR.get())
        .define('d', Tags.Items.CHESTS_WOODEN)
        .unlockedBy(getHasName(ESItems.CRAFTER.get(CrafterTier.IRON).get()),
            has(ESItems.CRAFTER.get(CrafterTier.IRON).get()))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
            ESItems.CRAFTER.get(CrafterTier.DIAMOND).get())
        .pattern("ada")
        .pattern("cbc")
        .pattern("a a")
        .define('a', Tags.Items.STORAGE_BLOCKS_DIAMOND)
        .define('b', ESItems.CRAFTER.get(CrafterTier.GOLD).get())
        .define('c', ESItems.NEURAL_PROCESSOR.get())
        .define('d', Tags.Items.CHESTS_WOODEN)
        .unlockedBy(getHasName(ESItems.CRAFTER.get(CrafterTier.GOLD).get()),
            has(ESItems.CRAFTER.get(CrafterTier.GOLD).get()))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
            ESItems.CRAFTER.get(CrafterTier.NETHERITE).get())
        .pattern("ada")
        .pattern("cbc")
        .pattern("a a")
        .define('a', Tags.Items.STORAGE_BLOCKS_NETHERITE)
        .define('b', ESItems.CRAFTER.get(CrafterTier.DIAMOND).get())
        .define('c', ESItems.NEURAL_PROCESSOR.get())
        .define('d', Tags.Items.CHESTS_WOODEN)
        .unlockedBy(getHasName(ESItems.CRAFTER.get(CrafterTier.DIAMOND).get()),
            has(ESItems.CRAFTER.get(CrafterTier.DIAMOND).get()))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ESItems.ADVANCED_EXPORTER.get())
        .pattern(" a ")
        .pattern("cbc")
        .pattern(" a ")
        .define('a', net.minecraft.world.item.Items.REDSTONE_TORCH)
        .define('b', Blocks.INSTANCE.getExporter().getDefault())
        .define('c', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.IMPROVED))
        .unlockedBy(getHasName(Blocks.INSTANCE.getExporter().getDefault()),
            has(Blocks.INSTANCE.getExporter().getDefault()))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ESItems.ADVANCED_IMPORTER.get())
        .pattern(" a ")
        .pattern("cbc")
        .pattern(" a ")
        .define('a', Items.REDSTONE_TORCH)
        .define('b', Blocks.INSTANCE.getImporter().getDefault())
        .define('c', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.IMPROVED))
        .unlockedBy(getHasName(Blocks.INSTANCE.getImporter().getDefault()),
            has(Blocks.INSTANCE.getImporter().getDefault()))
        .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ESItems.RAW_NEURAL_PROCESSOR.get())
        .pattern("cbd")
        .pattern("bab")
        .pattern("efe")
        .define('a', Items.CRAFTING_TABLE)
        .define('b', Items.QUARTZ)
        .define('c', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.RAW_ADVANCED))
        .define('d', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.RAW_IMPROVED))
        .define('e', Tags.Items.OBSIDIANS)
        .define('f', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessorBinding())
        .unlockedBy(getHasName(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.RAW_ADVANCED)),
            has(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.RAW_ADVANCED)))
        .save(recipeOutput);

    SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(ESItems.RAW_NEURAL_PROCESSOR.get()),
            RecipeCategory.MISC,
            ESItems.NEURAL_PROCESSOR.get(),
            1.25F, 200)
        .unlockedBy("has_part", has(ESItems.RAW_NEURAL_PROCESSOR.get())).save(recipeOutput);

    this.registerItemStorageUpgrades(recipeOutput);
    this.registerFluidStorageUpgrades(recipeOutput);
  }

  private void partRecipe(DeferredItem<Item> result, TagKey<Item> previousPart,
      RecipeOutput consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .pattern("DID")
        .pattern("GRG")
        .pattern("DGD")
        .define('G', previousPart)
        .define('D', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.ADVANCED))
        .define('I', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getQuartzEnrichedIron())
        .define('R', Items.REDSTONE)
        .unlockedBy("has_previous_part", has(previousPart))
        .save(consumer, ExtraStorage.rl("part/" + result.getId().getPath()));
  }

  private void partRecipe(DeferredItem<Item> result, Item previousPart,
      RecipeOutput consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .pattern("DID")
        .pattern("GRG")
        .pattern("DGD")
        .define('G', previousPart)
        .define('D', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getProcessor(ProcessorItem.Type.ADVANCED))
        .define('I', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getQuartzEnrichedIron())
        .define('R', Items.REDSTONE)
        .unlockedBy("has_previous_part", has(previousPart))
        .save(consumer, ExtraStorage.rl("part/" + result.getId().getPath()));
  }

  private void diskRecipe(DeferredItem<Item> result, TagKey<Item> part,
      RecipeOutput consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .pattern("GRG")
        .pattern("RSR")
        .pattern("III")
        .define('G', Tags.Items.GLASS_BLOCKS)
        .define('S', part)
        .define('I', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getQuartzEnrichedIron())
        .define('R', Items.REDSTONE)
        .unlockedBy("has_part", has(part))
        .save(consumer, ExtraStorage.rl("disk/shaped/" + result.getId().getPath()));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get())
        .requires(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getStorageHousing())
        .requires(part)
        .unlockedBy("has_part", has(part))
        .save(consumer, ExtraStorage.rl("disk/shapeless/" + result.getId().getPath()));
  }

  private void storageBlockRecipe(DeferredItem<Item> result, TagKey<Item> part,
      RecipeOutput consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .pattern("EPE")
        .pattern("EME")
        .pattern("ERE")
        .define('M', Blocks.INSTANCE.getMachineCasing())
        .define('R', Items.REDSTONE)
        .define('P', part)
        .define('E', com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getQuartzEnrichedIron())
        .unlockedBy("has_part", has(part))
        .save(consumer, ExtraStorage.rl("storage_block/" + result.getId().getPath()));
  }

  private void registerItemStorageUpgrades(RecipeOutput recipeOutput) {
    Set<Ingredient.Value> disks = new HashSet<>();

    for (var value : ItemStorageVariant.values()) {
      if (value == ItemStorageVariant.CREATIVE) {
        continue;
      }
      disks.add(new Ingredient.ItemValue(
          com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getItemStorageDisk(value).getDefaultInstance()));
    }

    for (var value : AdvancedItemStorageVariant.values()) {
      var disk = ESItems.ITEM_DISK.get(value);
      StorageContainerUpgradeRecipeBuilder.shapeless(disk)
          .addDisk(Ingredient.fromValues(disks.stream()))
          .addPart(Ingredient.of(ESItems.ITEM_STORAGE_PART.get(value)))
          .save(recipeOutput);
      disks.add(new Ingredient.TagValue(ExtraStorageTags.Items.DISKS_ITEM.get(value)));
    }

    Set<ItemLike> storageBlocks = new HashSet<>();
    for (ItemStorageVariant value : ItemStorageVariant.values()) {
      if (value == ItemStorageVariant.CREATIVE) {
        continue;
      }
      storageBlocks.add(Blocks.INSTANCE.getItemStorageBlock(value));
    }

    for (var value : AdvancedItemStorageVariant.values()) {
      var storageBlock = ESBlocks.ITEM_STORAGE.get(value);
      StorageContainerUpgradeRecipeBuilder.shapeless(storageBlock)
          .addDisk(Ingredient.of(storageBlocks.toArray(new ItemLike[0])))
          .addPart(Ingredient.of(ESItems.ITEM_STORAGE_PART.get(value)))
          .save(recipeOutput);

      storageBlocks.add(storageBlock);
    }
  }

  private void registerFluidStorageUpgrades(RecipeOutput recipeOutput) {
    Set<ItemLike> disks = new HashSet<>();

    for (var value : FluidStorageVariant.values()) {
      if (value == FluidStorageVariant.CREATIVE) {
        continue;
      }
      disks.add(com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getFluidStorageDisk(value));
    }

    for (var value : AdvancedFluidStorageVariant.values()) {
      var disk = ESItems.FLUID_DISK.get(value);
      StorageContainerUpgradeRecipeBuilder.shapeless(disk)
          .addDisk(Ingredient.of(disks.toArray(new ItemLike[0])))
          .addPart(Ingredient.of(ESItems.FLUID_STORAGE_PART.get(value)))
          .save(recipeOutput);
      disks.add(disk);
    }

    Set<ItemLike> storageBlocks = new HashSet<>();
    for (var value : FluidStorageVariant.values()) {
      if (value == FluidStorageVariant.CREATIVE) {
        continue;
      }

      storageBlocks.add(Blocks.INSTANCE.getFluidStorageBlock(value));
    }

    for (var value : AdvancedFluidStorageVariant.values()) {
      var storageBlock = ESBlocks.FLUID_STORAGE.get(value);
      StorageContainerUpgradeRecipeBuilder.shapeless(storageBlock)
          .addDisk(Ingredient.of(storageBlocks.toArray(new ItemLike[0])))
          .addPart(Ingredient.of(ESItems.FLUID_STORAGE_PART.get(value)))
          .save(recipeOutput);

      storageBlocks.add(storageBlock);
    }
  }
}
