package edivad.extrastorage.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        TagGenerator.BlockTags blockTagsProvider = new TagGenerator.BlockTags(generator, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(),new TagGenerator.ItemTags(generator, blockTagsProvider, existingFileHelper));
        generator.addProvider(event.includeServer(),new Recipes(generator));
        generator.addProvider(event.includeServer(),new Lang(generator));
        generator.addProvider(event.includeServer(),new LootTableGenerator(generator));
        generator.addProvider(event.includeClient(), new BlockStates(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new Items(generator, existingFileHelper));
    }
}
