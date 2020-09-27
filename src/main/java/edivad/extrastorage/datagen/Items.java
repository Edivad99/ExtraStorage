package edivad.extrastorage.datagen;

import edivad.extrastorage.Main;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider
{

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for(ItemStorageType type : ItemStorageType.values())
        {
            singleTexture(Registration.ITEM_STORAGE_PART.get(type).get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc(String.format("items/parts/{0}", type.getName())));
            singleTexture(Registration.ITEM_DISK.get(type).get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc(String.format("items/disks/{0}", type.getName())));
            parentedBlock(Registration.ITEM_STORAGE_BLOCK.get(type).get());
        }

        for(FluidStorageType type : FluidStorageType.values())
        {
            singleTexture(Registration.FLUID_STORAGE_PART.get(type).get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc(String.format("items/parts/{0}_fluid", type.getName())));
            singleTexture(Registration.FLUID_DISK.get(type).get().getRegistryName().getPath(), mcLoc("item/generated"), "layer0", modLoc(String.format("items/disks/{0}_fluid", type.getName())));
            parentedBlock(Registration.FLUID_STORAGE_BLOCK.get(type).get());
        }
    }

    private void parentedBlock(Block block)
    {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }
}
