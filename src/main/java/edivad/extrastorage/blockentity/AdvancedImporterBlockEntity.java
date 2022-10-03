package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedImporterBlockEntity extends NetworkNodeBlockEntity<AdvancedImporterNetworkNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> COMPARE = IComparable.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> TYPE = IType.createParameter();

    public static final BlockEntitySynchronizationParameter<CompoundTag, AdvancedImporterBlockEntity> COVER_MANAGER =
        new BlockEntitySynchronizationParameter<>(EntityDataSerializers.COMPOUND_TAG, new CompoundTag(),
            t -> t.getNode().getCoverManager().writeToNbt(),
            (t, v) -> t.getNode().getCoverManager().readFromNbt(v),
            (initial, p) -> {}
        );

    public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
        .addWatchedParameter(REDSTONE_MODE)
        .addWatchedParameter(COMPARE)
        .addWatchedParameter(WHITELIST_BLACKLIST)
        .addWatchedParameter(TYPE)
        .addWatchedParameter(COVER_MANAGER)
        .build();

    public AdvancedImporterBlockEntity(BlockPos pos, BlockState state)
    {
        super(Registration.ADVANCED_IMPORTER_TILE.get(), pos, state, SPEC);
    }

    @Override
    public AdvancedImporterNetworkNode createNode(Level level, BlockPos blockPos)
    {
        return new AdvancedImporterNetworkNode(level, blockPos);
    }
}
