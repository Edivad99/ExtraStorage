package edivad.extrastorage.loottable;

import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class StorageBlockLootFunction implements LootItemFunction {

  @Override
  public ItemStack apply(ItemStack stack, LootContext lootContext) {
    var blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);
    if (blockEntity instanceof AdvancedStorageBlockEntity itemStorageBlockEntity) {
      var removedNode = itemStorageBlockEntity.getRemovedNode();
      if (removedNode == null) {
        removedNode = itemStorageBlockEntity.getNode();
      }

      stack.getOrCreateTag().putUUID(AdvancedStorageNetworkNode.NBT_ID, removedNode.getStorageId());
    } else if (blockEntity instanceof AdvancedFluidStorageBlockEntity fluidStorageBlockEntity) {
      var removedNode = fluidStorageBlockEntity.getRemovedNode();
      if (removedNode == null) {
        removedNode = fluidStorageBlockEntity.getNode();
      }

      stack.getOrCreateTag()
          .putUUID(AdvancedFluidStorageNetworkNode.NBT_ID, removedNode.getStorageId());
    }

    return stack;
  }

  public LootItemFunctionType getType() {
    return ESLootFunctions.STORAGE_BLOCK;
  }
}
