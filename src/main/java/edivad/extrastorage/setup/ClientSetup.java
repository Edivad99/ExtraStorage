package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import com.refinedmods.refinedstorage.render.model.baked.CableCoverBakedModel;
import edivad.edivadlib.setup.UpdateChecker;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientSetup {

  private static final BakedModelOverrideRegistry BAKED_MODEL_OVERRIDE_REGISTRY = new BakedModelOverrideRegistry();

  public static void handleClientSetup(FMLClientSetupEvent event) {
    NeoForge.EVENT_BUS.register(new UpdateChecker(ExtraStorage.ID));
    registerBakedModelOverrides();
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

  public static void onModelBake(ModelEvent.ModifyBakingResult e) {
    for (var id : e.getModels().keySet()) {
      var factory = BAKED_MODEL_OVERRIDE_REGISTRY.get(
          new ResourceLocation(id.getNamespace(), id.getPath()));
      if (factory != null) {
        e.getModels().put(id, factory.create(e.getModels().get(id), e.getModels()));
      }
    }
  }

  private static void registerBakedModelOverrides() {
    BAKED_MODEL_OVERRIDE_REGISTRY.add(ExtraStorage.rl("advanced_exporter"), (base, registry) -> new CableCoverBakedModel(base));
    BAKED_MODEL_OVERRIDE_REGISTRY.add(ExtraStorage.rl("advanced_importer"), (base, registry) -> new CableCoverBakedModel(base));
  }
}
