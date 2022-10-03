package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedExporterBlockEntity extends NetworkNodeBlockEntity<AdvancedExporterNetworkNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> COMPARE = IComparable.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> TYPE = IType.createParameter();

    public static final BlockEntitySynchronizationParameter<CompoundTag, AdvancedExporterBlockEntity> COVER_MANAGER =
        new BlockEntitySynchronizationParameter<>(EntityDataSerializers.COMPOUND_TAG, new CompoundTag(),
        t -> t.getNode().getCoverManager().writeToNbt(),
        (t, v) -> t.getNode().getCoverManager().readFromNbt(v),
        (initial, p) -> {
        });

    public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
        .addWatchedParameter(REDSTONE_MODE)
        .addWatchedParameter(COMPARE)
        .addWatchedParameter(TYPE)
        .addWatchedParameter(COVER_MANAGER)
        .build();

    public AdvancedExporterBlockEntity(BlockPos pos, BlockState state)
    {
        super(Registration.ADVANCED_EXPORTER_TILE.get(), pos, state, SPEC);
    }

    @Override
    public AdvancedExporterNetworkNode createNode(Level level, BlockPos pos)
    {
        return new AdvancedExporterNetworkNode(level, pos);
    }
}
