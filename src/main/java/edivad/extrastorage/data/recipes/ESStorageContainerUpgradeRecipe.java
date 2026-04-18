package edivad.extrastorage.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;
import edivad.extrastorage.setup.ESRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ESStorageContainerUpgradeRecipe extends ShapelessRecipe {

  private final Ingredient baseDisk;
  private final Ingredient part;

  public ESStorageContainerUpgradeRecipe(Ingredient baseDisk, Ingredient part, ItemStack result) {
    super("", CraftingBookCategory.MISC, result, NonNullList.of(Ingredient.EMPTY, baseDisk, part));
    this.baseDisk = baseDisk;
    this.part = part;
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
    for (int i = 0; i < input.size(); ++i) {
      final ItemStack fromDisk = input.getItem(i);
      if (fromDisk.getItem() instanceof UpgradeableStorageContainer from) {
        final ItemStack toDisk = this.getResultItem(provider).copy();
        from.transferTo(fromDisk, toDisk);
        return toDisk;
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    for (int i = 0; i < input.size(); i++) {
      final ItemStack fromDisk = input.getItem(i);
      if (fromDisk.getItem() instanceof UpgradeableStorageContainer from) {
        Item storagePart = from.getVariant().getStoragePart();
        if (storagePart != null) {
          remainingItems.set(i, new ItemStack(storagePart));
        }
      }
    }
    return remainingItems;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ESRecipeSerializers.UPGRADE_RECIPE.get();
  }

  public static class Serializer implements RecipeSerializer<ESStorageContainerUpgradeRecipe> {

    private static final MapCodec<ESStorageContainerUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("base_disk").forGetter(recipe -> recipe.baseDisk),
            Ingredient.CODEC_NONEMPTY.fieldOf("storage_part").forGetter(recipe -> recipe.part),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        )
        .apply(instance, ESStorageContainerUpgradeRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ESStorageContainerUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
        (buffer, recipe) -> {
          Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.baseDisk);
          Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.part);
          ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }, buffer -> new ESStorageContainerUpgradeRecipe(
            Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
            Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
            ItemStack.STREAM_CODEC.decode(buffer)
        )
    );

    @Override
    public MapCodec<ESStorageContainerUpgradeRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ESStorageContainerUpgradeRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
