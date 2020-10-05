package edivad.extrastorage.tiles;

import com.refinedmods.refinedstorage.api.storage.AccessType;
import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IAccessType;
import com.refinedmods.refinedstorage.tile.config.IComparable;
import com.refinedmods.refinedstorage.tile.config.IPrioritizable;
import com.refinedmods.refinedstorage.tile.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.tile.data.RSSerializers;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedFluidStorageBlockTile extends NetworkNodeTile<AdvancedFluidStorageNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockTile> PRIORITY = IPrioritizable.createParameter();
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockTile> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockTile> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<AccessType, AdvancedFluidStorageBlockTile> ACCESS_TYPE = IAccessType.createParameter();
    public static final TileDataParameter<Long, AdvancedFluidStorageBlockTile> STORED = new TileDataParameter<>(RSSerializers.LONG_SERIALIZER, 0L, t -> t.getNode().getStorage() != null ? (long) t.getNode().getStorage().getStored() : 0);

    private final FluidStorageType type;

    public AdvancedFluidStorageBlockTile(FluidStorageType type)
    {
        super(Registration.FLUID_STORAGE_TILE.get(type).get());

        this.type = type;

        dataManager.addWatchedParameter(PRIORITY);
        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(WHITELIST_BLACKLIST);
        dataManager.addWatchedParameter(STORED);
        dataManager.addWatchedParameter(ACCESS_TYPE);
    }

    public FluidStorageType getFluidStorageType()
    {
        return type;
    }

    @Override
    public AdvancedFluidStorageNetworkNode createNode(World world, BlockPos pos)
    {
        return new AdvancedFluidStorageNetworkNode(world, pos, type);
    }
}
