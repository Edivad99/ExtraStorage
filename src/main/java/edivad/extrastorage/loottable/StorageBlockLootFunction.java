package edivad.extrastorage.loottable;

import com.mojang.serialization.MapCodec;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class StorageBlockLootFunction implements LootItemFunction {
  public static final MapCodec<? extends LootItemFunction> FUNCTION_CODEC =
      MapCodec.unit(StorageBlockLootFunction::new);

  @Override
  public ItemStack apply(ItemStack stack, LootContext lootContext) {
    var blockEntity = lootContext.getParameter(LootContextParams.BLOCK_ENTITY);
    if (blockEntity instanceof StorageBlockEntity transferable) {
      RefinedStorageApi.INSTANCE.getStorageContainerItemHelper().transferFromBlockEntity(stack, transferable);
    }
    return stack;
  }

  @Override
  public MapCodec<? extends LootItemFunction> codec() {
    return FUNCTION_CODEC;
  }
}
