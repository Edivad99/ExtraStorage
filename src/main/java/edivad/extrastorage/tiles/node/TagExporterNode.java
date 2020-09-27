//package edivad.expandedstorage.tiles.node;
//
//import com.refinedmods.refinedstorage.api.util.Action;
//import com.refinedmods.refinedstorage.apiimpl.API;
//import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
//import com.refinedmods.refinedstorage.apiimpl.network.node.SlottedCraftingRequest;
//import com.refinedmods.refinedstorage.inventory.fluid.FluidInventory;
//import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
//import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
//import com.refinedmods.refinedstorage.item.UpgradeItem;
//import com.refinedmods.refinedstorage.tile.config.IComparable;
//import com.refinedmods.refinedstorage.tile.config.IType;
//import com.refinedmods.refinedstorage.util.StackUtils;
//import com.refinedmods.refinedstorage.util.WorldUtils;
//import edivad.expandedstorage.Main;
//import edivad.expandedstorage.tools.WildcardMatcher;
//import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tags.ITag;
//import net.minecraft.tags.ITagCollection;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.items.ItemHandlerHelper;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class TagExporterNode extends NetworkNode
//{
//    public static final ResourceLocation ID = new ResourceLocation(Main.MODID, "tag_exporter");
//    private static final Map<String, List<ItemStack>> itemTagStacks = new Object2ObjectOpenHashMap<>();
//
//    private static final String NBT_COMPARE = "Compare";
//
//    private final UpgradeItemHandler upgrades = (UpgradeItemHandler) new UpgradeItemHandler(4, UpgradeItem.Type.SPEED, UpgradeItem.Type.STACK)
//    .addListener(new NetworkNodeInventoryListener(this))
//    .addListener((handler, slot, reading) -> {
//        if (!reading && !getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR)) {
//            boolean changed = false;
//
//            for (int i = 0; i < itemFilters.getSlots(); ++i) {
//                ItemStack filterSlot = itemFilters.getStackInSlot(i);
//
//                if (filterSlot.getCount() > 1) {
//                    filterSlot.setCount(1);
//                    changed = true;
//                }
//            }
//
//            if (changed) {
//                markDirty();
//            }
//        }
//    });
//
//    public TagExporterNode(World world, BlockPos pos)
//    {
//        super(world, pos);
//    }
//
//    @Override
//    public int getEnergyUsage()
//    {
//        return 8 + upgrades.getEnergyUsage();
//    }
//
//    @Override
//    public void update() {
//        super.update();
//
//        if (canUpdate() && ticks % upgrades.getSpeed() == 0 && world.isBlockPresent(pos))
//        {
//            IItemHandler handler = WorldUtils.getItemHandler(getFacingTile(), getDirection().getOpposite());
//
//            if (handler != null)
//            {
//                ItemStack slot = itemFilters.getStackInSlot(filterSlot);
//
//                if (!slot.isEmpty())
//                {
//                    int stackSize = upgrades.getStackInteractCount();
//
//                    if (stackSize > 0)
//                    {
//                        ItemStack took = network.extractItem(slot, Math.min(slot.getMaxStackSize(), stackSize), compare, Action.SIMULATE);
//
//                        if (!took.isEmpty())
//                        {
//                            ItemStack remainder = ItemHandlerHelper.insertItem(handler, took, true);
//
//                            int correctedStackSize = took.getCount() - remainder.getCount();
//
//                            if (correctedStackSize > 0) {
//                                took = network.extractItem(slot, correctedStackSize, compare, Action.PERFORM);
//
//                                ItemHandlerHelper.insertItem(handler, took, false);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private static List<ItemStack> getItemTagStack(String oreName)
//    {
//        if(itemTagStacks.get(oreName) != null)
//            return itemTagStacks.get(oreName);
//
//        ITagCollection<Item> tagCollection = ItemTags.getCollection();
//        List<ResourceLocation> keys = tagCollection.getRegisteredTags().stream().filter(rl -> WildcardMatcher.matches(oreName, rl.toString())).collect(Collectors.toList());
//        Set<Item> items = new HashSet<>();
//        for(ResourceLocation key : keys)
//        {
//            ITag<Item> itemTag = tagCollection.get(key);
//            if(itemTag != null)
//                items.addAll(itemTag.getAllElements());
//        }
//        List<ItemStack> stacks = items.stream().map(ItemStack::new).collect(Collectors.toList());
//        itemTagStacks.put(oreName, stacks);
//        return stacks;
//    }
//
//    @Override
//    public ResourceLocation getId()
//    {
//        return ID;
//    }
//
//    @Override
//    public CompoundNBT write(CompoundNBT tag)
//    {
//        StackUtils.writeItems(upgrades, 1, tag);
//        return super.write(tag);
//    }
//
//    @Override
//    public void read(CompoundNBT tag)
//    {
//        super.read(tag);
//        StackUtils.readItems(upgrades, 1, tag);
//    }
//
//    public UpgradeItemHandler getUpgrades()
//    {
//        return upgrades;
//    }
//
//    @Override
//    public IItemHandler getDrops()
//    {
//        return upgrades;
//    }
//}
