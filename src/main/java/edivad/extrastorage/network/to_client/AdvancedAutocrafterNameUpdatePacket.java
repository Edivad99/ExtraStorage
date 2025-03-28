package edivad.extrastorage.network.to_client;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterContainerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AdvancedAutocrafterNameUpdatePacket(Component name) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<AdvancedAutocrafterNameUpdatePacket> TYPE =
      new Type(ExtraStorage.rl("advanced_autocrafter_name_update"));

  public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedAutocrafterNameUpdatePacket> STREAM_CODEC =
      StreamCodec.composite(ComponentSerialization.STREAM_CODEC, AdvancedAutocrafterNameUpdatePacket::name,
          AdvancedAutocrafterNameUpdatePacket::new);


  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(AdvancedAutocrafterNameUpdatePacket packet, IPayloadContext ctx) {
    if (ctx.player().containerMenu instanceof AdvancedAutocrafterContainerMenu containerMenu) {
      containerMenu.nameChanged(packet.name);
    }
  }
}
