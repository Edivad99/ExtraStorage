package edivad.extrastorage.compat.jade;

import com.refinedmods.refinedstorage.blockentity.CrafterBlockEntity;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class CrafterProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
    if (blockAccessor.getBlockEntity() instanceof CrafterBlockEntity crafter) {
      var node = crafter.getNode();
      tag.putInt("patterns", node.getPatterns().size());
      tag.putInt("speed", node.getMaximumSuccessfulCraftingUpdates());
      tag.putInt("slots", 9);
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ExtraStorage.rl("crafter");
  }
}
