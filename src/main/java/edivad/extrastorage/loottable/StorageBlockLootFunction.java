package edivad.extrastorage.loottable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class StorageBlockLootFunction extends LootItemConditionalFunction
{
    protected StorageBlockLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(StorageBlockLootFunction::new);
    }

    @Override
    public ItemStack run(ItemStack stack, LootContext lootContext)
    {
        BlockEntity blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof AdvancedStorageBlockEntity itemStorageBlockEntity)
        {
            AdvancedStorageNetworkNode removedNode = itemStorageBlockEntity.getRemovedNode();
            if (removedNode == null)
            {
                removedNode = itemStorageBlockEntity.getNode();
            }

            stack.getOrCreateTag().putUUID(AdvancedStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }
        else if (blockEntity instanceof AdvancedFluidStorageBlockEntity fluidStorageBlockEntity)
        {
            AdvancedFluidStorageNetworkNode removedNode = fluidStorageBlockEntity.getRemovedNode();
            if (removedNode == null) {
                removedNode = fluidStorageBlockEntity.getNode();
            }

            stack.getOrCreateTag().putUUID(AdvancedFluidStorageNetworkNode.NBT_ID, removedNode.getStorageId());
        }

        return stack;
    }

    public LootItemFunctionType getType() {
        return Registration.REGISTERED_LOOT_ITEM_FUNCTIONS.get("storage_block").get();
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<StorageBlockLootFunction>
    {
        @Override
        public StorageBlockLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new StorageBlockLootFunction(conditions);
        }
    }
}
