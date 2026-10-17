package edivad.extrastorage.compat.top;

import java.util.function.Function;
import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterBlockEntity;
import com.refinedmods.refinedstorage.common.content.Items;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlockEntity;
import edivad.extrastorage.tools.Translations;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TOPIntegration implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

  @Override
  public Void apply(ITheOneProbe probe) {
    probe.registerProvider(this);
    return null;
  }

  @Override
  public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level,
      BlockState blockState, IProbeHitData data) {
    var blockEntity = level.getBlockEntity(data.getPos());
    int patterns, speed, slots;

    if (blockEntity instanceof AdvancedAutocrafterBlockEntity advancedCrafter) {
      patterns = (int) advancedCrafter.getPatternContainer().getItems().stream()
          .filter(x -> !x.isEmpty())
          .count();
      speed = advancedCrafter.getSteps();
      slots = advancedCrafter.getTier().getSlots();
      probeInfo.horizontal().text(
          Component.translatable(Translations.OCCUPIED_SPACE, String.valueOf(patterns), String.valueOf(slots)));

      probeInfo.horizontal()
            .text(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
    } else if (blockEntity instanceof AutocrafterBlockEntity crafter) {
      patterns = (int) crafter.getPatternContainer().getItems().stream()
          .filter(x -> !x.isEmpty())
          .count();
      int speedUpgrades = crafter.getUpgrades().stream()
          .filter(x -> x.is(Items.INSTANCE.getSpeedUpgrade()))
          .mapToInt(ItemStack::getCount)
          .sum();
      // Mirrors AutocrafterBlockEntity#getSteps: 1 step + 1 per speed upgrade, capped at 5
      speed = Math.min(1 + speedUpgrades, 5);
      slots = crafter.getPatternContainer().getContainerSize();
      probeInfo.horizontal().text(
          Component.translatable(Translations.OCCUPIED_SPACE, String.valueOf(patterns),
              String.valueOf(slots)));
      probeInfo.horizontal()
          .text(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
    }
  }

  @Override
  public Identifier getID() {
    return ExtraStorage.rl("default");
  }
}
