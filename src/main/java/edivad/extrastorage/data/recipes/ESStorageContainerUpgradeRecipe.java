package edivad.extrastorage.data.recipes;

import java.util.List;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class ESStorageContainerUpgradeRecipe extends ShapelessRecipe {

  public static final MapCodec<ESStorageContainerUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Ingredient.CODEC.fieldOf("base_disk").forGetter(recipe -> recipe.baseDisk),
          Ingredient.CODEC.fieldOf("storage_part").forGetter(recipe -> recipe.part),
          ItemStackTemplate.MAP_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
      ).apply(instance, ESStorageContainerUpgradeRecipe::new)
  );
  public static final StreamCodec<RegistryFriendlyByteBuf, ESStorageContainerUpgradeRecipe> STREAM_CODEC =
      StreamCodec.composite(
          Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.baseDisk,
          Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.part,
          ItemStackTemplate.STREAM_CODEC, recipe -> recipe.result,
          ESStorageContainerUpgradeRecipe::new
      );

  public static final RecipeSerializer<ESStorageContainerUpgradeRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
  private final Ingredient baseDisk;
  private final Ingredient part;

  public ESStorageContainerUpgradeRecipe(Ingredient baseDisk, Ingredient part, ItemStackTemplate result) {
    super(
        new CommonInfo(false),
        new CraftingBookInfo(CraftingBookCategory.MISC, ""),
        result,
        List.of(baseDisk, part)
    );
    this.baseDisk = baseDisk;
    this.part = part;
  }

  @Override
  public ItemStack assemble(final CraftingInput input) {
    for (int i = 0; i < input.size(); ++i) {
      final ItemStack fromDisk = input.getItem(i);
      if (fromDisk.getItem() instanceof UpgradeableStorageContainer upgrader) {
        final ItemStack toDisk = super.assemble(input);
        upgrader.transferTo(fromDisk, toDisk);
        return toDisk;
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final CraftingInput input) {
    final NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    for (int i = 0; i < input.size(); ++i) {
      final ItemStack stack = input.getItem(i);
      if (stack.getItem() instanceof UpgradeableStorageContainer upgrader) {
        final Item sourceStoragePart = upgrader.getVariant().getStoragePart();
        if (sourceStoragePart != null) {
          remainingItems.set(i, sourceStoragePart.getDefaultInstance());
        }
      }
    }
    return remainingItems;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public RecipeSerializer<ShapelessRecipe> getSerializer() {
    return (RecipeSerializer) SERIALIZER;
  }
}
