package edivad.extrastorage.data.models;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ExtraStorageItemModelProvider extends ItemModelProvider {

  public ExtraStorageItemModelProvider(PackOutput packOutput,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, ExtraStorage.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    for (var type : ItemStorageType.values()) {
      singleTexture(getPath(ESItems.ITEM_STORAGE_PART.get(type).get()),
          mcLoc("item/generated"), "layer0", modLoc("item/parts/" + type.getName()));
      singleTexture(getPath(ESItems.ITEM_DISK.get(type).get()), mcLoc("item/generated"),
          "layer0", modLoc("item/disks/" + type.getName()));
      parentedBlock(ESBlocks.ITEM_STORAGE.get(type).get());
    }

    for (var type : FluidStorageType.values()) {
      singleTexture(getPath(ESItems.FLUID_STORAGE_PART.get(type).get()),
          mcLoc("item/generated"), "layer0", modLoc("item/parts/" + type.getName() + "_fluid"));
      singleTexture(getPath(ESItems.FLUID_DISK.get(type).get()), mcLoc("item/generated"),
          "layer0", modLoc("item/disks/" + type.getName() + "_fluid"));
      parentedBlock(ESBlocks.FLUID_STORAGE.get(type).get());
    }

    singleTexture(getPath(ESItems.RAW_NEURAL_PROCESSOR.get()), mcLoc("item/generated"),
        "layer0", modLoc("item/raw_neural_processor"));
    singleTexture(getPath(ESItems.NEURAL_PROCESSOR.get()), mcLoc("item/generated"),
        "layer0", modLoc("item/neural_processor"));
  }

  private void parentedBlock(Block block) {
    String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
    getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
  }

  private String getPath(Item item) {
    return ForgeRegistries.ITEMS.getKey(item).getPath();
  }
}
