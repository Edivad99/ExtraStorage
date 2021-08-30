package edivad.extrastorage.nodes;

import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPattern;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPatternContainer;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPatternProvider;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.ConnectivityStateChangeCause;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.inventory.item.BaseItemHandler;
import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import com.refinedmods.refinedstorage.inventory.item.validator.PatternItemValidator;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.util.StackUtils;
import com.refinedmods.refinedstorage.util.WorldUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AdvancedCrafterNetworkNode extends NetworkNode implements ICraftingPatternContainer
{
    public enum CrafterMode
    {
        IGNORE,
        SIGNAL_UNLOCKS_AUTOCRAFTING,
        SIGNAL_LOCKS_AUTOCRAFTING,
        PULSE_INSERTS_NEXT_SET;

        public static CrafterMode getById(int id)
        {
            if (id >= 0 && id < values().length)
                return values()[id];
            return IGNORE;
        }
    }

    private final TranslationTextComponent DEFAULT_NAME;

    private static final String NBT_DISPLAY_NAME = "DisplayName";
    private static final String NBT_UUID = "CrafterUuid";
    private static final String NBT_MODE = "Mode";
    private static final String NBT_LOCKED = "Locked";
    private static final String NBT_WAS_POWERED = "WasPowered";
    private static final String NBT_TIER = "Tier";

    private final BaseItemHandler patternsInventory;

    private final Map<Integer, ICraftingPattern> slot_to_pattern = new HashMap<>();
    private final List<ICraftingPattern> patterns = new ArrayList<>();

    private final UpgradeItemHandler upgrades = (UpgradeItemHandler) new UpgradeItemHandler(4, UpgradeItem.Type.SPEED)
            .addListener(new NetworkNodeInventoryListener(this));

    // Used to prevent infinite recursion on getRootContainer() when there's e.g. two crafters facing each other.
    private boolean visited = false;

    private CrafterMode mode = CrafterMode.IGNORE;
    private boolean locked = false;
    private boolean wasPowered;

    @Nullable
    private ITextComponent displayName;

    @Nullable
    private UUID uuid = null;

    private CrafterTier tier;
    private final ResourceLocation ID;

    public AdvancedCrafterNetworkNode(World world, BlockPos pos, CrafterTier tier)
    {
        super(world, pos);
        this.tier = tier;
        this.patternsInventory = new BaseItemHandler(this.tier.getSlots())
            {
                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if(!stacks.get(slot).isEmpty()) {
                        return stack;
                    }
                    return super.insertItem(slot, stack, simulate);
                }
            }
            .addValidator(new PatternItemValidator(world))
            .addListener(new NetworkNodeInventoryListener(this))
            .addListener((handler, slot, reading) -> {
                if (!reading) {
                    if (!world.isRemote)
                        invalidateSlot(slot);
                    if (network != null)
                        network.getCraftingManager().invalidate();
                }
            });
        DEFAULT_NAME = new TranslationTextComponent("block." + Main.MODID + "." + this.tier.getID());
        ID = new ResourceLocation(Main.MODID, tier.getID());
    }

    private void invalidate()
    {
        slot_to_pattern.clear();
        patterns.clear();
     
        for(int i = 0; i < patternsInventory.getSlots(); ++i)
        {
            invalidateSlot(i);
        }
    }

    private void invalidateSlot(int slot)
    {
        if (slot_to_pattern.containsKey(slot)) {
            patterns.remove(slot_to_pattern.remove(slot));
        }
        
        ItemStack patternStack = patternsInventory.getStackInSlot(slot);

        if(!patternStack.isEmpty())
        {
            ICraftingPattern pattern = ((ICraftingPatternProvider) patternStack.getItem()).create(world, patternStack, this);

            if(pattern.isValid())
            {
                slot_to_pattern.put(slot, pattern);
                patterns.add(pattern);
            }
        }
    }

    @Override
    public int getEnergyUsage()
    {
        int energyPatterns = 2 * patterns.size();
        int energyUpgrades = upgrades.getEnergyUsage() * (tier.ordinal() + 1);
        int energyCrafter = 15 * (tier.ordinal() +1);
        return energyCrafter + energyUpgrades + energyPatterns;
    }

    @Override
    public void update()
    {
        super.update();

        if (ticks == 1)
            invalidate();

        if (mode == CrafterMode.PULSE_INSERTS_NEXT_SET && world.isBlockPresent(pos))
        {
            if (world.isBlockPowered(pos))
            {
                this.wasPowered = true;
                markDirty();
            }
            else if (wasPowered)
            {
                this.wasPowered = false;
                this.locked = false;
                markDirty();
            }
        }
    }

    @Override
    protected void onConnectedStateChange(INetwork network, boolean state, ConnectivityStateChangeCause cause)
    {
        super.onConnectedStateChange(network, state, cause);
        network.getCraftingManager().invalidate();
    }

    @Override
    public void onDisconnected(INetwork network)
    {
        super.onDisconnected(network);

        network.getCraftingManager().getTasks().stream()
                .filter(task -> task.getPattern().getContainer().getPosition().equals(pos))
                .forEach(task -> network.getCraftingManager().cancel(task.getId()));
    }

    @Override
    public void onDirectionChanged(Direction direction)
    {
        super.onDirectionChanged(direction);

        if(network != null)
            network.getCraftingManager().invalidate();
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);

        StackUtils.readItems(patternsInventory, 0, tag);

        invalidate();

        StackUtils.readItems(upgrades, 1, tag);

        if (tag.contains(NBT_DISPLAY_NAME)) {
            displayName = ITextComponent.Serializer.getComponentFromJson(tag.getString(NBT_DISPLAY_NAME));
        }

        if (tag.hasUniqueId(NBT_UUID)) {
            uuid = tag.getUniqueId(NBT_UUID);
        }

        if (tag.contains(NBT_MODE)) {
            mode = CrafterMode.getById(tag.getInt(NBT_MODE));
        }

        if (tag.contains(NBT_LOCKED)) {
            locked = tag.getBoolean(NBT_LOCKED);
        }

        if (tag.contains(NBT_WAS_POWERED)) {
            wasPowered = tag.getBoolean(NBT_WAS_POWERED);
        }

        if(tag.contains(NBT_TIER)) {
            tier = CrafterTier.values()[tag.getInt(NBT_TIER)];
        }
    }

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);

        StackUtils.writeItems(patternsInventory, 0, tag);
        StackUtils.writeItems(upgrades, 1, tag);

        if (displayName != null) {
            tag.putString(NBT_DISPLAY_NAME, ITextComponent.Serializer.toJson(displayName));
        }

        if (uuid != null) {
            tag.putUniqueId(NBT_UUID, uuid);
        }

        tag.putInt(NBT_MODE, mode.ordinal());
        tag.putBoolean(NBT_LOCKED, locked);
        tag.putBoolean(NBT_WAS_POWERED, wasPowered);
        tag.putInt(NBT_TIER, tier.ordinal());

        return tag;
    }

    @Override
    public int getUpdateInterval()
    {
        int upgradesCount = upgrades.getUpgradeCount(UpgradeItem.Type.SPEED);
        if(upgradesCount < 0 || upgradesCount > 4)
            return 0;
        else
            return 10 - (upgradesCount * 2);//Min:2 Max:10
    }

    @Override
    public int getMaximumSuccessfulCraftingUpdates()
    {
        int upgradesCount = upgrades.getUpgradeCount(UpgradeItem.Type.SPEED);
        if(upgradesCount < 0 || upgradesCount > 4)
            return 1;
        else
        {
            if(tier.equals(CrafterTier.IRON))
                return upgradesCount + tier.getCraftingSpeed();
            else
                return (upgradesCount * (tier.getCraftingSpeed() / 5)) + tier.getCraftingSpeed();//PREV Min:1 Max:5
        }
    }

    @Nullable
    @Override
    public IItemHandler getConnectedInventory()
    {
        ICraftingPatternContainer proxy = getRootContainer();
        if(proxy == null)
            return null;

        return WorldUtils.getItemHandler(proxy.getFacingTile(), proxy.getDirection().getOpposite());
    }

    @Nullable
    @Override
    public IFluidHandler getConnectedFluidInventory()
    {
        ICraftingPatternContainer proxy = getRootContainer();
        if(proxy == null)
            return null;

        return WorldUtils.getFluidHandler(proxy.getFacingTile(), proxy.getDirection().getOpposite());
    }

    @Nullable
    @Override
    public TileEntity getConnectedTile()
    {
        ICraftingPatternContainer proxy = getRootContainer();
        if(proxy == null)
            return null;

        return proxy.getFacingTile();
    }

    @Nullable
    @Override
    public TileEntity getFacingTile()
    {
        BlockPos facingPos = pos.offset(getDirection());
        if (!world.isBlockPresent(facingPos))
            return null;

        return world.getTileEntity(facingPos);
    }

    @Override
    public List<ICraftingPattern> getPatterns()
    {
        return patterns;
    }

    @Nullable
    @Override
    public IItemHandlerModifiable getPatternInventory()
    {
        return patternsInventory;
    }

    @Override
    public ITextComponent getName()
    {
        if (displayName != null)
            return displayName;

        TileEntity facing = getConnectedTile();

        if (facing instanceof INameable && ((INameable) facing).getName() != null)
            return ((INameable) facing).getName();

        if (facing != null)
            return new TranslationTextComponent(world.getBlockState(facing.getPos()).getBlock().getTranslationKey());

        return DEFAULT_NAME;
    }

    public void setDisplayName(ITextComponent displayName)
    {
        this.displayName = displayName;
    }

    @Nullable
    public ITextComponent getDisplayName()
    {
        return displayName;
    }

    @Override
    public BlockPos getPosition()
    {
        return pos;
    }

    public CrafterMode getMode()
    {
        return mode;
    }

    public void setMode(CrafterMode mode)
    {
        this.mode = mode;
        this.wasPowered = false;
        this.locked = false;

        markDirty();
    }

    public IItemHandler getPatternItems()
    {
        return patternsInventory;
    }

    public UpgradeItemHandler getUpgrades()
    {
        return upgrades;
    }

    @Nullable
    @Override
    public IItemHandler getDrops()
    {
        return new CombinedInvWrapper(patternsInventory, upgrades);
    }

    @Nullable
    @Override
    public ICraftingPatternContainer getRootContainer() {
        if (visited)
            return null;

        INetworkNode facing = API.instance().getNetworkNodeManager((ServerWorld) world).getNode(pos.offset(getDirection()));
        if (!(facing instanceof ICraftingPatternContainer) || facing.getNetwork() != network)
            return this;

        visited = true;
        ICraftingPatternContainer facingContainer = ((ICraftingPatternContainer) facing).getRootContainer();
        visited = false;

        return facingContainer;
    }

    public Optional<ICraftingPatternContainer> getRootContainerNotSelf()
    {
        ICraftingPatternContainer root = getRootContainer();

        if (root != null && root != this)
            return Optional.of(root);

        return Optional.empty();
    }

    @Override
    public UUID getUuid()
    {
        if(this.uuid == null)
        {
            this.uuid = UUID.randomUUID();
            markDirty();
        }
        return this.uuid;
    }

    @Override
    public boolean isLocked()
    {
        Optional<ICraftingPatternContainer> root = getRootContainerNotSelf();
        if (root.isPresent())
            return root.get().isLocked();

        switch (mode)
        {
            case SIGNAL_LOCKS_AUTOCRAFTING:
                return world.isBlockPowered(pos);
            case SIGNAL_UNLOCKS_AUTOCRAFTING:
                return !world.isBlockPowered(pos);
            case PULSE_INSERTS_NEXT_SET:
                return locked;
            default:
                return false;
        }
    }

    @Override
    public void unlock()
    {
        locked=false;
    }

    @Override
    public void onUsedForProcessing()
    {
        Optional<ICraftingPatternContainer> root = getRootContainerNotSelf();
        if (root.isPresent())
        {
            root.get().onUsedForProcessing();
        }
        else if (mode == CrafterMode.PULSE_INSERTS_NEXT_SET)
        {
            this.locked = true;
            markDirty();
        }
    }
}
