package edivad.extrastorage.datagen;

import edivad.extrastorage.Main;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider
{
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, Main.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        for(ItemStorageType type : ItemStorageType.values())
            simpleBlock(Registration.ITEM_STORAGE_BLOCK.get(type).get(), models().cubeAll("block_" + type.getName(), modLoc("blocks/storage/" + type.getName() + "_storage_block")));
        for(FluidStorageType type : FluidStorageType.values())
            simpleBlock(Registration.FLUID_STORAGE_BLOCK.get(type).get(), models().cubeAll("block_" + type.getName() + "_fluid", modLoc("blocks/storage/" + type.getName() + "_fluid_storage_block")));
    }
}
