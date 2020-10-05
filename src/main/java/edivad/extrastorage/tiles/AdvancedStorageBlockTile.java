package edivad.extrastorage.tiles;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AdvancedStorageBlockTile extends NetworkNodeTile<AdvancedStorageNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedStorageBlockTile> PRIORITY = IPrioritizable.createParameter();
    public static final TileDataParameter<Integer, AdvancedStorageBlockTile> COMPARE = IComparable.createParameter();
    public static final TileDataParameter<Integer, AdvancedStorageBlockTile> WHITELIST_BLACKLIST = IWhitelistBlacklist.createParameter();
    public static final TileDataParameter<AccessType, AdvancedStorageBlockTile> ACCESS_TYPE = IAccessType.createParameter();
    public static final TileDataParameter<Long, AdvancedStorageBlockTile> STORED = new TileDataParameter<>(RSSerializers.LONG_SERIALIZER, 0L, t -> t.getNode().getStorage() != null ? (long) t.getNode().getStorage().getStored() : 0);

    private final ItemStorageType type;

    public AdvancedStorageBlockTile(ItemStorageType type)
    {
        super(Registration.ITEM_STORAGE_TILE.get(type).get());

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
    public AdvancedStorageNetworkNode createNode(World world, BlockPos pos)
    {
        return new AdvancedStorageNetworkNode(world, pos, type);
    }
}
