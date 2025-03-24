package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterData;
import com.refinedmods.refinedstorage.common.exporter.ExporterData;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.advancedexporter.AdvancedExporterContainerMenu;
import edivad.extrastorage.advancedimporter.AdvancedImporterContainerMenu;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterContainerMenu;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import edivad.extrastorage.storage.advancedstorageblock.AdvancedStorageBlockContainerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESContainer {

  public static final Map<AdvancedItemStorageVariant, DeferredHolder<MenuType<?>, MenuType<AdvancedStorageBlockContainerMenu>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<AdvancedFluidStorageVariant, DeferredHolder<MenuType<?>, MenuType<AdvancedStorageBlockContainerMenu>>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredHolder<MenuType<?>, MenuType<AdvancedAutocrafterContainerMenu>>> CRAFTER = new HashMap<>();
  private static final DeferredRegister<MenuType<?>> MENU =
      DeferredRegister.create(BuiltInRegistries.MENU, ExtraStorage.ID);

  public static final DeferredHolder<MenuType<?>, MenuType<AdvancedExporterContainerMenu>> ADVANCED_EXPORTER =
      MENU.register("advanced_exporter", () ->
          new MenuType<>((IContainerFactory<AdvancedExporterContainerMenu>) (id, inventory, buf) ->
              new AdvancedExporterContainerMenu(id, inventory, ExporterData.STREAM_CODEC.decode(buf)), FeatureFlags.DEFAULT_FLAGS));

  public static final DeferredHolder<MenuType<?>, MenuType<AdvancedImporterContainerMenu>> ADVANCED_IMPORTER =
      MENU.register("advanced_importer", () ->
          new MenuType<>((IContainerFactory<AdvancedImporterContainerMenu>) (id, inventory, buf) ->
              new AdvancedImporterContainerMenu(id, inventory, ResourceContainerData.STREAM_CODEC.decode(buf)), FeatureFlags.DEFAULT_FLAGS));

  static {
    for (var type : AdvancedItemStorageVariant.values()) {
      ITEM_STORAGE.put(type,
          MENU.register("block_" + type.getName(), () ->
              new MenuType<>((IContainerFactory<AdvancedStorageBlockContainerMenu>) (id, inventory, buf) -> {
                var data = RefinedStorageApi.INSTANCE.getStorageBlockDataStreamCodec().decode(buf);
                return new AdvancedStorageBlockContainerMenu(ITEM_STORAGE.get(type).get(), id,
                    inventory.player, data, RefinedStorageApi.INSTANCE.getItemResourceFactory());
              }, FeatureFlags.DEFAULT_FLAGS)));
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      FLUID_STORAGE.put(type,
          MENU.register("block_" + type.getName() + "_fluid", () ->
              new MenuType<>((IContainerFactory<AdvancedStorageBlockContainerMenu>) (id, inventory, buf) -> {
                var data = RefinedStorageApi.INSTANCE.getStorageBlockDataStreamCodec().decode(buf);
                return new AdvancedStorageBlockContainerMenu(FLUID_STORAGE.get(type).get(), id,
                    inventory.player, data, RefinedStorageApi.INSTANCE.getFluidResourceFactory());
              }, FeatureFlags.DEFAULT_FLAGS)));
    }

    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier,
          MENU.register(tier.getID(), () ->
              new MenuType<>((IContainerFactory<AdvancedAutocrafterContainerMenu>) (id, inventory, buf) -> {
                var data = AutocrafterData.STREAM_CODEC.decode(buf);
                return new AdvancedAutocrafterContainerMenu(id, inventory, data, tier);
              },FeatureFlags.DEFAULT_FLAGS)));
    }
  }

  public static void register(IEventBus modEventBus) {
    MENU.register(modEventBus);
  }
}
