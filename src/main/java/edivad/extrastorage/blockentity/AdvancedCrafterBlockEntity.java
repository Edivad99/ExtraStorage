package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
    private final LazyOptional<IItemHandler> patternsCapability = LazyOptional.of(() -> getNode().getPatternItems());
    private final CrafterTier tier;

    private AdvancedCrafterNetworkNode.CrafterMode CRAFTER_MODE;
    private static final String CRAFTER_MODE_ID = "crafter_mode_id";

    public AdvancedCrafterBlockEntity(CrafterTier tier, BlockPos pos, BlockState state)
    {
        super(Registration.CRAFTER_TILE.get(tier).get(), pos, state);
        this.tier = tier;
        CRAFTER_MODE = AdvancedCrafterNetworkNode.CrafterMode.IGNORE;
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

    public void setCrafterMode(AdvancedCrafterNetworkNode.CrafterMode mode) {
        CRAFTER_MODE = mode;
        getNode().setMode(CRAFTER_MODE);
        setChanged();
    }

    public AdvancedCrafterNetworkNode.CrafterMode getCrafterMode() {
        return CRAFTER_MODE;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(CRAFTER_MODE_ID, CRAFTER_MODE.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CRAFTER_MODE = AdvancedCrafterNetworkNode.CrafterMode.getById(tag.getInt(CRAFTER_MODE_ID));
    }

    @Override
    public CompoundTag writeUpdate(CompoundTag tag) {
        tag.putInt(CRAFTER_MODE_ID, CRAFTER_MODE.ordinal());
        return super.writeUpdate(tag);
    }

    @Override
    public void readUpdate(CompoundTag tag) {
        super.readUpdate(tag);
        CRAFTER_MODE = AdvancedCrafterNetworkNode.CrafterMode.getById(tag.getInt(CRAFTER_MODE_ID));
    }
}
