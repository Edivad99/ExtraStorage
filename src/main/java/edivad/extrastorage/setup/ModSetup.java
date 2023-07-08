package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
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
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ExtraStorage.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

  public static void init(final FMLCommonSetupEvent event) {
    for (CrafterTier tier : CrafterTier.values()) {
      API.instance().getNetworkNodeRegistry().add(ExtraStorage.rl(tier.getID()),
          (tag, world, pos) ->
              readAndReturn(tag, new AdvancedCrafterNetworkNode(world, pos, tier)));
      ESBlockEntities.CRAFTER.get(tier).get().create(BlockPos.ZERO, null).getDataManager()
          .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    }
    for (ItemStorageType type : ItemStorageType.values()) {
      API.instance().getNetworkNodeRegistry()
          .add(ExtraStorage.rl("block_" + type.getName()),
              (tag, world, pos) ->
                  readAndReturn(tag, new AdvancedStorageNetworkNode(world, pos, type)));
      ESBlockEntities.ITEM_STORAGE.get(type).get().create(BlockPos.ZERO, null).getDataManager()
          .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    }
    for (FluidStorageType type : FluidStorageType.values()) {
      API.instance().getNetworkNodeRegistry()
          .add(ExtraStorage.rl("block_" + type.getName() + "_fluid"),
              (tag, world, pos) ->
                  readAndReturn(tag, new AdvancedFluidStorageNetworkNode(world, pos, type)));
      ESBlockEntities.FLUID_STORAGE.get(type).get().create(BlockPos.ZERO, null).getDataManager()
          .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    }

    API.instance().getNetworkNodeRegistry().add(AdvancedExporterNetworkNode.ID,
        (tag, world, pos) -> readAndReturn(tag, new AdvancedExporterNetworkNode(world, pos)));
    API.instance().getNetworkNodeRegistry().add(AdvancedImporterNetworkNode.ID,
        (tag, world, pos) -> readAndReturn(tag, new AdvancedImporterNetworkNode(world, pos)));
    ESBlockEntities.ADVANCED_EXPORTER.get().create(BlockPos.ZERO, null).getDataManager()
        .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    ESBlockEntities.ADVANCED_IMPORTER.get().create(BlockPos.ZERO, null).getDataManager()
        .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);

    //Integrations
    if (ModList.get().isLoaded("theoneprobe")) {
      InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPIntegration::new);
    }
    if (ModList.get().isLoaded("inventorysorter")) {
      ESContainer.CRAFTER.values()
          .forEach(v -> InterModComms.sendTo("inventorysorter", "containerblacklist", v::getId));
      ESContainer.ITEM_STORAGE.values()
          .forEach(v -> InterModComms.sendTo("inventorysorter", "containerblacklist", v::getId));
      ESContainer.FLUID_STORAGE.values()
          .forEach(v -> InterModComms.sendTo("inventorysorter", "containerblacklist", v::getId));
      InterModComms.sendTo("inventorysorter", "containerblacklist",
          ESContainer.ADVANCED_EXPORTER::getId);
      InterModComms.sendTo("inventorysorter", "containerblacklist",
          ESContainer.ADVANCED_IMPORTER::getId);
    }
  }

  private static INetworkNode readAndReturn(CompoundTag tag, NetworkNode node) {
    node.read(tag);
    return node;
  }
}
