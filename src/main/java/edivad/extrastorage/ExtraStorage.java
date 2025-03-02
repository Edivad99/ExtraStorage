package edivad.extrastorage;

import java.util.Arrays;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.support.network.AbstractNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import edivad.extrastorage.blockentity.AdvancedAutocrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedExporterScreen;
import edivad.extrastorage.client.screen.AdvancedImporterScreen;
import edivad.extrastorage.client.screen.AdvancedStorageBlockScreen;
import edivad.extrastorage.compat.top.TOPIntegration;
import edivad.extrastorage.container.AdvancedStorageBlockContainerMenu;
import edivad.extrastorage.data.ExtraStorageBlockTagsProvider;
import edivad.extrastorage.data.ExtraStorageItemTagsProvider;
import edivad.extrastorage.data.ExtraStorageLanguageProvider;
import edivad.extrastorage.data.ExtraStorageRecipeProvider;
import edivad.extrastorage.data.loot.pack.ExtraStorageLootTableProvider;
import edivad.extrastorage.data.models.ExtraStorageBlockModelProvider;
import edivad.extrastorage.data.models.ExtraStorageItemModelProvider;
import edivad.extrastorage.items.storage.fluid.AdvancedFluidStorageVariant;
import edivad.extrastorage.items.storage.item.AdvancedItemStorageVariant;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.Config;
import edivad.extrastorage.setup.CreativeModeTabs;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.setup.ESLootFunctions;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
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

  public ExtraStorage(ModContainer modContainer, Dist dist) {
    var modEventBus = modContainer.getEventBus();
    ESBlocks.register(modEventBus);
    ESItems.register(modEventBus);
    ESBlockEntities.register(modEventBus);
    ESContainer.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Config.registerConfig(modContainer);

    if (dist.isClient()) {
      modEventBus.addListener(ClientSetup::handleClientSetup);
    }

    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleRegisterMenuScreens);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::onRegister);
    modEventBus.addListener(this::registerCapabilities);
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(ID, path);
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
    generator.addProvider(event.includeServer(), new ExtraStorageLootTableProvider(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(), new ExtraStorageRecipeProvider(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(), new ExtraStorageLanguageProvider(packOutput));
    /*generator.addProvider(event.includeServer(),
        new ExtraStorageAdvancementProvider(packOutput, lookupProvider, existingFileHelper));*/
    generator.addProvider(event.includeClient(),
        new ExtraStorageBlockModelProvider(packOutput, existingFileHelper));
    generator.addProvider(event.includeClient(),
        new ExtraStorageItemModelProvider(packOutput, existingFileHelper));
  }

  public void handleCommonSetup(FMLCommonSetupEvent event) {
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
    for (var type : AdvancedItemStorageVariant.values()) {
      event.register(ESContainer.ITEM_STORAGE.get(type).get(),
          new MenuScreens.ScreenConstructor<AdvancedStorageBlockContainerMenu, AdvancedStorageBlockScreen>() {
            @Override
            public AdvancedStorageBlockScreen create(AdvancedStorageBlockContainerMenu menu, Inventory inventory, Component component) {
              var resourceRendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(ItemResource.class);
              return new AdvancedStorageBlockScreen(menu, inventory, component, resourceRendering);
            }
          });
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      event.register(ESContainer.FLUID_STORAGE.get(type).get(),
          new MenuScreens.ScreenConstructor<AdvancedStorageBlockContainerMenu, AdvancedStorageBlockScreen>() {
            @Override
            public AdvancedStorageBlockScreen create(AdvancedStorageBlockContainerMenu menu, Inventory inventory, Component component) {
              var resourceRendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(FluidResource.class);
              return new AdvancedStorageBlockScreen(menu, inventory, component, resourceRendering);
            }
          });
    }
    event.register(ESContainer.ADVANCED_EXPORTER.get(), AdvancedExporterScreen::new);
    event.register(ESContainer.ADVANCED_IMPORTER.get(), AdvancedImporterScreen::new);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    registerNetworkNodeContainerProvider(event, ESBlockEntities.ADVANCED_EXPORTER.get());
    registerNetworkNodeContainerProvider(event, ESBlockEntities.ADVANCED_IMPORTER.get());
    Arrays.stream(AdvancedItemStorageVariant.values()).forEach(type ->
        registerNetworkNodeContainerProvider(event, ESBlockEntities.ITEM_STORAGE.get(type).get()));
    Arrays.stream(AdvancedFluidStorageVariant.values()).forEach(type ->
        registerNetworkNodeContainerProvider(event, ESBlockEntities.FLUID_STORAGE.get(type).get()));
    Arrays.stream(CrafterTier.values()).forEach(type ->
        registerNetworkNodeContainerProvider(event, ESBlockEntities.CRAFTER.get(type).get()));
  }

  private void registerNetworkNodeContainerProvider(RegisterCapabilitiesEvent event,
      BlockEntityType<? extends AbstractNetworkNodeContainerBlockEntity<?>> type) {
    event.registerBlockEntity(
        RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(),
        type,
        (be, side) -> be.getContainerProvider()
    );
  }
}
