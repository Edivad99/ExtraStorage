package edivad.extrastorage.compat.jade;

import com.refinedmods.refinedstorage.block.CrafterBlock;
import com.refinedmods.refinedstorage.blockentity.CrafterBlockEntity;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blocks.AdvancedCrafterBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(ExtraStorage.ID)
public class JadeCompatibility implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(new AdvancedCrafterProvider(), AdvancedCrafterBlockEntity.class);
    registration.registerBlockDataProvider(new CrafterProvider(), CrafterBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new AdvancedCrafterComponent(), AdvancedCrafterBlock.class);
    registration.registerBlockComponent(new CrafterComponent(), CrafterBlock.class);
  }
}
