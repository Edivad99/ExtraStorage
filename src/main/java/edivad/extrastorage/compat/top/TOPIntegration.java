package edivad.extrastorage.compat.top;

import java.util.function.Function;
import com.refinedmods.refinedstorage.blockentity.CrafterBlockEntity;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.tools.Translations;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

    if (blockEntity instanceof AdvancedCrafterBlockEntity advancedCrafter) {
      var node = advancedCrafter.getNode();
      patterns = node.getPatterns().size();
      speed = node.getMaximumSuccessfulCraftingUpdates();
      slots = advancedCrafter.getTier().getSlots();
      probeInfo.horizontal().text(
          Component.translatable(Translations.OCCUPIED_SPACE, String.valueOf(patterns),
              String.valueOf(slots)));

      if (node.getTierSpeed() != speed) {
        probeInfo.horizontal().text(
            Component.translatable(Translations.LIMITED_SPEED, node.getName().getString(),
                String.valueOf(speed)));
      } else {
        probeInfo.horizontal()
            .text(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
      }
    } else if (blockEntity instanceof CrafterBlockEntity crafter) {
      var node = crafter.getNode();
      patterns = node.getPatterns().size();
      speed = node.getMaximumSuccessfulCraftingUpdates();
      slots = 9;
      probeInfo.horizontal().text(
          Component.translatable(Translations.OCCUPIED_SPACE, String.valueOf(patterns),
              String.valueOf(slots)));
      probeInfo.horizontal()
          .text(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
    }
  }

  @Override
  public ResourceLocation getID() {
    return ExtraStorage.rl("default");
  }
}
