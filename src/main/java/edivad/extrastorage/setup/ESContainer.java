package edivad.extrastorage.setup;

import java.util.HashMap;
import java.util.Map;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.container.AdvancedExporterContainerMenu;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainerMenu;
import edivad.extrastorage.container.AdvancedImporterContainerMenu;
import edivad.extrastorage.container.AdvancedStorageBlockContainerMenu;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESContainer {

  public static final Map<ItemStorageType, DeferredHolder<MenuType<?>, MenuType<AdvancedStorageBlockContainerMenu>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, DeferredHolder<MenuType<?>, MenuType<AdvancedFluidStorageBlockContainerMenu>>> FLUID_STORAGE = new HashMap<>();
  public static final Map<CrafterTier, DeferredHolder<MenuType<?>, MenuType<AdvancedCrafterContainerMenu>>> CRAFTER = new HashMap<>();
  private static final DeferredRegister<MenuType<?>> MENU =
      DeferredRegister.create(BuiltInRegistries.MENU, ExtraStorage.ID);

  public static final DeferredHolder<MenuType<?>, MenuType<AdvancedExporterContainerMenu>> ADVANCED_EXPORTER =
      MENU.register("advanced_exporter", () ->
          new MenuType<>((IContainerFactory<AdvancedExporterContainerMenu>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof AdvancedExporterBlockEntity be)) {
              ExtraStorage.LOGGER.error(
                  "Wrong type of blockentity (expected AdvancedExporterBlockEntity)!");
              return null;
            }
            return new AdvancedExporterContainerMenu(id, inventory.player, be);
          }, FeatureFlags.DEFAULT_FLAGS));

  public static final DeferredHolder<MenuType<?>, MenuType<AdvancedImporterContainerMenu>> ADVANCED_IMPORTER =
      MENU.register("advanced_importer", () ->
          new MenuType<>((IContainerFactory<AdvancedImporterContainerMenu>) (id, inventory, buf) -> {
            var pos = buf.readBlockPos();
            var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof AdvancedImporterBlockEntity be)) {
              ExtraStorage.LOGGER.error(
                  "Wrong type of blockentity (expected AdvancedImporterBlockEntity)!");
              return null;
            }
            return new AdvancedImporterContainerMenu(id, inventory.player, be);
          }, FeatureFlags.DEFAULT_FLAGS));

  static {
    for (var type : ItemStorageType.values()) {
      ITEM_STORAGE.put(type,
          MENU.register("block_" + type.getName(), () ->
              new MenuType<>((IContainerFactory<AdvancedStorageBlockContainerMenu>) (id, inventory, buf) -> {
                var pos = buf.readBlockPos();
                var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedStorageBlockEntity be)) {
                  ExtraStorage.LOGGER.error(
                      "Wrong type of blockentity (expected AdvancedStorageBlockEntity)!");
                  return null;
                }
                return new AdvancedStorageBlockContainerMenu(id, inventory.player, be);
              }, FeatureFlags.DEFAULT_FLAGS)));
    }
    for (var type : FluidStorageType.values()) {
      FLUID_STORAGE.put(type,
          MENU.register("block_" + type.getName() + "_fluid", () ->
              new MenuType<>((IContainerFactory<AdvancedFluidStorageBlockContainerMenu>) (id, inventory, buf) -> {
                var pos = buf.readBlockPos();
                var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedFluidStorageBlockEntity be)) {
                  ExtraStorage.LOGGER.error(
                      "Wrong type of blockentity (expected AdvancedFluidStorageBlockEntity)!");
                  return null;
                }
                return new AdvancedFluidStorageBlockContainerMenu(id, inventory.player, be);
              }, FeatureFlags.DEFAULT_FLAGS)));
    }

    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier,
          MENU.register(tier.getID(), () ->
              new MenuType<>((IContainerFactory<AdvancedCrafterContainerMenu>) (id, inventory, buf) -> {
                var pos = buf.readBlockPos();
                var blockEntity = inventory.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedCrafterBlockEntity be)) {
                  ExtraStorage.LOGGER.error(
                      "Wrong type of blockentity (expected AdvancedCrafterBlockEntity)!");
                  return null;
                }
                return new AdvancedCrafterContainerMenu(id, inventory.player, be);
              },FeatureFlags.DEFAULT_FLAGS)));
    }
  }

  public static void register(IEventBus modEventBus) {
    MENU.register(modEventBus);
  }
}
