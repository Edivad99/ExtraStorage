package edivad.extrastorage.setup;

import edivad.extrastorage.Main;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ESLootFunctions
{
    public static LootFunctionType STORAGE_BLOCK;

    public static void register()
    {
        STORAGE_BLOCK = (LootFunctionType) Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Main.MODID, "storage_block"), new LootFunctionType(new StorageBlockLootFunction.Serializer()));
    }
}
