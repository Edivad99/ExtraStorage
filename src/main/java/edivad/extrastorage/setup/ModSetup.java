package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.compat.CarryOnIntegration;
import edivad.extrastorage.compat.TOPIntegration;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final CreativeModeTab extraStorageTab = new CreativeModeTab(Main.MODID + "_tab") {

        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Registration.CRAFTER_BLOCK.get(CrafterTier.GOLD).get());
        }
    };

    public static void init(final FMLCommonSetupEvent event)
    {
        for(CrafterTier tier : CrafterTier.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, tier.getID()), (tag, world, pos) -> readAndReturn(tag, new AdvancedCrafterNetworkNode(world, pos, tier)));
            Registration.CRAFTER_TILE.get(tier).get().create(BlockPos.ZERO, null).getDataManager().getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
        }
        for(ItemStorageType type : ItemStorageType.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + type.getName()), (tag, world, pos) -> readAndReturn(tag, new AdvancedStorageNetworkNode(world, pos, type)));
            Registration.ITEM_STORAGE_TILE.get(type).get().create(BlockPos.ZERO, null).getDataManager().getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
        }
        for(FluidStorageType type : FluidStorageType.values())
        {
            API.instance().getNetworkNodeRegistry().add(new ResourceLocation(Main.MODID, "block_" + type.getName() + "_fluid"), (tag, world, pos) -> readAndReturn(tag, new AdvancedFluidStorageNetworkNode(world, pos, type)));
            Registration.FLUID_STORAGE_TILE.get(type).get().create(BlockPos.ZERO, null).getDataManager().getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
        }

        API.instance().getNetworkNodeRegistry().add(AdvancedExporterNetworkNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedExporterNetworkNode(world, pos)));
        API.instance().getNetworkNodeRegistry().add(AdvancedImporterNetworkNode.ID, (tag, world, pos) -> readAndReturn(tag, new AdvancedImporterNetworkNode(world, pos)));
        Registration.ADVANCED_EXPORTER_TILE.get().create(BlockPos.ZERO, null).getDataManager().getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
        Registration.ADVANCED_IMPORTER_TILE.get().create(BlockPos.ZERO, null).getDataManager().getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);

        //Integrations
        CarryOnIntegration.registerCarryOn();
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPIntegration::new);
        }
    }

    private static INetworkNode readAndReturn(CompoundTag tag, NetworkNode node) {
        node.read(tag);
        return node;
    }
}
