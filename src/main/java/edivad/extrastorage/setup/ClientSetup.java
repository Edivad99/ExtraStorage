package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import com.refinedmods.refinedstorage.render.model.FullbrightBakedModel;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedCrafterScreenQuark;
import edivad.extrastorage.client.screen.AdvancedExporterScreen;
import edivad.extrastorage.client.screen.AdvancedFluidStorageBlockScreen;
import edivad.extrastorage.client.screen.AdvancedImporterScreen;
import edivad.extrastorage.client.screen.AdvancedStorageBlockScreen;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSetup
{
    private static final BakedModelOverrideRegistry bakedModelOverrideRegistry = new BakedModelOverrideRegistry();

    public ClientSetup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModelBake);
    }

    public void init(FMLClientSetupEvent event)
    {
        //Version checker
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);

        boolean quarkLoaded = ModList.get().isLoaded("quark");

        //Special render & GUI
        for(CrafterTier tier : CrafterTier.values())
        {
            if(quarkLoaded)
                MenuScreens.register(Registration.CRAFTER_CONTAINER.get(tier).get(), AdvancedCrafterScreenQuark::new);
            else
                MenuScreens.register(Registration.CRAFTER_CONTAINER.get(tier).get(), AdvancedCrafterScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.CRAFTER_BLOCK.get(tier).get(), RenderType.cutout());

            String name = tier.name().toLowerCase();
            String parent = "blocks/crafter/" + name + "/cutouts/";
            bakedModelOverrideRegistry.add(Registration.CRAFTER_BLOCK.get(tier).getId(), (base, registry) -> new FullbrightBakedModel(
                    base,
                    true,
                    new ResourceLocation(Main.MODID, parent + "side_connected"),
                    new ResourceLocation(Main.MODID, parent + "top_connected")
            ));
        }

        MenuScreens.register(Registration.ADVANCED_EXPORTER_CONTAINER.get(), AdvancedExporterScreen::new);
        MenuScreens.register(Registration.ADVANCED_IMPORTER_CONTAINER.get(), AdvancedImporterScreen::new);

        ItemBlockRenderTypes.setRenderLayer(Registration.ADVANCED_EXPORTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.ADVANCED_IMPORTER.get(), RenderType.cutout());

        for(ItemStorageType type : ItemStorageType.values())
            MenuScreens.register(Registration.ITEM_STORAGE_CONTAINER.get(type).get(), AdvancedStorageBlockScreen::new);
        for(FluidStorageType type : FluidStorageType.values())
            MenuScreens.register(Registration.FLUID_STORAGE_CONTAINER.get(type).get(), AdvancedFluidStorageBlockScreen::new);

        API.instance().addPatternRenderHandler(pattern ->
        {
            AbstractContainerMenu container = Minecraft.getInstance().player.containerMenu;

            if (container instanceof AdvancedCrafterContainerMenu)
            {
                AdvancedCrafterContainerMenu actualContainer = (AdvancedCrafterContainerMenu) container;
                int slots = actualContainer.getBlockEntity().getTier().getSlots();
                for (int i = 0; i < slots; i++)
                    if (container.getSlot(i).getItem() == pattern)
                        return true;
            }

            return false;
        });
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent e) {
        FullbrightBakedModel.invalidateCache();

        for (ResourceLocation id : e.getModelRegistry().keySet()) {
            BakedModelOverrideRegistry.BakedModelOverrideFactory factory = this.bakedModelOverrideRegistry.get(new ResourceLocation(id.getNamespace(), id.getPath()));

            if (factory != null) {
                e.getModelRegistry().put(id, factory.create(e.getModelRegistry().get(id), e.getModelRegistry()));
            }
        }
    }

}