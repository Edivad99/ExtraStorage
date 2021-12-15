package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IComparable;
import com.refinedmods.refinedstorage.tile.config.IType;
import com.refinedmods.refinedstorage.tile.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedImporterBlockEntity extends NetworkNodeTile<AdvancedImporterNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedImporterBlockEntity> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedImporterBlockEntity> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<Integer, AdvancedImporterBlockEntity> TYPE = IType.createParameter();

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
