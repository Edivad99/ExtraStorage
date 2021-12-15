package edivad.extrastorage.loottable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class AdvancedCrafterLootFunction extends LootItemConditionalFunction {

    protected AdvancedCrafterLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(AdvancedCrafterLootFunction::new);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext lootContext)
    {
        BlockEntity blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);

        AdvancedCrafterNetworkNode removedNode = ((AdvancedCrafterBlockEntity) blockEntity).getRemovedNode();
        if (removedNode == null) {
            removedNode = ((AdvancedCrafterBlockEntity) blockEntity).getNode();
        }

        if (removedNode.getDisplayName() != null) {
            stack.setHoverName(removedNode.getDisplayName());
        }

        return stack;
    }

    public LootItemFunctionType getType() {
        return ESLootFunctions.getCrafter();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<AdvancedCrafterLootFunction>
    {
        @Override
        public AdvancedCrafterLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new AdvancedCrafterLootFunction(conditions);
        }
    }
}
