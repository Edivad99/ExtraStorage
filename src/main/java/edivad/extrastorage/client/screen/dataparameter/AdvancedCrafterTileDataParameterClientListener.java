package edivad.extrastorage.client.screen.dataparameter;

import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.tile.data.TileDataParameterClientListener;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedCrafterScreenQuark;
import edivad.extrastorage.client.screen.custombutton.AdvancedCrafterModeSideButton;
import edivad.extrastorage.compat.TOPIntegration;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class AdvancedCrafterTileDataParameterClientListener implements TileDataParameterClientListener<Boolean>
{
    @Override
    public void onChanged(boolean initial, Boolean hasRoot)
    {
        if (!hasRoot)
        {
            boolean quarkLoaded = ModList.get().isLoaded("quark");
            Class<? extends BaseScreen> clazz = quarkLoaded ? AdvancedCrafterScreenQuark.class : AdvancedCrafterScreen.class;

            BaseScreen.executeLater(clazz, gui -> gui.addSideButton(new AdvancedCrafterModeSideButton(gui)));
        }
    }
}
