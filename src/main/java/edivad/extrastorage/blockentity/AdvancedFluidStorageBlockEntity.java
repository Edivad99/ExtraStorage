package edivad.extrastorage.blockentity;

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
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedFluidStorageBlockEntity extends NetworkNodeTile<AdvancedFluidStorageNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockEntity> PRIORITY = IPrioritizable.createParameter();
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockEntity> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedFluidStorageBlockEntity> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<AccessType, AdvancedFluidStorageBlockEntity> ACCESS_TYPE = IAccessType.createParameter();
    public static final TileDataParameter<Long, AdvancedFluidStorageBlockEntity> STORED = new TileDataParameter<>(RSSerializers.LONG_SERIALIZER, 0L, t -> t.getNode().getStorage() != null ? (long) t.getNode().getStorage().getStored() : 0);

    private final FluidStorageType type;

    public AdvancedFluidStorageBlockEntity(FluidStorageType type, BlockPos pos, BlockState state)
    {
        super(Registration.FLUID_STORAGE_TILE.get(type).get(), pos, state);

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
    public AdvancedFluidStorageNetworkNode createNode(Level level, BlockPos pos)
    {
        return new AdvancedFluidStorageNetworkNode(level, pos, type);
    }
}
