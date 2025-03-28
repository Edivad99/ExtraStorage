package edivad.extrastorage.network.to_server;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterContainerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AdvancedAutocrafterNameChangePacket(String name) implements CustomPacketPayload {
  public static final Type<AdvancedAutocrafterNameChangePacket> TYPE =
      new Type(ExtraStorage.rl("advanced_autocrafter_name_change"));

  public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedAutocrafterNameChangePacket> STREAM_CODEC =
      StreamCodec.composite(ByteBufCodecs.STRING_UTF8, AdvancedAutocrafterNameChangePacket::name,
          AdvancedAutocrafterNameChangePacket::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(AdvancedAutocrafterNameChangePacket packet, IPayloadContext ctx) {
    if (ctx.player().containerMenu instanceof AdvancedAutocrafterContainerMenu containerMenu) {
      containerMenu.changeName(packet.name);
    }
  }
}
