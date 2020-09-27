package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import edivad.extrastorage.Main;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.tiles.node.AdvancedCrafterNetworkNode;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.tiles.node.AdvancedExporterNetworkNode;
import edivad.extrastorage.tiles.node.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.tiles.node.AdvancedStorageNetworkNode;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup rebornStorageTab = new ItemGroup(Main.MODID + "_tab") {

        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Registration.CRAFTER_BLOCK.get(CrafterTier.GOLD).get());
        }
    };

    public static void init(final FMLCommonSetupEvent event)
    {
        for(CrafterTier value : CrafterTier.values())
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, value.getID()), (tag, world, pos) -> readAndReturn(tag, new AdvancedCrafterNetworkNode(world, pos, value)));

        API.instance().getNetworkNodeRegistry().add(AdvancedExporterNetworkNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedExporterNetworkNode(world, pos)));

        for(ItemStorageType value : ItemStorageType.values())
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + value.getName()), (tag, world, pos) -> readAndReturn(tag, new AdvancedStorageNetworkNode(world, pos, value)));
        for(FluidStorageType value : FluidStorageType.values())
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + value.getName() + "_fluid"), (tag, world, pos) -> readAndReturn(tag, new AdvancedFluidStorageNetworkNode(world, pos, value)));
    }

    private static INetworkNode readAndReturn(CompoundNBT tag, NetworkNode node) {
        node.read(tag);
        return node;
    }
}
