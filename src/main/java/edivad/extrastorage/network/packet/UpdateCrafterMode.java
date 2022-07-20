package edivad.extrastorage.network.packet;

import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateCrafterMode {

    private BlockPos pos;
    private int index;

    public UpdateCrafterMode(BlockPos pos, int index) {
        this.pos = pos;
        this.index = index;
    }

    public UpdateCrafterMode(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        index = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(index);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Level level = player.level;
            if(level.isLoaded(pos)) {
                BlockEntity crafterBlockEntity = level.getBlockEntity(pos);
                if(!(crafterBlockEntity instanceof AdvancedCrafterBlockEntity crafter)) {
                    Main.LOGGER.error("Wrong type of blockentity (expected AdvancedCrafterBlockEntity)!");
                    return;
                }
                crafter.setCrafterMode(AdvancedCrafterNetworkNode.CrafterMode.getById(index));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
