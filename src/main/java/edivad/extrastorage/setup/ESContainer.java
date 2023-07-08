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
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ESContainer {

  private static final DeferredRegister<MenuType<?>> CONTAINERS =
      DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExtraStorage.MODID);

  public static final Map<ItemStorageType, RegistryObject<MenuType<AdvancedStorageBlockContainerMenu>>> ITEM_STORAGE = new HashMap<>();
  public static final Map<FluidStorageType, RegistryObject<MenuType<AdvancedFluidStorageBlockContainerMenu>>> FLUID_STORAGE = new HashMap<>();

  public static final Map<CrafterTier, RegistryObject<MenuType<AdvancedCrafterContainerMenu>>> CRAFTER = new HashMap<>();


  public static final RegistryObject<MenuType<AdvancedExporterContainerMenu>> ADVANCED_EXPORTER =
      CONTAINERS.register("advanced_exporter",
          () -> IForgeMenuType.create((windowId, inv, data) -> {
            var pos = data.readBlockPos();
            var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof AdvancedExporterBlockEntity be)) {
              ExtraStorage.LOGGER.error(
                  "Wrong type of blockentity (expected AdvancedExporterBlockEntity)!");
              return null;
            }
            return new AdvancedExporterContainerMenu(windowId, inv.player, be);
          }));
  public static final RegistryObject<MenuType<AdvancedImporterContainerMenu>> ADVANCED_IMPORTER =
      CONTAINERS.register("advanced_importer",
          () -> IForgeMenuType.create((windowId, inv, data) -> {
            var pos = data.readBlockPos();
            var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof AdvancedImporterBlockEntity be)) {
              ExtraStorage.LOGGER.error(
                  "Wrong type of blockentity (expected AdvancedImporterBlockEntity)!");
              return null;
            }
            return new AdvancedImporterContainerMenu(windowId, inv.player, be);
          }));

  static {
    for (var type : ItemStorageType.values()) {
      ITEM_STORAGE.put(type,
          CONTAINERS.register("block_" + type.getName(),
              () -> IForgeMenuType.create((windowId, inv, data) -> {
                var pos = data.readBlockPos();
                var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedStorageBlockEntity be)) {
                  ExtraStorage.LOGGER.error(
                      "Wrong type of blockentity (expected AdvancedStorageBlockEntity)!");
                  return null;
                }
                return new AdvancedStorageBlockContainerMenu(windowId, inv.player, be);
              })));
    }
    for (var type : FluidStorageType.values()) {
      FLUID_STORAGE.put(type,
          CONTAINERS.register("block_" + type.getName() + "_fluid",
              () -> IForgeMenuType.create((windowId, inv, data) -> {
                var pos = data.readBlockPos();
                var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedFluidStorageBlockEntity be)) {
                  ExtraStorage.LOGGER.error(
                      "Wrong type of blockentity (expected AdvancedFluidStorageBlockEntity)!");
                  return null;
                }
                return new AdvancedFluidStorageBlockContainerMenu(windowId, inv.player, be);
              })));
    }

    for (var tier : CrafterTier.values()) {
      CRAFTER.put(tier,
          CONTAINERS.register(tier.getID(), () -> IForgeMenuType.create((windowId, inv, data) -> {
            var pos = data.readBlockPos();
            var blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
            if (!(blockEntity instanceof AdvancedCrafterBlockEntity be)) {
              ExtraStorage.LOGGER.error(
                  "Wrong type of blockentity (expected AdvancedCrafterBlockEntity)!");
              return null;
            }
            return new AdvancedCrafterContainerMenu(windowId, inv.player, be);
          })));
    }
  }

  public static void register(IEventBus modEventBus) {
    CONTAINERS.register(modEventBus);
  }
}
