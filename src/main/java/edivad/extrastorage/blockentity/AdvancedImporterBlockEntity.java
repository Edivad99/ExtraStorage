package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedImporterBlockEntity extends NetworkNodeBlockEntity<AdvancedImporterNetworkNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> COMPARE = IComparable.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> TYPE = IType.createParameter();

    public AdvancedImporterBlockEntity(BlockPos pos, BlockState state)
    {
        super(Registration.ADVANCED_IMPORTER_TILE.get(), pos, state);

        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(WHITELIST_BLACKLIST);
        dataManager.addWatchedParameter(TYPE);
    }

    @Override
    public AdvancedImporterNetworkNode createNode(Level level, BlockPos blockPos)
    {
        return new AdvancedImporterNetworkNode(level, blockPos);
    }
}
