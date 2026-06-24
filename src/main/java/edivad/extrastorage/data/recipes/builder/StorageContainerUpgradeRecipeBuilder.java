package edivad.extrastorage.data.recipes.builder;

import org.jetbrains.annotations.Nullable;
import edivad.extrastorage.data.recipes.ESStorageContainerUpgradeRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class StorageContainerUpgradeRecipeBuilder implements RecipeBuilder {

  private final ItemStackTemplate result;
  private Ingredient baseDisk;
  private Ingredient upgradePart;

  public StorageContainerUpgradeRecipeBuilder(ItemStackTemplate result) {
    this.result = result;
  }

  public static StorageContainerUpgradeRecipeBuilder shapeless(ItemLike result) {
    return new StorageContainerUpgradeRecipeBuilder(new ItemStackTemplate(result.asItem()));
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
  public ResourceKey<Recipe<?>> defaultId() {
    var id = this.result.typeHolder().unwrapKey().orElseThrow().identifier()
        .withPrefix("storage_upgrade/")
        .withSuffix("_upgrade");
    return ResourceKey.create(Registries.RECIPE, id);
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
    var recipe = new ESStorageContainerUpgradeRecipe(this.baseDisk, this.upgradePart, this.result);
    recipeOutput.accept(resourceKey, recipe, null);
  }
}
