package edivad.extrastorage.setup;

import com.mojang.serialization.Codec;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ESLootFunctions {

  public static LootItemFunctionType STORAGE_BLOCK;
  public static LootItemFunctionType CRAFTER;

  public static void register() {
    STORAGE_BLOCK =
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE,
            ExtraStorage.rl("storage_block"),
            new LootItemFunctionType(Codec.unit(new StorageBlockLootFunction())));
    CRAFTER =
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE,
            ExtraStorage.rl("crafter"),
            new LootItemFunctionType(Codec.unit(new AdvancedCrafterLootFunction())));
  }
}
