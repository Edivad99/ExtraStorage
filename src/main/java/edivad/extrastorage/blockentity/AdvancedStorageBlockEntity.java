package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.api.storage.AccessType;
import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IAccessType;
import com.refinedmods.refinedstorage.tile.config.IComparable;
import com.refinedmods.refinedstorage.tile.config.IPrioritizable;
import com.refinedmods.refinedstorage.tile.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.tile.data.RSSerializers;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedStorageBlockEntity extends NetworkNodeTile<AdvancedStorageNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedStorageBlockEntity> PRIORITY = IPrioritizable.createParameter();
    public static final TileDataParameter<Integer, AdvancedStorageBlockEntity> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedStorageBlockEntity> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<AccessType, AdvancedStorageBlockEntity> ACCESS_TYPE = IAccessType.createParameter();
    public static final TileDataParameter<Long, AdvancedStorageBlockEntity> STORED = new TileDataParameter<>(RSSerializers.LONG_SERIALIZER, 0L, t -> t.getNode().getStorage() != null ? (long) t.getNode().getStorage().getStored() : 0);

    private final ItemStorageType type;

    public AdvancedStorageBlockEntity(ItemStorageType type, BlockPos pos, BlockState state)
    {
        super(Registration.ITEM_STORAGE_TILE.get(type).get(), pos, state);

        this.type = type;

        dataManager.addWatchedParameter(PRIORITY);
        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(WHITELIST_BLACKLIST);
        dataManager.addWatchedParameter(STORED);
        dataManager.addWatchedParameter(ACCESS_TYPE);
    }

    public ItemStorageType getItemStorageType()
    {
        return type;
    }

    @Override
    public AdvancedStorageNetworkNode createNode(Level level, BlockPos pos)
    {
        return new AdvancedStorageNetworkNode(level, pos, type);
    }
}
