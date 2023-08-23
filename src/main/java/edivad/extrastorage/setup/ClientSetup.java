package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedExporterScreen;
import edivad.extrastorage.client.screen.AdvancedFluidStorageBlockScreen;
import edivad.extrastorage.client.screen.AdvancedImporterScreen;
import edivad.extrastorage.client.screen.AdvancedStorageBlockScreen;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

  private static final BakedModelOverrideRegistry BAKED_MODEL_OVERRIDE_REGISTRY = new BakedModelOverrideRegistry();

  public static void init(FMLClientSetupEvent event) {
    //Version checker
    MinecraftForge.EVENT_BUS.register(new UpdateChecker(ExtraStorage.ID));

    //Special render & GUI
    for (var tier : CrafterTier.values()) {
      MenuScreens.register(ESContainer.CRAFTER.get(tier).get(), AdvancedCrafterScreen::new);
    }

    MenuScreens.register(ESContainer.ADVANCED_EXPORTER.get(), AdvancedExporterScreen::new);
    MenuScreens.register(ESContainer.ADVANCED_IMPORTER.get(), AdvancedImporterScreen::new);

    for (var type : ItemStorageType.values()) {
      MenuScreens.register(ESContainer.ITEM_STORAGE.get(type).get(),
          AdvancedStorageBlockScreen::new);
    }
    for (var type : FluidStorageType.values()) {
      MenuScreens.register(ESContainer.FLUID_STORAGE.get(type).get(),
          AdvancedFluidStorageBlockScreen::new);
    }

    API.instance().addPatternRenderHandler(pattern -> {
      var container = Minecraft.getInstance().player.containerMenu;
      if (container instanceof AdvancedCrafterContainerMenu actualContainer) {
        int slots = actualContainer.getBlockEntity().getTier().getSlots();
        for (int i = 0; i < slots; i++) {
          if (container.getSlot(i).getItem() == pattern) {
            return true;
          }
        }
      }
      return false;
    });
  }

  public static void onModelBake(ModelEvent.BakingCompleted e) {
    for (var id : e.getModels().keySet()) {
      var factory = BAKED_MODEL_OVERRIDE_REGISTRY.get(
          new ResourceLocation(id.getNamespace(), id.getPath()));
      if (factory != null) {
        e.getModels().put(id, factory.create(e.getModels().get(id), e.getModels()));
      }
    }
  }
}