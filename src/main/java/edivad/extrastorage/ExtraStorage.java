package edivad.extrastorage;

import java.util.Arrays;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.network.AbstractNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.compat.top.TOPIntegration;
import edivad.extrastorage.data.ExtraStorageBlockTagsProvider;
import edivad.extrastorage.data.ExtraStorageItemTagsProvider;
import edivad.extrastorage.data.ExtraStorageLanguageProvider;
import edivad.extrastorage.data.loot.pack.ExtraStorageLootTableProvider;
import edivad.extrastorage.data.models.ExtraStorageBlockModelProvider;
import edivad.extrastorage.data.models.ExtraStorageItemModelProvider;
import edivad.extrastorage.data.recipes.ExtraStorageRecipeProvider;
import edivad.extrastorage.network.PacketHandler;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.Config;
import edivad.extrastorage.setup.CreativeModeTabs;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.setup.ESRecipeSerializers;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import edivad.extrastorage.tools.UpgradeDestinations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ExtraStorage.ID)
public class ExtraStorage {

  public static final String ID = "extrastorage";
  public static final String MODNAME = "ExtraStorage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public ExtraStorage(ModContainer modContainer, Dist dist) {
    var modEventBus = modContainer.getEventBus();
    PacketHandler.register(modEventBus);
    ESBlocks.register(modEventBus);
    ESItems.register(modEventBus);
    ESBlockEntities.register(modEventBus);
    ESContainer.register(modEventBus);
    ESRecipeSerializers.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Config.registerConfig(modContainer);

    if (dist.isClient()) {
      modEventBus.addListener(ClientSetup::handleClientSetup);
      modEventBus.addListener(ClientSetup::handleRegisterMenuScreens);
    }

    modEventBus.addListener(this::handleCommonSetup);
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
    this.registerUpgradeMappings();
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

  private void registerUpgradeMappings() {
    RefinedStorageApi.INSTANCE.getUpgradeRegistry().forDestination(UpgradeDestinations.ADVANCED_IMPORTER)
        .add(Items.INSTANCE.getSpeedUpgrade(), 4)
        .add(Items.INSTANCE.getStackUpgrade())
        .add(Items.INSTANCE.getRegulatorUpgrade(), 4);

    RefinedStorageApi.INSTANCE.getUpgradeRegistry().forDestination(UpgradeDestinations.ADVANCED_EXPORTER)
        .add(Items.INSTANCE.getSpeedUpgrade(), 4)
        .add(Items.INSTANCE.getStackUpgrade())
        .add(Items.INSTANCE.getRegulatorUpgrade(), 4)
        .add(Items.INSTANCE.getAutocraftingUpgrade());

    RefinedStorageApi.INSTANCE.getUpgradeRegistry().forDestination(UpgradeDestinations.ADVANCED_AUTOCRAFTER)
        .add(Items.INSTANCE.getSpeedUpgrade(), 4);
  }
}
