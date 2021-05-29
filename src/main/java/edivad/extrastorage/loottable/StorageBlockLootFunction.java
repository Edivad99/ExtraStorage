package edivad.extrastorage.loottable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import edivad.extrastorage.tiles.AdvancedStorageBlockTile;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.TileEntity;

public class StorageBlockLootFunction extends LootFunction
{
    protected StorageBlockLootFunction(ILootCondition[] conditions) {
        super(conditions);
    }

    @Override
    public ItemStack doApply(ItemStack stack, LootContext lootContext)
    {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof AdvancedStorageBlockTile)
        {
            AdvancedStorageNetworkNode removedNode = ((AdvancedStorageBlockTile)tile).getRemovedNode();
            if (removedNode == null)
            {
                removedNode = ((AdvancedStorageBlockTile)tile).getNode();
            }

            stack.getOrCreateTag().putUniqueId(AdvancedStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }
        else if (tile instanceof AdvancedFluidStorageBlockTile)
        {
            AdvancedFluidStorageNetworkNode removedNode = ((AdvancedFluidStorageBlockTile)tile).getRemovedNode();
            if (removedNode == null) {
                removedNode = ((AdvancedFluidStorageBlockTile)tile).getNode();
            }

            stack.getOrCreateTag().putUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }

        return stack;
    }

    public LootFunctionType getFunctionType() {
        return ESLootFunctions.getStorageBlock();
    }

    public static LootFunction.Builder<?> builder() {
        return builder(StorageBlockLootFunction::new);
    }

    public static class Serializer extends LootFunction.Serializer<StorageBlockLootFunction>
    {
        @Override
        public StorageBlockLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditions) {
            return new StorageBlockLootFunction(conditions);
        }
    }
}
