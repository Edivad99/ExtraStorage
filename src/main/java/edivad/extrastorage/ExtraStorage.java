package edivad.extrastorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import edivad.extrastorage.blocks.CrafterTier;
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
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(ExtraStorage.ID)
public class ExtraStorage {

  public static final String ID = "extrastorage";
  public static final String MODNAME = "ExtraStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public ExtraStorage() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    if (FMLEnvironment.dist.isClient()) {
      modEventBus.addListener(ClientSetup::init);
      modEventBus.addListener(ClientSetup::onModelBake);
    }

    ESBlocks.register(modEventBus);
    ESItems.register(modEventBus);
    ESBlockEntities.register(modEventBus);
    ESContainer.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Config.init();

    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::onRegister);
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
}
