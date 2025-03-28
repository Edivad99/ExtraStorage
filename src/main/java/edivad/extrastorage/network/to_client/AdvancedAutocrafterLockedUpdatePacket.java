package edivad.extrastorage.network.to_client;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterContainerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AdvancedAutocrafterLockedUpdatePacket(boolean locked) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<AdvancedAutocrafterLockedUpdatePacket> TYPE =
      new Type(ExtraStorage.rl("advanced_autocrafter_locked_update"));

  public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedAutocrafterLockedUpdatePacket> STREAM_CODEC =
      StreamCodec.composite(ByteBufCodecs.BOOL, AdvancedAutocrafterLockedUpdatePacket::locked,
          AdvancedAutocrafterLockedUpdatePacket::new);


  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(AdvancedAutocrafterLockedUpdatePacket packet, IPayloadContext ctx) {
    if (ctx.player().containerMenu instanceof AdvancedAutocrafterContainerMenu containerMenu) {
      containerMenu.lockedChanged(packet.locked);
    }
  }
}
