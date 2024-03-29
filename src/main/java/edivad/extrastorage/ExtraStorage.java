package edivad.extrastorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedExporterScreen;
import edivad.extrastorage.client.screen.AdvancedFluidStorageBlockScreen;
import edivad.extrastorage.client.screen.AdvancedImporterScreen;
import edivad.extrastorage.client.screen.AdvancedStorageBlockScreen;
import edivad.extrastorage.compat.top.TOPIntegration;
import edivad.extrastorage.data.ExtraStorageBlockTagsProvider;
import edivad.extrastorage.data.ExtraStorageItemTagsProvider;
import edivad.extrastorage.data.ExtraStorageLanguageProvider;
import edivad.extrastorage.data.ExtraStorageRecipeProvider;
import edivad.extrastorage.data.loot.pack.ExtraStorageLootTableProvider;
import edivad.extrastorage.data.models.ExtraStorageBlockModelProvider;
import edivad.extrastorage.data.models.ExtraStorageItemModelProvider;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.Config;
import edivad.extrastorage.setup.CreativeModeTabs;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ExtraStorage.ID)
public class ExtraStorage {

  public static final String ID = "extrastorage";
  public static final String MODNAME = "ExtraStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public ExtraStorage(IEventBus modEventBus, Dist dist) {
    ESBlocks.register(modEventBus);
    ESItems.register(modEventBus);
    ESBlockEntities.register(modEventBus);
    ESContainer.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Config.init();

    if (dist.isClient()) {
      modEventBus.addListener(ClientSetup::handleClientSetup);
      modEventBus.addListener(ClientSetup::onModelBake);
    }

    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::onRegister);
    modEventBus.addListener(this::registerCapabilities);
  }

  public static ResourceLocation rl(String path) {
    return new ResourceLocation(ID, path);
  }

  private static INetworkNode readAndReturn(CompoundTag tag, NetworkNode node) {
    node.read(tag);
    return node;
  }

  private void onRegister(final RegisterEvent e) {
    e.register(Registries.LOOT_FUNCTION_TYPE, helper -> ESLootFunctions.register());
  }

  private void handleGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var existingFileHelper = event.getExistingFileHelper();

    var blockTags =
        new ExtraStorageBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
    var blockTagsLookup = blockTags.contentsGetter();
    generator.addProvider(event.includeServer(), blockTags);
    generator.addProvider(event.includeServer(),
        new ExtraStorageItemTagsProvider(packOutput, lookupProvider, blockTagsLookup,
            existingFileHelper));
    generator.addProvider(event.includeServer(), new ExtraStorageLootTableProvider(packOutput));
    generator.addProvider(event.includeServer(), new ExtraStorageRecipeProvider(packOutput));
    generator.addProvider(event.includeServer(), new ExtraStorageLanguageProvider(packOutput));
    /*generator.addProvider(event.includeServer(),
        new ExtraStorageAdvancementProvider(packOutput, lookupProvider, existingFileHelper));*/
    generator.addProvider(event.includeClient(),
        new ExtraStorageBlockModelProvider(packOutput, existingFileHelper));
    generator.addProvider(event.includeClient(),
        new ExtraStorageItemModelProvider(packOutput, existingFileHelper));
  }

  public void handleCommonSetup(FMLCommonSetupEvent event) {
    for (var tier : CrafterTier.values()) {
      API.instance().getNetworkNodeRegistry().add(ExtraStorage.rl(tier.getID()),
          (tag, world, pos) ->
              readAndReturn(tag, new AdvancedCrafterNetworkNode(world, pos, tier)));
      ESBlockEntities.CRAFTER.get(tier).get().create(BlockPos.ZERO, null).getDataManager()
          .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    }
    for (var type : ItemStorageType.values()) {
      API.instance().getNetworkNodeRegistry()
          .add(ExtraStorage.rl("block_" + type.getName()),
              (tag, world, pos) ->
                  readAndReturn(tag, new AdvancedStorageNetworkNode(world, pos, type)));
      ESBlockEntities.ITEM_STORAGE.get(type).get().create(BlockPos.ZERO, null).getDataManager()
          .getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
    }
    for (var type : FluidStorageType.values()) {
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

  private void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    for (var tier : CrafterTier.values()) {
      event.register(ESContainer.CRAFTER.get(tier).get(), AdvancedCrafterScreen::new);
    }
    for (var type : ItemStorageType.values()) {
      event.register(ESContainer.ITEM_STORAGE.get(type).get(), AdvancedStorageBlockScreen::new);
    }
    for (var type : FluidStorageType.values()) {
      event.register(ESContainer.FLUID_STORAGE.get(type).get(), AdvancedFluidStorageBlockScreen::new);
    }
    event.register(ESContainer.ADVANCED_EXPORTER.get(), AdvancedExporterScreen::new);
    event.register(ESContainer.ADVANCED_IMPORTER.get(), AdvancedImporterScreen::new);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    for (var tier : CrafterTier.values()) {
      event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
          ESBlockEntities.CRAFTER.get(tier).get(), AdvancedCrafterBlockEntity::getPatterns);
    }
  }
}
