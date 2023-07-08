package edivad.extrastorage.data.models;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.ESBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ExtraStorageBlockModelProvider extends BlockStateProvider {

  public ExtraStorageBlockModelProvider(PackOutput packOutput,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, ExtraStorage.MODID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    for (var type : ItemStorageType.values()) {
      var model = models().cubeAll("block_" + type.getName(),
          modLoc("block/storage/" + type.getName() + "_storage_block"));
      simpleBlock(ESBlocks.ITEM_STORAGE.get(type).get(), model);
    }
    for (var type : FluidStorageType.values()) {
      var model = models().cubeAll("block_" + type.getName() + "_fluid",
          modLoc("block/storage/" + type.getName() + "_fluid_storage_block"));
      simpleBlock(ESBlocks.FLUID_STORAGE.get(type).get(), model);
    }
  }
}
