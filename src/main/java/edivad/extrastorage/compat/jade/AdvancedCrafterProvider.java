package edivad.extrastorage.compat.jade;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class AdvancedCrafterProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
    if (blockAccessor.getBlockEntity() instanceof AdvancedAutocrafterBlockEntity advancedCrafter) {
      int patterns = (int) advancedCrafter.getPatternContainer().getItems().stream()
          .filter(x -> !x.isEmpty())
          .count();
      tag.putInt("patterns", patterns);
      //tag.putInt("speed", node.getMaximumSuccessfulCraftingUpdates());
      tag.putInt("slots", advancedCrafter.getTier().getSlots());
      tag.putInt("tier_speed", advancedCrafter.getSteps());
      //tag.putString("node_name", node.getName().getString());
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ExtraStorage.rl("advanced_crafter");
  }
}
