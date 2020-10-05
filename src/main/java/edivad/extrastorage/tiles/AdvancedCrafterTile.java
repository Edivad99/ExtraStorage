package edivad.extrastorage.tiles;

import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.dataparameter.AdvancedCrafterTileDataParameterClientListener;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancedCrafterTile extends NetworkNodeTile<AdvancedCrafterNetworkNode>
{
    public static final TileDataParameter<Integer, AdvancedCrafterTile> MODE = new TileDataParameter<>(DataSerializers.VARINT, AdvancedCrafterNetworkNode.CrafterMode.IGNORE.ordinal(), t -> t.getNode().getMode().ordinal(), (t, v) -> t.getNode().setMode(AdvancedCrafterNetworkNode.CrafterMode.getById(v)));
    private static final TileDataParameter<Boolean, AdvancedCrafterTile> HAS_ROOT = new TileDataParameter<>(DataSerializers.BOOLEAN, false, t -> t.getNode().getRootContainerNotSelf().isPresent(), null, (t, v) -> new AdvancedCrafterTileDataParameterClientListener().onChanged(t, v));

    private final LazyOptional<IItemHandler> patternsCapability = LazyOptional.of(() -> getNode().getPatternItems());
    private final CrafterTier tier;

    public AdvancedCrafterTile(CrafterTier tier)
    {
        super(Registration.CRAFTER_TILE.get(tier).get());

        this.tier = tier;

        dataManager.addWatchedParameter(MODE);
        dataManager.addParameter(HAS_ROOT);
    }

    @Override
    public AdvancedCrafterNetworkNode createNode(World world, BlockPos blockPos)
    {
        return new AdvancedCrafterNetworkNode(world, blockPos, tier);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction)
    {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if(direction != null && !direction.equals(this.getNode().getDirection()))
                return patternsCapability.cast();
        return super.getCapability(cap, direction);
    }

    public CrafterTier getTier()
    {
        return tier;
    }
}
