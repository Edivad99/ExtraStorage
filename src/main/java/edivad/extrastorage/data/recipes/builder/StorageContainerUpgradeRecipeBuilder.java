package edivad.extrastorage.data.recipes.builder;

import org.jetbrains.annotations.Nullable;
import edivad.extrastorage.data.recipes.ESStorageContainerUpgradeRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class StorageContainerUpgradeRecipeBuilder implements RecipeBuilder {

  private final Item result;
  private final ItemStack resultStack;
  private Ingredient baseDisk;
  private Ingredient upgradePart;

  public StorageContainerUpgradeRecipeBuilder(ItemStack result) {
    this.result = result.getItem();
    this.resultStack = result;
  }

  public static StorageContainerUpgradeRecipeBuilder shapeless(ItemLike result) {
    return new StorageContainerUpgradeRecipeBuilder(result.asItem().getDefaultInstance());
  }

  public StorageContainerUpgradeRecipeBuilder addDisk(Ingredient baseDisk) {
    this.baseDisk = baseDisk;
    return this;
  }

  public StorageContainerUpgradeRecipeBuilder addPart(Ingredient upgradePart) {
    this.upgradePart = upgradePart;
    return this;
  }

  @Override
  public RecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
    return this;
  }

  @Override
  public RecipeBuilder group(@Nullable String s) {
    return this;
  }

  @Override
  public Item getResult() {
    return this.result;
  }

  @Override
  public void save(RecipeOutput recipeOutput) {
    var location = BuiltInRegistries.ITEM.getKey(this.getResult().asItem())
        .withPrefix("storage_upgrade/")
        .withSuffix("_upgrade");
    this.save(recipeOutput, location);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
    var recipe = new ESStorageContainerUpgradeRecipe(this.baseDisk, this.upgradePart,
        this.resultStack);
    recipeOutput.accept(resourceLocation, recipe, null);
  }
}
