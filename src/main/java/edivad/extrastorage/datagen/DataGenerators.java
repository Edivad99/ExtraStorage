package edivad.extrastorage.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if(event.includeServer()) {
            Tag.BlockTags blockTagsProvider = new Tag.BlockTags(generator, existingFileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new Tag.ItemTags(generator, blockTagsProvider, existingFileHelper));
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new Lang(generator));
        }
        if(event.includeClient()) {
            generator.addProvider(new BlockStates(generator, existingFileHelper));
            generator.addProvider(new Items(generator, existingFileHelper));
        }
    }
}
