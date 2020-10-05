package edivad.extrastorage.tiles;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IComparable;
import com.refinedmods.refinedstorage.tile.config.IType;
import com.refinedmods.refinedstorage.tile.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedImporterTile extends NetworkNodeTile<AdvancedImporterNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedImporterTile> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedImporterTile> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<Integer, AdvancedImporterTile> TYPE = IType.createParameter();

    public AdvancedImporterTile()
    {
        super(Registration.ADVANCED_IMPORTER_TILE.get());

        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(WHITELIST_BLACKLIST);
        dataManager.addWatchedParameter(TYPE);
    }

    @Override
    public AdvancedImporterNetworkNode createNode(World world, BlockPos blockPos)
    {
        return new AdvancedImporterNetworkNode(world, blockPos);
    }
}
