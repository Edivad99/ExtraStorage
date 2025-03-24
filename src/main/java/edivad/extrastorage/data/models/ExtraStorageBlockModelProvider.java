package edivad.extrastorage.data.models;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ExtraStorageBlockModelProvider extends BlockStateProvider {

  public ExtraStorageBlockModelProvider(PackOutput packOutput,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, ExtraStorage.ID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    for (var type : AdvancedItemStorageVariant.values()) {
      var model = models().cubeAll("block_" + type.getName(),
          modLoc("block/storage/" + type.getName() + "_storage_block"));
      simpleBlock(ESBlocks.ITEM_STORAGE.get(type).get(), model);
    }
    for (var type : AdvancedFluidStorageVariant.values()) {
      var model = models().cubeAll("block_" + type.getName() + "_fluid",
          modLoc("block/storage/" + type.getName() + "_fluid_storage_block"));
      simpleBlock(ESBlocks.FLUID_STORAGE.get(type).get(), model);
    }
  }
}
