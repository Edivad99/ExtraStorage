package edivad.extrastorage.loottable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import edivad.extrastorage.tiles.AdvancedStorageBlockTile;
import edivad.extrastorage.tiles.node.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.tiles.node.AdvancedStorageNetworkNode;
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
        TileEntity tile = (TileEntity)lootContext.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof AdvancedStorageBlockTile)
        {
            AdvancedStorageNetworkNode removedNode = (AdvancedStorageNetworkNode)((AdvancedStorageBlockTile)tile).getRemovedNode();
            if (removedNode == null)
            {
                removedNode = (AdvancedStorageNetworkNode)((AdvancedStorageBlockTile)tile).getNode();
            }

            stack.getOrCreateTag().putUniqueId(AdvancedStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }
        else if (tile instanceof AdvancedFluidStorageBlockTile)
        {
            AdvancedFluidStorageNetworkNode removedNode = (AdvancedFluidStorageNetworkNode)((AdvancedFluidStorageBlockTile)tile).getRemovedNode();
            if (removedNode == null) {
                removedNode = (AdvancedFluidStorageNetworkNode)((AdvancedFluidStorageBlockTile)tile).getNode();
            }

            stack.getOrCreateTag().putUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }

        return stack;
    }

    public LootFunctionType getFunctionType() {
        return ESLootFunctions.STORAGE_BLOCK;
    }

    public static class Serializer extends LootFunction.Serializer<StorageBlockLootFunction>
    {
        @Override
        public StorageBlockLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditions) {
            return new StorageBlockLootFunction(conditions);
        }
    }
}
