package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.dataparameter.AdvancedCrafterTileDataParameterClientListener;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancedCrafterBlockEntity extends NetworkNodeBlockEntity<AdvancedCrafterNetworkNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedCrafterBlockEntity> MODE = new BlockEntitySynchronizationParameter<>(EntityDataSerializers.INT, AdvancedCrafterNetworkNode.CrafterMode.IGNORE.ordinal(), t -> t.getNode().getMode().ordinal(), (t, v) -> t.getNode().setMode(AdvancedCrafterNetworkNode.CrafterMode.getById(v)));
    private static final BlockEntitySynchronizationParameter<Boolean, AdvancedCrafterBlockEntity> HAS_ROOT = new BlockEntitySynchronizationParameter<>(EntityDataSerializers.BOOLEAN, false, t -> t.getNode().getRootContainerNotSelf().isPresent(), null, (t, v) -> new AdvancedCrafterTileDataParameterClientListener().onChanged(t, v));

    private final LazyOptional<IItemHandler> patternsCapability = LazyOptional.of(() -> getNode().getPatternItems());
    private final CrafterTier tier;

    public AdvancedCrafterBlockEntity(CrafterTier tier, BlockPos pos, BlockState state)
    {
        super(Registration.CRAFTER_TILE.get(tier).get(), pos, state);

        this.tier = tier;

        dataManager.addWatchedParameter(MODE);
        dataManager.addParameter(HAS_ROOT);
    }

    @Override
    public AdvancedCrafterNetworkNode createNode(Level level, BlockPos pos)
    {
        return new AdvancedCrafterNetworkNode(level, pos, tier);
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
