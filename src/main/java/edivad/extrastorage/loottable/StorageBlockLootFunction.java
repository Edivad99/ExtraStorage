package edivad.extrastorage.loottable;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockEntity;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class StorageBlockLootFunction implements LootItemFunction {

  @Override
  public ItemStack apply(ItemStack stack, LootContext lootContext) {
    var blockEntity = lootContext.getParam(LootContextParams.BLOCK_ENTITY);
    if (blockEntity instanceof StorageBlockEntity transferable) {
      RefinedStorageApi.INSTANCE.getStorageContainerItemHelper().transferFromBlockEntity(stack, transferable);
    }
    return stack;
  }

  public LootItemFunctionType getType() {
    return ESLootFunctions.STORAGE_BLOCK;
  }
}
