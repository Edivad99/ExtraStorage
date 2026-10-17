package edivad.extrastorage.compat.jade;

import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterBlockEntity;
import com.refinedmods.refinedstorage.common.content.Items;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public class CrafterProvider implements IServerDataProvider<BlockAccessor> {

  @Override
  public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
    if (blockAccessor.getBlockEntity() instanceof AutocrafterBlockEntity crafter) {
      int patterns = (int) crafter.getPatternContainer().getItems().stream()
          .filter(x -> !x.isEmpty())
          .count();
      int speedUpgrades = crafter.getUpgrades().stream()
          .filter(x -> x.is(Items.INSTANCE.getSpeedUpgrade()))
          .mapToInt(ItemStack::getCount)
          .sum();
      tag.putInt("patterns", patterns);
      // Mirrors AutocrafterBlockEntity#getSteps: 1 step + 1 per speed upgrade, capped at 5
      tag.putInt("speed", Math.min(1 + speedUpgrades, 5));
      tag.putInt("slots", crafter.getPatternContainer().getContainerSize());
    }
  }

  @Override
  public Identifier getUid() {
    return ExtraStorage.rl("crafter");
  }
}
