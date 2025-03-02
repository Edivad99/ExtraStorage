package edivad.extrastorage.setup;

import edivad.edivadlib.setup.UpdateChecker;
import edivad.extrastorage.ExtraStorage;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientSetup {

  public static void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ExtraStorage.ID));
  }
}
