package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.tile.data.TileDataManager;
import edivad.extrastorage.Main;
import edivad.extrastorage.compat.CarryOnIntegration;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
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
        for(CrafterTier tier : CrafterTier.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, tier.getID()), (tag, world, pos) -> readAndReturn(tag, new AdvancedCrafterNetworkNode(world, pos, tier)));
            Registration.CRAFTER_TILE.get(tier).get().create().getDataManager().getParameters().forEach(TileDataManager::registerParameter);
        }
        for(ItemStorageType type : ItemStorageType.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + type.getName()), (tag, world, pos) -> readAndReturn(tag, new AdvancedStorageNetworkNode(world, pos, type)));
            Registration.ITEM_STORAGE_TILE.get(type).get().create().getDataManager().getParameters().forEach(TileDataManager::registerParameter);
        }
        for(FluidStorageType type : FluidStorageType.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + type.getName() + "_fluid"), (tag, world, pos) -> readAndReturn(tag, new AdvancedFluidStorageNetworkNode(world, pos, type)));
            Registration.FLUID_STORAGE_TILE.get(type).get().create().getDataManager().getParameters().forEach(TileDataManager::registerParameter);
        }

        API.instance().getNetworkNodeRegistry().add(AdvancedExporterNetworkNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedExporterNetworkNode(world, pos)));
        API.instance().getNetworkNodeRegistry().add(AdvancedImporterNetworkNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedImporterNetworkNode(world, pos)));
        Registration.ADVANCED_EXPORTER_TILE.get().create().getDataManager().getParameters().forEach(TileDataManager::registerParameter);
        Registration.ADVANCED_IMPORTER_TILE.get().create().getDataManager().getParameters().forEach(TileDataManager::registerParameter);

        CarryOnIntegration.registerCarryOn();
    }

    private static INetworkNode readAndReturn(CompoundNBT tag, NetworkNode node) {
        node.read(tag);
        return node;
    }
}
