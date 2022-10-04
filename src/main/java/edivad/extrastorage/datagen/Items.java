package edivad.extrastorage.datagen;

import edivad.extrastorage.Main;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var type : ItemStorageType.values()) {
            singleTexture(getPath(Registration.ITEM_STORAGE_PART.get(type).get()), mcLoc("item/generated"), "layer0", modLoc("items/parts/" + type.getName()));
            singleTexture(getPath(Registration.ITEM_DISK.get(type).get()), mcLoc("item/generated"), "layer0", modLoc("items/disks/" + type.getName()));
            parentedBlock(Registration.ITEM_STORAGE_BLOCK.get(type).get());
        }

        for (var type : FluidStorageType.values()) {
            singleTexture(getPath(Registration.FLUID_STORAGE_PART.get(type).get()), mcLoc("item/generated"), "layer0", modLoc("items/parts/" + type.getName() + "_fluid"));
            singleTexture(getPath(Registration.FLUID_DISK.get(type).get()), mcLoc("item/generated"), "layer0", modLoc("items/disks/" + type.getName() + "_fluid"));
            parentedBlock(Registration.FLUID_STORAGE_BLOCK.get(type).get());
        }

        singleTexture(getPath(Registration.RAW_NEURAL_PROCESSOR_ITEM.get()), mcLoc("item/generated"), "layer0", modLoc("items/raw_neural_processor"));
        singleTexture(getPath(Registration.NEURAL_PROCESSOR_ITEM.get()), mcLoc("item/generated"), "layer0", modLoc("items/neural_processor"));
    }

    private void parentedBlock(Block block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)));
    }

    private String getPath(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }
}
