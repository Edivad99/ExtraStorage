package edivad.extrastorage.network;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.network.to_client.AdvancedAutocrafterLockedUpdatePacket;
import edivad.extrastorage.network.to_client.AdvancedAutocrafterNameUpdatePacket;
import edivad.extrastorage.network.to_server.AdvancedAutocrafterNameChangePacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

  private PacketHandler() {
  }

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
      var registrar = event.registrar(ExtraStorage.ID).versioned("1");
      registerClientToServer(registrar);
      registerServerToClient(registrar);
    });
  }

  private static void registerClientToServer(PayloadRegistrar registrar) {
    registrar.playToServer(AdvancedAutocrafterNameChangePacket.TYPE,
        AdvancedAutocrafterNameChangePacket.STREAM_CODEC, AdvancedAutocrafterNameChangePacket::handle);
  }

  private static void registerServerToClient(PayloadRegistrar registrar) {
    registrar.playToClient(AdvancedAutocrafterLockedUpdatePacket.TYPE,
        AdvancedAutocrafterLockedUpdatePacket.STREAM_CODEC, AdvancedAutocrafterLockedUpdatePacket::handle);
    registrar.playToClient(AdvancedAutocrafterNameUpdatePacket.TYPE,
        AdvancedAutocrafterNameUpdatePacket.STREAM_CODEC, AdvancedAutocrafterNameUpdatePacket::handle);
  }
}
