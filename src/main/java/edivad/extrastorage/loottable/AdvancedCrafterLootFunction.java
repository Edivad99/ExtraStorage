package edivad.extrastorage.loottable;

import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class AdvancedCrafterLootFunction implements LootItemFunction {

  @Override
  public ItemStack apply(ItemStack stack, LootContext lootContext) {
    var blockEntity = lootContext.getParamOrNull(LootContextParams.BLOCK_ENTITY);

    var removedNode = ((AdvancedCrafterBlockEntity) blockEntity).getRemovedNode();
    if (removedNode == null) {
      removedNode = ((AdvancedCrafterBlockEntity) blockEntity).getNode();
    }

    if (removedNode.getDisplayName() != null) {
      stack.setHoverName(removedNode.getDisplayName());
    }

    return stack;
  }

  public LootItemFunctionType getType() {
    return ESLootFunctions.CRAFTER;
  }
}
