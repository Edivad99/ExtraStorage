package edivad.extrastorage.data;

import java.util.concurrent.CompletableFuture;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.ESItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ExtraStorageItemTagsProvider extends ItemTagsProvider {

  public ExtraStorageItemTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      CompletableFuture<TagLookup<Block>> blockTagProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, blockTagProvider, ExtraStorage.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    var itemPartsBuilder = this.tag(ExtraStorageTags.Items.ITEM_PARTS);
    var itemDisksBuilder = this.tag(ExtraStorageTags.Items.ITEM_DISKS);
    for (var type : ItemStorageType.values()) {
      var tag = ExtraStorageTags.Items.PARTS_ITEM.get(type);
      this.tag(tag).add(ESItems.ITEM_STORAGE_PART.get(type).get());
      itemPartsBuilder.addTag(tag);

      tag = ExtraStorageTags.Items.DISKS_ITEM.get(type);
      this.tag(tag).add(ESItems.ITEM_DISK.get(type).get());
      itemDisksBuilder.addTag(tag);
    }

    var fluidPartsBuilder = this.tag(ExtraStorageTags.Items.FLUID_PARTS);
    var fluidDisksBuilder = this.tag(ExtraStorageTags.Items.FLUID_DISKS);
    for (var type : FluidStorageType.values()) {
      var tag = ExtraStorageTags.Items.PARTS_FLUID.get(type);
      this.tag(tag).add(ESItems.FLUID_STORAGE_PART.get(type).get());
      fluidPartsBuilder.addTag(tag);

      tag = ExtraStorageTags.Items.DISKS_FLUID.get(type);
      this.tag(tag).add(ESItems.FLUID_DISK.get(type).get());
      fluidDisksBuilder.addTag(tag);
    }

    this.tag(ExtraStorageTags.Items.PARTS)
        .addTags(ExtraStorageTags.Items.ITEM_PARTS, ExtraStorageTags.Items.FLUID_PARTS);
    this.tag(ExtraStorageTags.Items.DISKS)
        .addTags(ExtraStorageTags.Items.ITEM_DISKS, ExtraStorageTags.Items.FLUID_DISKS);

    // blocks
    this.copy(ExtraStorageTags.Blocks.ITEM_STORAGE_BLOCKS,
        ExtraStorageTags.Items.ITEM_STORAGE_BLOCKS);
    for (var type : ItemStorageType.values()) {
      this.copy(ExtraStorageTags.Blocks.STORAGE_BLOCKS_ITEM.get(type),
          ExtraStorageTags.Items.STORAGE_BLOCKS_ITEM.get(type));
    }

    this.copy(ExtraStorageTags.Blocks.FLUID_STORAGE_BLOCKS,
        ExtraStorageTags.Items.FLUID_STORAGE_BLOCKS);
    for (var type : FluidStorageType.values()) {
      this.copy(ExtraStorageTags.Blocks.STORAGE_BLOCKS_FLUID.get(type),
          ExtraStorageTags.Items.STORAGE_BLOCKS_FLUID.get(type));
    }

    this.copy(ExtraStorageTags.Blocks.STORAGE_BLOCKS, ExtraStorageTags.Items.STORAGE_BLOCKS);
  }
}
