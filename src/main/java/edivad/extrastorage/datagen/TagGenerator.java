package edivad.extrastorage.datagen;

import edivad.extrastorage.Main;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class TagGenerator {
    public static class Blocks {
        public static final TagKey<Block> STORAGE_BLOCKS = tag("storage_blocks");
        public static final TagKey<Block> ITEM_STORAGE_BLOCKS = tag("storage_blocks/items");
        public static final TagKey<Block> FLUID_STORAGE_BLOCKS = tag("storage_blocks/fluids");
        public static final Map<ItemStorageType, TagKey<Block>> STORAGE_BLOCKS_ITEM = new HashMap<>();
        public static final Map<FluidStorageType, TagKey<Block>> STORAGE_BLOCKS_FLUID = new HashMap<>();

        static {
            for (var type : ItemStorageType.values()) {
                STORAGE_BLOCKS_ITEM.put(type, tag("storage_blocks/items/" + type.getName()));
            }
            for (var type : FluidStorageType.values()) {
                STORAGE_BLOCKS_FLUID.put(type, tag("storage_blocks/fluids/" + type.getName()));
            }
        }

        private static TagKey<Block> tag(String name) {
            return net.minecraft.tags.BlockTags.create(new ResourceLocation("refinedstorage", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> STORAGE_BLOCKS = tag("storage_blocks");
        public static final TagKey<Item> ITEM_STORAGE_BLOCKS = tag("storage_blocks/items");
        public static final TagKey<Item> FLUID_STORAGE_BLOCKS = tag("storage_blocks/fluids");
        public static final Map<ItemStorageType, TagKey<Item>> STORAGE_BLOCKS_ITEM = new HashMap<>();
        public static final Map<FluidStorageType, TagKey<Item>> STORAGE_BLOCKS_FLUID = new HashMap<>();

        public static final TagKey<Item> PARTS = tag("parts");
        public static final TagKey<Item> ITEM_PARTS = tag("parts/items");
        public static final TagKey<Item> FLUID_PARTS = tag("parts/fluids");
        public static final Map<ItemStorageType, TagKey<Item>> PARTS_ITEM = new HashMap<>();
        public static final Map<FluidStorageType, TagKey<Item>> PARTS_FLUID = new HashMap<>();

        public static final TagKey<Item> DISKS = tag("disks");
        public static final TagKey<Item> ITEM_DISKS = tag("disks/items");
        public static final TagKey<Item> FLUID_DISKS = tag("disks/fluids");
        public static final Map<ItemStorageType, TagKey<Item>> DISKS_ITEM = new HashMap<>();
        public static final Map<FluidStorageType, TagKey<Item>> DISKS_FLUID = new HashMap<>();

        static {
            for (var type : ItemStorageType.values()) {
                STORAGE_BLOCKS_ITEM.put(type, tag("storage_blocks/items/" + type.getName()));
                PARTS_ITEM.put(type, tag("parts/items/" + type.getName()));
                DISKS_ITEM.put(type, tag("disks/items/" + type.getName()));
            }
            for (var type : FluidStorageType.values()) {
                STORAGE_BLOCKS_FLUID.put(type, tag("storage_blocks/fluids/" + type.getName()));
                PARTS_FLUID.put(type, tag("parts/fluids/" + type.getName()));
                DISKS_FLUID.put(type, tag("disks/fluids/" + type.getName()));
            }
        }

        private static TagKey<Item> tag(String name) {
            return net.minecraft.tags.ItemTags.create(new ResourceLocation("refinedstorage", name));
        }
    }

    public static class BlockTags extends BlockTagsProvider {
        public BlockTags(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, Main.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            var itemBlocksBuilder = this.tag(Blocks.ITEM_STORAGE_BLOCKS);
            for (var type : ItemStorageType.values()) {
                var tag = Blocks.STORAGE_BLOCKS_ITEM.get(type);
                this.tag(tag).add(Registration.ITEM_STORAGE_BLOCK.get(type).get());
                itemBlocksBuilder.addTag(tag);
            }

            var fluidBlocksBuilder = this.tag(Blocks.FLUID_STORAGE_BLOCKS);
            for (var type : FluidStorageType.values()) {
                var tag = Blocks.STORAGE_BLOCKS_FLUID.get(type);
                this.tag(tag).add(Registration.FLUID_STORAGE_BLOCK.get(type).get());
                fluidBlocksBuilder.addTag(tag);
            }

            //noinspection unchecked
            this.tag(Blocks.STORAGE_BLOCKS).addTags(Blocks.ITEM_STORAGE_BLOCKS, Blocks.FLUID_STORAGE_BLOCKS);
        }
    }

    public static class ItemTags extends ItemTagsProvider {
        public ItemTags(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
            super(generator, provider, Main.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            var itemPartsBuilder = this.tag(Items.ITEM_PARTS);
            var itemDisksBuilder = this.tag(Items.ITEM_DISKS);
            for (var type : ItemStorageType.values()) {
                var tag = Items.PARTS_ITEM.get(type);
                this.tag(tag).add(Registration.ITEM_STORAGE_PART.get(type).get());
                itemPartsBuilder.addTag(tag);

                tag = Items.DISKS_ITEM.get(type);
                this.tag(tag).add(Registration.ITEM_DISK.get(type).get());
                itemDisksBuilder.addTag(tag);
            }

            var fluidPartsBuilder = this.tag(Items.FLUID_PARTS);
            var fluidDisksBuilder = this.tag(Items.FLUID_DISKS);
            for (var type : FluidStorageType.values()) {
                var tag = Items.PARTS_FLUID.get(type);
                this.tag(tag).add(Registration.FLUID_STORAGE_PART.get(type).get());
                fluidPartsBuilder.addTag(tag);

                tag = Items.DISKS_FLUID.get(type);
                this.tag(tag).add(Registration.FLUID_DISK.get(type).get());
                fluidDisksBuilder.addTag(tag);
            }

            //noinspection unchecked
            this.tag(Items.PARTS).addTags(Items.ITEM_PARTS, Items.FLUID_PARTS);
            //noinspection unchecked
            this.tag(Items.DISKS).addTags(Items.ITEM_DISKS, Items.FLUID_DISKS);

            // blocks
            this.copy(Blocks.ITEM_STORAGE_BLOCKS, Items.ITEM_STORAGE_BLOCKS);
            for (var type : ItemStorageType.values()) {
                this.copy(Blocks.STORAGE_BLOCKS_ITEM.get(type), Items.STORAGE_BLOCKS_ITEM.get(type));
            }

            this.copy(Blocks.FLUID_STORAGE_BLOCKS, Items.FLUID_STORAGE_BLOCKS);
            for (var type : FluidStorageType.values()) {
                this.copy(Blocks.STORAGE_BLOCKS_FLUID.get(type), Items.STORAGE_BLOCKS_FLUID.get(type));
            }

            this.copy(Blocks.STORAGE_BLOCKS, Items.STORAGE_BLOCKS);
        }
    }
}
