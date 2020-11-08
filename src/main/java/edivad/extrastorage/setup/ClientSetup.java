package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import com.refinedmods.refinedstorage.render.model.FullbrightBakedModel;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.AdvancedImporter;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedExporterScreen;
import edivad.extrastorage.client.screen.AdvancedFluidStorageBlockScreen;
import edivad.extrastorage.client.screen.AdvancedImporterScreen;
import edivad.extrastorage.client.screen.AdvancedStorageBlockScreen;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup
{
    private static final BakedModelOverrideRegistry bakedModelOverrideRegistry = new BakedModelOverrideRegistry();

    public static void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        //Special render & GUI
        for(CrafterTier tier : CrafterTier.values())
        {
            ScreenManager.registerFactory(Registration.CRAFTER_CONTAINER.get(tier).get(), AdvancedCrafterScreen::new);
            RenderTypeLookup.setRenderLayer(Registration.CRAFTER_BLOCK.get(tier).get(), RenderType.getCutout());

            String name = tier.name().toLowerCase();
            String parent = "blocks/crafter/" + name + "/cutouts/";
            bakedModelOverrideRegistry.add(Registration.CRAFTER_BLOCK.get(tier).getId(), (base, registry) -> new FullbrightBakedModel(
                    base,
                    true,
                    new ResourceLocation(Main.MODID, parent + "side_connected"),
                    new ResourceLocation(Main.MODID, parent + "top_connected")
            ));
        }

        ScreenManager.registerFactory(Registration.ADVANCED_EXPORTER_CONTAINER.get(), AdvancedExporterScreen::new);
        ScreenManager.registerFactory(Registration.ADVANCED_IMPORTER_CONTAINER.get(), AdvancedImporterScreen::new);

        RenderTypeLookup.setRenderLayer(Registration.ADVANCED_EXPORTER.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(Registration.ADVANCED_IMPORTER.get(), RenderType.getCutout());

        for(ItemStorageType type : ItemStorageType.values())
            ScreenManager.registerFactory(Registration.ITEM_STORAGE_CONTAINER.get(type).get(), AdvancedStorageBlockScreen::new);
        for(FluidStorageType type : FluidStorageType.values())
            ScreenManager.registerFactory(Registration.FLUID_STORAGE_CONTAINER.get(type).get(), AdvancedFluidStorageBlockScreen::new);

        API.instance().addPatternRenderHandler(pattern ->
        {
            Container container = Minecraft.getInstance().player.openContainer;

            if (container instanceof AdvancedCrafterContainer)
            {
                AdvancedCrafterContainer actualContainer = (AdvancedCrafterContainer) container;
                int slots = actualContainer.getTile().getTier().getSlots();
                for (int i = 0; i < slots; i++)
                    if (container.getSlot(i).getStack() == pattern)
                        return true;
            }

            return false;
        });
    }
}