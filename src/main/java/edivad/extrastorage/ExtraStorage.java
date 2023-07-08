package edivad.extrastorage;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import edivad.extrastorage.data.ExtraStorageBlockTagsProvider;
import edivad.extrastorage.data.ExtraStorageItemTagsProvider;
import edivad.extrastorage.data.ExtraStorageLanguageProvider;
import edivad.extrastorage.data.ExtraStorageRecipeProvider;
import edivad.extrastorage.data.loot.pack.ExtraStorageLootTableProvider;
import edivad.extrastorage.data.models.ExtraStorageBlockModelProvider;
import edivad.extrastorage.data.models.ExtraStorageItemModelProvider;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.Config;
import edivad.extrastorage.setup.CreativeModeTabs;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.setup.ModSetup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(ExtraStorage.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraStorage {

  public static final String MODID = "extrastorage";
  public static final String MODNAME = "Extra Storage";

  public static final Logger LOGGER = LogUtils.getLogger();

  public ExtraStorage() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
      modEventBus.addListener(ClientSetup::init);
      modEventBus.addListener(ClientSetup::onModelBake);
    });
    ESBlocks.register(modEventBus);
    ESItems.register(modEventBus);
    ESBlockEntities.register(modEventBus);
    ESContainer.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Config.init();

    modEventBus.addListener(ModSetup::init);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::onRegister);
  }

  public void onRegister(final RegisterEvent e) {
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
    generator.addProvider(event.includeClient(),
        new ExtraStorageBlockModelProvider(packOutput, existingFileHelper));
    generator.addProvider(event.includeClient(),
        new ExtraStorageItemModelProvider(packOutput, existingFileHelper));
  }

  public static ResourceLocation rl(String path) {
    return new ResourceLocation(MODID, path);
  }
}
