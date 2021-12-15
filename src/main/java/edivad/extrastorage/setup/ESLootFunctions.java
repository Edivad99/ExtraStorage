package edivad.extrastorage.setup;

import edivad.extrastorage.Main;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ESLootFunctions
{
    private static LootItemFunctionType storageBlock;
    private static LootItemFunctionType crafter;

    public static void register()
    {
        storageBlock = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Main.MODID, "storage_block"), new LootItemFunctionType(new StorageBlockLootFunction.Serializer()));
        crafter = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Main.MODID, "crafter"), new LootItemFunctionType(new AdvancedCrafterLootFunction.Serializer()));
    }

    public static LootItemFunctionType getStorageBlock()
    {
        return storageBlock;
    }

    public static LootItemFunctionType getCrafter()
    {
        return crafter;
    }
}
