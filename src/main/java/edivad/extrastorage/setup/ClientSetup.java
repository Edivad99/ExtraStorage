package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.advancedexporter.AdvancedExporterScreen;
import edivad.extrastorage.advancedimporter.AdvancedImporterScreen;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterScreen;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedStorageBlockContainerMenu;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedStorageBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientSetup {

  public static void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ExtraStorage.ID));

    for (var value : ESItems.ITEM_DISK.values()) {
      RefinedStorageClientApi.INSTANCE.registerDiskModel(
          value.asItem(), IdentifierUtil.createIdentifier("block/disk/disk"));
    }

    for (var value : ESItems.FLUID_DISK.values()) {
      RefinedStorageClientApi.INSTANCE.registerDiskModel(
          value.asItem(), IdentifierUtil.createIdentifier("block/disk/fluid_disk"));
    }
  }

  public static void handleRegisterMenuScreens(RegisterMenuScreensEvent event) {
    for (var tier : CrafterTier.values()) {
      event.register(ESContainer.CRAFTER.get(tier).get(), AdvancedAutocrafterScreen::new);
    }
    for (var type : AdvancedItemStorageVariant.values()) {
      event.register(ESContainer.ITEM_STORAGE.get(type).get(),
          new MenuScreens.ScreenConstructor<AdvancedStorageBlockContainerMenu, AdvancedStorageBlockScreen>() {
            @Override
            public AdvancedStorageBlockScreen create(AdvancedStorageBlockContainerMenu menu, Inventory inventory, Component component) {
              var resourceRendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(
                  ItemResource.class);
              return new AdvancedStorageBlockScreen(menu, inventory, component, resourceRendering);
            }
          });
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      event.register(ESContainer.FLUID_STORAGE.get(type).get(),
          new MenuScreens.ScreenConstructor<AdvancedStorageBlockContainerMenu, AdvancedStorageBlockScreen>() {
            @Override
            public AdvancedStorageBlockScreen create(AdvancedStorageBlockContainerMenu menu, Inventory inventory, Component component) {
              var resourceRendering = RefinedStorageClientApi.INSTANCE.getResourceRendering(
                  FluidResource.class);
              return new AdvancedStorageBlockScreen(menu, inventory, component, resourceRendering);
            }
          });
    }
    event.register(ESContainer.ADVANCED_EXPORTER.get(), AdvancedExporterScreen::new);
    event.register(ESContainer.ADVANCED_IMPORTER.get(), AdvancedImporterScreen::new);
  }
}
