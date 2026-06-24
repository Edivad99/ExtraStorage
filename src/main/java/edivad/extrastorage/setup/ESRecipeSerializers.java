package edivad.extrastorage.setup;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.data.recipes.ESStorageContainerUpgradeRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESRecipeSerializers {
  private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER =
      DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ExtraStorage.ID);

  //https://vscode.dev/github/ChaoticTrials/ExtraDisks?vscode-lang=it-it

//  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ESStorageContainerUpgradeRecipe>> UPGRADE_RECIPE =
//      RECIPE_SERIALIZER.register("upgrade_recipe", ESStorageContainerUpgradeRecipe.Serializer::new);

  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ESStorageContainerUpgradeRecipe>> UPGRADE_RECIPE =
      RECIPE_SERIALIZER.register("upgrade_recipe", () -> ESStorageContainerUpgradeRecipe.SERIALIZER);

  public static void register(IEventBus modEventBus) {
    RECIPE_SERIALIZER.register(
        "item_storage_disk_upgrade",
        () -> new RecipeSerializer<>(
            ESStorageContainerUpgradeRecipe.CODEC,
            ESStorageContainerUpgradeRecipe.STREAM_CODEC
        )
    );
//    RECIPE_SERIALIZER.register(
//        "item_storage_disk_upgrade",
//        () -> new StorageContainerUpgradeRecipeSerializer<>(
//            AdvancedItemStorageVariant.values(),
//            to -> new StorageContainerUpgradeRecipe<>(
//                AdvancedItemStorageVariant.values(), to, ESItems.ITEM_DISK::get
//            )
//        )
//    );
//    RECIPE_SERIALIZER.register(
//        "fluid_storage_disk_upgrade",
//        () -> new StorageContainerUpgradeRecipeSerializer<>(
//            AdvancedFluidStorageVariant.values(),
//            to -> new StorageContainerUpgradeRecipe<>(
//                AdvancedFluidStorageVariant.values(), to, ESItems.FLUID_DISK::get
//            )
//        )
//    );
//
//    RECIPE_SERIALIZER.register(
//        "item_storage_block_upgrade",
//        () -> new StorageContainerUpgradeRecipeSerializer<>(
//            AdvancedItemStorageVariant.values(),
//            to -> new StorageContainerUpgradeRecipe<>(
//                AdvancedItemStorageVariant.values(), to, ESItems.ITEM_STORAGE::get
//            )
//        )
//    );
//    RECIPE_SERIALIZER.register(
//        "fluid_storage_block_upgrade",
//        () -> new StorageContainerUpgradeRecipeSerializer<>(
//            AdvancedFluidStorageVariant.values(),
//            to -> new StorageContainerUpgradeRecipe<>(
//                AdvancedFluidStorageVariant.values(), to, ESItems.FLUID_STORAGE::get
//            )
//        )
//    );

    RECIPE_SERIALIZER.register(modEventBus);
  }
}
