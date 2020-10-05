package edivad.extrastorage.tiles;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IComparable;
import com.refinedmods.refinedstorage.tile.config.IType;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedExporterTile extends NetworkNodeTile<AdvancedExporterNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedExporterTile> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedExporterTile> TYPE = IType.createParameter();

    public AdvancedExporterTile()
    {
        super(Registration.ADVANCED_EXPORTER_TILE.get());

        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(TYPE);
    }

    @Override
    public AdvancedExporterNetworkNode createNode(World world, BlockPos blockPos)
    {
        return new AdvancedExporterNetworkNode(world, blockPos);
    }
}
