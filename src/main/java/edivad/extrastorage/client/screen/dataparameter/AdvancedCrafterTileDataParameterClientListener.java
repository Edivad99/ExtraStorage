package edivad.extrastorage.client.screen.dataparameter;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationClientListener;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.AdvancedCrafterScreenQuark;
import edivad.extrastorage.client.screen.custombutton.AdvancedCrafterModeSideButton;
import net.minecraftforge.fml.ModList;

public class AdvancedCrafterTileDataParameterClientListener implements BlockEntitySynchronizationClientListener<Boolean>
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
