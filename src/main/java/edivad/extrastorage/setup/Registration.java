package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import edivad.extrastorage.blocks.AdvancedCrafterBlock;
import edivad.extrastorage.blocks.AdvancedExporter;
import edivad.extrastorage.blocks.AdvancedFluidStorageBlock;
import edivad.extrastorage.blocks.AdvancedStorageBlock;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import edivad.extrastorage.container.AdvancedExporterContainer;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainer;
import edivad.extrastorage.container.AdvancedStorageBlockContainer;
import edivad.extrastorage.items.AdvancedFluidStorageBlockItem;
import edivad.extrastorage.items.AdvancedStorageBlockItem;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.fluid.ExpandedStorageDiskFluid;
import edivad.extrastorage.Main;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.items.item.ExpandedStorageDiskItem;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import edivad.extrastorage.tiles.AdvancedExporterTile;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import edivad.extrastorage.tiles.AdvancedStorageBlockTile;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE_PART = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE_PART = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_DISK = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_DISK = new HashMap<>();

    public static final Map<ItemStorageType, RegistryObject<AdvancedStorageBlock>> ITEM_STORAGE_BLOCK = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<Item>> ITEM_STORAGE = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<TileEntityType<AdvancedStorageBlockTile>>> ITEM_STORAGE_TILE = new HashMap<>();
    public static final Map<ItemStorageType, RegistryObject<ContainerType<AdvancedStorageBlockContainer>>> ITEM_STORAGE_CONTAINER = new HashMap<>();

    public static final Map<FluidStorageType, RegistryObject<AdvancedFluidStorageBlock>> FLUID_STORAGE_BLOCK = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<Item>> FLUID_STORAGE = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<TileEntityType<AdvancedFluidStorageBlockTile>>> FLUID_STORAGE_TILE = new HashMap<>();
    public static final Map<FluidStorageType, RegistryObject<ContainerType<AdvancedFluidStorageBlockContainer>>> FLUID_STORAGE_CONTAINER = new HashMap<>();

    public static final Map<CrafterTier, RegistryObject<AdvancedCrafterBlock>> CRAFTER_BLOCK = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<Item>> CRAFTER = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<TileEntityType<AdvancedCrafterTile>>> CRAFTER_TILE = new HashMap<>();
    public static final Map<CrafterTier, RegistryObject<ContainerType<AdvancedCrafterContainer>>> CRAFTER_CONTAINER = new HashMap<>();

    public static Item.Properties globalProperties = new Item.Properties().group(ModSetup.rebornStorageTab);

    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        //StoragePart
        for(ItemStorageType type : ItemStorageType.values())
            ITEM_STORAGE_PART.put(type, ITEMS.register("storagepart_" + type.getName(), () -> new Item(globalProperties)));
        for(FluidStorageType type : FluidStorageType.values())
            FLUID_STORAGE_PART.put(type, ITEMS.register("storagepart_" + type.getName() + "_fluid", () -> new Item(globalProperties)));

        //Disk
        for(ItemStorageType type : ItemStorageType.values())
            ITEM_DISK.put(type, ITEMS.register("disk_" + type.getName(), () -> new ExpandedStorageDiskItem(type)));
        for(FluidStorageType type : FluidStorageType.values())
            FLUID_DISK.put(type, ITEMS.register("disk_" + type.getName() + "_fluid", () -> new ExpandedStorageDiskFluid(type)));

        //Storage Block
        for(ItemStorageType type : ItemStorageType.values())
        {
            String name = "block_" + type.getName();

            ITEM_STORAGE_BLOCK.put(type, BLOCKS.register(name, () -> new AdvancedStorageBlock(type)));
            ITEM_STORAGE.put(type, ITEMS.register(name, () -> new AdvancedStorageBlockItem(ITEM_STORAGE_BLOCK.get(type).get())));
            ITEM_STORAGE_TILE.put(type, TILES.register(name, () -> TileEntityType.Builder.create(() -> new AdvancedStorageBlockTile(type), ITEM_STORAGE_BLOCK.get(type).get()).build(null)));
            ITEM_STORAGE_CONTAINER.put(type, CONTAINERS.register(name, () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
                if(!(te instanceof AdvancedStorageBlockTile))
                {
                    Main.logger.error("Wrong type of tile entity (expected AdvancedStorageBlockTile)!");
                    return null;
                }
                AdvancedStorageBlockTile tile = (AdvancedStorageBlockTile) te;
                return new AdvancedStorageBlockContainer(windowId, inv.player, tile);
            })));
        }

        //Fluid Storage Block
        for(FluidStorageType type : FluidStorageType.values())
        {
            String name = "block_" + type.getName() + "_fluid";

            FLUID_STORAGE_BLOCK.put(type, BLOCKS.register(name, () -> new AdvancedFluidStorageBlock(type)));
            FLUID_STORAGE.put(type, ITEMS.register(name, () -> new AdvancedFluidStorageBlockItem(FLUID_STORAGE_BLOCK.get(type).get())));
            FLUID_STORAGE_TILE.put(type, TILES.register(name, () -> TileEntityType.Builder.create(() -> new AdvancedFluidStorageBlockTile(type), FLUID_STORAGE_BLOCK.get(type).get()).build(null)));
            FLUID_STORAGE_CONTAINER.put(type, CONTAINERS.register(name, () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
                if(!(te instanceof AdvancedFluidStorageBlockTile))
                {
                    Main.logger.error("Wrong type of tile entity (expected AdvancedFluidStorageBlockTile)!");
                    return null;
                }
                AdvancedFluidStorageBlockTile tile = (AdvancedFluidStorageBlockTile) te;
                return new AdvancedFluidStorageBlockContainer(windowId, inv.player, tile);
            })));
        }

        //Crafter
        for(CrafterTier tier : CrafterTier.values())
        {
            CRAFTER_BLOCK.put(tier, BLOCKS.register(tier.getID(), () -> new AdvancedCrafterBlock(tier)));
            CRAFTER.put(tier, ITEMS.register(tier.getID(), () -> new BlockItem(CRAFTER_BLOCK.get(tier).get(), globalProperties)));
            CRAFTER_TILE.put(tier, TILES.register(tier.getID(), () -> TileEntityType.Builder.create(() -> new AdvancedCrafterTile(tier), CRAFTER_BLOCK.get(tier).get()).build(null)));
            CRAFTER_CONTAINER.put(tier, CONTAINERS.register(tier.getID(), () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
                if(!(te instanceof AdvancedCrafterTile))
                {
                    Main.logger.error("Wrong type of tile entity (expected AdvancedCrafterTile)!");
                    return null;
                }
                AdvancedCrafterTile tile = (AdvancedCrafterTile) te;
                return new AdvancedCrafterContainer(windowId, inv.player, tile);
            })));
        }
    }

    public static final RegistryObject<AdvancedExporter> ADVANCED_EXPORTER = BLOCKS.register("advanced_exporter", AdvancedExporter::new);
    public static final RegistryObject<Item> ADVANCED_EXPORTER_ITEM = ITEMS.register("advanced_exporter", () -> new BaseBlockItem(ADVANCED_EXPORTER.get(), globalProperties));
    public static final RegistryObject<TileEntityType<AdvancedExporterTile>> ADVANCED_EXPORTER_TILE = TILES.register("advanced_exporter", () -> TileEntityType.Builder.create(AdvancedExporterTile::new, ADVANCED_EXPORTER.get()).build(null));
    public static final RegistryObject<ContainerType<AdvancedExporterContainer>> ADVANCED_EXPORTER_CONTAINER = CONTAINERS.register("advanced_exporter", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
        if(!(te instanceof AdvancedExporterTile))
        {
            Main.logger.error("Wrong type of tile entity (expected AdvancedExporterTile)!");
            return null;
        }

        AdvancedExporterTile tile = (AdvancedExporterTile) te;
        return new AdvancedExporterContainer(windowId, inv.player, tile);
    }));

//    public static final RegistryObject<AdvancedExporter> TAG_EXPORTER = BLOCKS.register("tag_exporter", AdvancedExporter::new);
//    public static final RegistryObject<Item> TAG_EXPORTER_ITEM = ITEMS.register("tag_exporter", () -> new BlockItem(TAG_EXPORTER.get(), globalProperties));
//    public static final RegistryObject<TileEntityType<TagExporterTile>> TAG_EXPORTER_TILE = TILES.register("tag_exporter", () -> TileEntityType.Builder.create(TagExporterTile::new, TAG_EXPORTER.get()).build(null));
//    public static final RegistryObject<ContainerType<TagExporterContainer>> TAG_EXPORTER_CONTAINER = CONTAINERS.register("tag_exporter", () -> IForgeContainerType.create((windowId, inv, data) -> {
//        BlockPos pos = data.readBlockPos();
//        TileEntity te = inv.player.getEntityWorld().getTileEntity(pos);
//        if(!(te instanceof TagExporterTile))
//        {
//            Main.logger.error("Wrong type of tile entity (expected TagExporterTile)!");
//            return null;
//        }
//
//        TagExporterTile tile = (TagExporterTile) te;
//        return new TagExporterContainer(windowId, inv.player, tile);
//    }));
}