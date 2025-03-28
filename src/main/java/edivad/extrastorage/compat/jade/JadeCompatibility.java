package edivad.extrastorage.compat.jade;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlock;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(ExtraStorage.ID)
public class JadeCompatibility implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(new AdvancedCrafterProvider(), AdvancedAutocrafterBlockEntity.class);
    //registration.registerBlockDataProvider(new CrafterProvider(), AutocrafterBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new AdvancedCrafterComponent(), AdvancedAutocrafterBlock.class);
    //registration.registerBlockComponent(new CrafterComponent(), AutocrafterBlock.class);
  }
}
