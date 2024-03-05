package edivad.extrastorage.compat.jade;

import com.refinedmods.refinedstorage.blockentity.CrafterBlockEntity;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.tools.Translations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CrafterComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor,
      IPluginConfig iPluginConfig) {
    if (blockAccessor.getBlockEntity() instanceof CrafterBlockEntity) {
      var data = blockAccessor.getServerData();
      var patterns = data.getInt("patterns");
      var speed = data.getInt("speed");
      var slots = data.getInt("slots");

      tooltip.add(Component.translatable(Translations.OCCUPIED_SPACE,
          String.valueOf(patterns), String.valueOf(slots)));
      tooltip.add(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ExtraStorage.rl("crafter");
  }
}
