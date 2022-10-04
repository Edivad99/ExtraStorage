package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.*;
import edivad.extrastorage.blocks.*;
import edivad.extrastorage.container.*;
import edivad.extrastorage.items.storage.AdvancedFluidStorageBlockItem;
import edivad.extrastorage.items.storage.AdvancedStorageBlockItem;
import edivad.extrastorage.items.storage.fluid.ExpandedStorageDiskFluid;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ExpandedStorageDiskItem;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class Registration {

    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE_PART = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE_PART = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_DISK = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_DISK = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<AdvancedStorageBlock>> ITEM_STORAGE_BLOCK = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<BlockEntityType<AdvancedStorageBlockEntity>>> ITEM_STORAGE_TILE = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<MenuType<AdvancedStorageBlockContainerMenu>>> ITEM_STORAGE_CONTAINER = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<AdvancedFluidStorageBlock>> FLUID_STORAGE_BLOCK = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<BlockEntityType<AdvancedFluidStorageBlockEntity>>> FLUID_STORAGE_TILE = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<MenuType<AdvancedFluidStorageBlockContainerMenu>>> FLUID_STORAGE_CONTAINER = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<AdvancedCrafterBlock>> CRAFTER_BLOCK = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<Item>> CRAFTER = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<BlockEntityType<AdvancedCrafterBlockEntity>>> CRAFTER_TILE = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<MenuType<AdvancedCrafterContainerMenu>>> CRAFTER_CONTAINER = new HashMap<>();
    public static final Map<String, RegistryObject<LootItemFunctionType>> REGISTERED_LOOT_ITEM_FUNCTIONS = new HashMap<>();
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    public static final RegistryObject<AdvancedExporterBlock> ADVANCED_EXPORTER = BLOCKS.register("advanced_exporter", AdvancedExporterBlock::new);
    public static final RegistryObject<AdvancedImporterBlock> ADVANCED_IMPORTER = BLOCKS.register("advanced_importer", AdvancedImporterBlock::new);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MODID);
    public static final RegistryObject<MenuType<AdvancedExporterContainerMenu>> ADVANCED_EXPORTER_CONTAINER = CONTAINERS.register("advanced_exporter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        if (!(blockEntity instanceof AdvancedExporterBlockEntity be)) {
            Main.LOGGER.error("Wrong type of blockentity (expected AdvancedExporterBlockEntity)!");
            return null;
        }
        return new AdvancedExporterContainerMenu(windowId, inv.player, be);
    }));
    public static final RegistryObject<MenuType<AdvancedImporterContainerMenu>> ADVANCED_IMPORTER_CONTAINER = CONTAINERS.register("advanced_importer", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        BlockEntity blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
        if (!(blockEntity instanceof AdvancedImporterBlockEntity be)) {
            Main.LOGGER.error("Wrong type of blockentity (expected AdvancedImporterBlockEntity)!");
            return null;
        }
        return new AdvancedImporterContainerMenu(windowId, inv.player, be);
    }));
    private static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_REGISTRY, Main.MODID);
    private static final Item.Properties GLOBAL_PROPERTIES = new Item.Properties().tab(ModSetup.extraStorageTab).stacksTo(64);    public static final RegistryObject<BlockEntityType<AdvancedExporterBlockEntity>> ADVANCED_EXPORTER_TILE = TILES.register("advanced_exporter", () -> registerSynchronizationParameters(AdvancedExporterBlockEntity.SPEC, BlockEntityType.Builder.of(AdvancedExporterBlockEntity::new, ADVANCED_EXPORTER.get()).build(null)));
    public static final RegistryObject<Item> ADVANCED_EXPORTER_ITEM = ITEMS.register("advanced_exporter", () -> new BaseBlockItem(ADVANCED_EXPORTER.get(), GLOBAL_PROPERTIES));
    public static final RegistryObject<Item> ADVANCED_IMPORTER_ITEM = ITEMS.register("advanced_importer", () -> new BaseBlockItem(ADVANCED_IMPORTER.get(), GLOBAL_PROPERTIES));
    public static final RegistryObject<Item> RAW_NEURAL_PROCESSOR_ITEM = ITEMS.register("raw_neural_processor", () -> new Item(GLOBAL_PROPERTIES));
    public static final RegistryObject<Item> NEURAL_PROCESSOR_ITEM = ITEMS.register("neural_processor", () -> new Item(GLOBAL_PROPERTIES));    public static final RegistryObject<BlockEntityType<AdvancedImporterBlockEntity>> ADVANCED_IMPORTER_TILE = TILES.register("advanced_importer", () -> registerSynchronizationParameters(AdvancedImporterBlockEntity.SPEC, BlockEntityType.Builder.of(AdvancedImporterBlockEntity::new, ADVANCED_IMPORTER.get()).build(null)));

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILES.register(eventBus);
        CONTAINERS.register(eventBus);
        LOOT_ITEM_FUNCTIONS.register(eventBus);

        //StoragePart
        for (ItemStorageType type : ItemStorageType.values())
            ITEM_STORAGE_PART.put(type, ITEMS.register("storagepart_" + type.getName(), () -> new Item(GLOBAL_PROPERTIES)));
        for (FluidStorageType type : FluidStorageType.values())
            FLUID_STORAGE_PART.put(type, ITEMS.register("storagepart_" + type.getName() + "_fluid", () -> new Item(GLOBAL_PROPERTIES)));

        //Disk
        for (ItemStorageType type : ItemStorageType.values())
            ITEM_DISK.put(type, ITEMS.register("disk_" + type.getName(), () -> new ExpandedStorageDiskItem(type)));
        for (FluidStorageType type : FluidStorageType.values())
            FLUID_DISK.put(type, ITEMS.register("disk_" + type.getName() + "_fluid", () -> new ExpandedStorageDiskFluid(type)));

        //Storage Block
        for (ItemStorageType type : ItemStorageType.values()) {
            String name = "block_" + type.getName();

            ITEM_STORAGE_BLOCK.put(type, BLOCKS.register(name, () -> new AdvancedStorageBlock(type)));
            ITEM_STORAGE.put(type, ITEMS.register(name, () -> new AdvancedStorageBlockItem(ITEM_STORAGE_BLOCK.get(type).get(), GLOBAL_PROPERTIES)));
            ITEM_STORAGE_TILE.put(type, TILES.register(name, () -> registerSynchronizationParameters(AdvancedStorageBlockEntity.SPEC, BlockEntityType.Builder.of((pos, state) -> new AdvancedStorageBlockEntity(type, pos, state), ITEM_STORAGE_BLOCK.get(type).get()).build(null))));
            ITEM_STORAGE_CONTAINER.put(type, CONTAINERS.register(name, () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedStorageBlockEntity be)) {
                    Main.LOGGER.error("Wrong type of blockentity (expected AdvancedStorageBlockEntity)!");
                    return null;
                }
                return new AdvancedStorageBlockContainerMenu(windowId, inv.player, be);
            })));
        }

        //Fluid Storage Block
        for (FluidStorageType type : FluidStorageType.values()) {
            String name = "block_" + type.getName() + "_fluid";

            FLUID_STORAGE_BLOCK.put(type, BLOCKS.register(name, () -> new AdvancedFluidStorageBlock(type)));
            FLUID_STORAGE.put(type, ITEMS.register(name, () -> new AdvancedFluidStorageBlockItem(FLUID_STORAGE_BLOCK.get(type).get(), GLOBAL_PROPERTIES)));
            FLUID_STORAGE_TILE.put(type, TILES.register(name, () -> registerSynchronizationParameters(AdvancedFluidStorageBlockEntity.SPEC, BlockEntityType.Builder.of((pos, state) -> new AdvancedFluidStorageBlockEntity(type, pos, state), FLUID_STORAGE_BLOCK.get(type).get()).build(null))));
            FLUID_STORAGE_CONTAINER.put(type, CONTAINERS.register(name, () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedFluidStorageBlockEntity be)) {
                    Main.LOGGER.error("Wrong type of blockentity (expected AdvancedFluidStorageBlockEntity)!");
                    return null;
                }
                return new AdvancedFluidStorageBlockContainerMenu(windowId, inv.player, be);
            })));
        }

        //Crafter
        for (CrafterTier tier : CrafterTier.values()) {
            CRAFTER_BLOCK.put(tier, BLOCKS.register(tier.getID(), () -> new AdvancedCrafterBlock(tier)));
            CRAFTER.put(tier, ITEMS.register(tier.getID(), () -> new BaseBlockItem(CRAFTER_BLOCK.get(tier).get(), GLOBAL_PROPERTIES)));
            CRAFTER_TILE.put(tier, TILES.register(tier.getID(), () -> registerSynchronizationParameters(AdvancedCrafterBlockEntity.SPEC, BlockEntityType.Builder.of((pos, state) -> new AdvancedCrafterBlockEntity(tier, pos, state), CRAFTER_BLOCK.get(tier).get()).build(null))));
            CRAFTER_CONTAINER.put(tier, CONTAINERS.register(tier.getID(), () -> IForgeMenuType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                BlockEntity blockEntity = inv.player.getCommandSenderWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof AdvancedCrafterBlockEntity be)) {
                    Main.LOGGER.error("Wrong type of blockentity (expected AdvancedCrafterBlockEntity)!");
                    return null;
                }
                return new AdvancedCrafterContainerMenu(windowId, inv.player, be);
            })));
        }

        REGISTERED_LOOT_ITEM_FUNCTIONS.put("storage_block", LOOT_ITEM_FUNCTIONS.register("storage_block", () -> new LootItemFunctionType(new StorageBlockLootFunction.Serializer())));
        REGISTERED_LOOT_ITEM_FUNCTIONS.put("crafter", LOOT_ITEM_FUNCTIONS.register("crafter", () -> new LootItemFunctionType(new AdvancedCrafterLootFunction.Serializer())));
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerSynchronizationParameters(BlockEntitySynchronizationSpec spec, BlockEntityType<T> t) {
        spec.getParameters().forEach(BlockEntitySynchronizationManager::registerParameter);
        return t;
    }



}