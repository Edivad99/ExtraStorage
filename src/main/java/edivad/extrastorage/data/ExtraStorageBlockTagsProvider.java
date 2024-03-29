package edivad.extrastorage.data;

import java.util.concurrent.CompletableFuture;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.ESBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ExtraStorageBlockTagsProvider extends BlockTagsProvider {

  public ExtraStorageBlockTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, ExtraStorage.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    var itemBlocksBuilder = this.tag(ExtraStorageTags.Blocks.ITEM_STORAGE_BLOCKS);
    for (var type : ItemStorageType.values()) {
      var tag = ExtraStorageTags.Blocks.STORAGE_BLOCKS_ITEM.get(type);
      this.tag(tag).add(ESBlocks.ITEM_STORAGE.get(type).get());
      itemBlocksBuilder.addTag(tag);
    }

    var fluidBlocksBuilder = this.tag(ExtraStorageTags.Blocks.FLUID_STORAGE_BLOCKS);
    for (var type : FluidStorageType.values()) {
      var tag = ExtraStorageTags.Blocks.STORAGE_BLOCKS_FLUID.get(type);
      this.tag(tag).add(ESBlocks.FLUID_STORAGE.get(type).get());
      fluidBlocksBuilder.addTag(tag);
    }

    var crafterBuilder = this.tag(ExtraStorageTags.Blocks.CRAFTER);
    for (var tier : CrafterTier.values()) {
      crafterBuilder.add(ESBlocks.CRAFTER.get(tier).get());
    }

    //noinspection unchecked
    this.tag(ExtraStorageTags.Blocks.STORAGE_BLOCKS)
        .addTags(ExtraStorageTags.Blocks.ITEM_STORAGE_BLOCKS,
            ExtraStorageTags.Blocks.FLUID_STORAGE_BLOCKS);

    this.tag(ExtraStorageTags.Blocks.CARRY_ON_BLACKLIST)
        .add(ESBlocks.ADVANCED_EXPORTER.get())
        .add(ESBlocks.ADVANCED_IMPORTER.get())
        .addTag(ExtraStorageTags.Blocks.STORAGE_BLOCKS)
        .addTag(ExtraStorageTags.Blocks.CRAFTER);
    this.tag(ExtraStorageTags.Blocks.MEKANISM_BLACKLIST)
        .addTag(ExtraStorageTags.Blocks.CARRY_ON_BLACKLIST);
    this.tag(ExtraStorageTags.Blocks.RELOCATION_NOT_SUPPORTED)
        .addTag(ExtraStorageTags.Blocks.CARRY_ON_BLACKLIST);
  }
}
