package edivad.extrastorage.compat.jade;

import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterBlockEntity;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.tools.Translations;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CrafterComponent implements IBlockComponentProvider {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor,
      IPluginConfig iPluginConfig) {
    if (blockAccessor.getBlockEntity() instanceof AutocrafterBlockEntity) {
      var data = blockAccessor.getServerData();
      var patterns = data.getInt("patterns").orElse(0);
      var speed = data.getInt("speed").orElse(0);
      var slots = data.getInt("slots").orElse(0);

      tooltip.add(Component.translatable(Translations.OCCUPIED_SPACE,
          String.valueOf(patterns), String.valueOf(slots)));
      tooltip.add(Component.translatable(Translations.CURRENT_SPEED, String.valueOf(speed)));
    }
  }

  @Override
  public Identifier getUid() {
    return ExtraStorage.rl("crafter");
  }
}
