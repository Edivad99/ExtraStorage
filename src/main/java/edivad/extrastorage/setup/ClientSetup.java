package edivad.extrastorage.setup;

import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry;
import com.refinedmods.refinedstorage.render.model.FullbrightBakedModel;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.*;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    private static final BakedModelOverrideRegistry BAKED_MODEL_OVERRIDE_REGISTRY = new BakedModelOverrideRegistry();

    public static void init(FMLClientSetupEvent event) {
        //Version checker
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        //Special render & GUI
        for(CrafterTier tier : CrafterTier.values())
        {
            MenuScreens.register(Registration.CRAFTER_CONTAINER.get(tier).get(), AdvancedCrafterScreen::new);
            ItemBlockRenderTypes.setRenderLayer(Registration.CRAFTER_BLOCK.get(tier).get(), RenderType.cutout());

            String name = tier.name().toLowerCase();
            String parent = "blocks/crafter/" + name + "/cutouts/";
            BAKED_MODEL_OVERRIDE_REGISTRY.add(Registration.CRAFTER_BLOCK.get(tier).getId(), (base, registry) -> new FullbrightBakedModel(
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
    public static void onModelBake(ModelBakeEvent e) {
        FullbrightBakedModel.invalidateCache();

        for (ResourceLocation id : e.getModelRegistry().keySet()) {
            BakedModelOverrideRegistry.BakedModelOverrideFactory factory = BAKED_MODEL_OVERRIDE_REGISTRY.get(new ResourceLocation(id.getNamespace(), id.getPath()));

            if (factory != null) {
                e.getModelRegistry().put(id, factory.create(e.getModelRegistry().get(id), e.getModelRegistry()));
            }
        }
    }

}