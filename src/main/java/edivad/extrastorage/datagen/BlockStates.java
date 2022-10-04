package edivad.extrastorage.datagen;

import edivad.extrastorage.Main;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Main.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (var type : ItemStorageType.values()) {
            var model = models().cubeAll("block_" + type.getName(), modLoc("blocks/storage/" + type.getName() + "_storage_block"));
            simpleBlock(Registration.ITEM_STORAGE_BLOCK.get(type).get(), model);
        }
        for (var type : FluidStorageType.values()) {
            var model = models().cubeAll("block_" + type.getName() + "_fluid", modLoc("blocks/storage/" + type.getName() + "_fluid_storage_block"));
            simpleBlock(Registration.FLUID_STORAGE_BLOCK.get(type).get(), model);
        }
    }
}
