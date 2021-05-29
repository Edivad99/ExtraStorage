package edivad.extrastorage.loottable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.TileEntity;


public class AdvancedCrafterLootFunction extends LootFunction {

    protected AdvancedCrafterLootFunction(ILootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext lootContext)
    {
        TileEntity tile = lootContext.get(LootParameters.BLOCK_ENTITY);

        AdvancedCrafterNetworkNode removedNode = ((AdvancedCrafterTile) tile).getRemovedNode();
        if (removedNode == null) {
            removedNode = ((AdvancedCrafterTile) tile).getNode();
        }

        if (removedNode.getDisplayName() != null) {
            stack.setDisplayName(removedNode.getDisplayName());
        }

        return stack;
    }

    public LootFunctionType getFunctionType() {
        return ESLootFunctions.getCrafter();
    }

    public static LootFunction.Builder<?> builder() {
        return builder(AdvancedCrafterLootFunction::new);
    }

    public static class Serializer extends LootFunction.Serializer<AdvancedCrafterLootFunction>
    {
        @Override
        public AdvancedCrafterLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditions) {
            return new AdvancedCrafterLootFunction(conditions);
        }
    }
}
