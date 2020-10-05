package edivad.extrastorage.client.screen.dataparameter;

import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.tile.data.TileDataParameterClientListener;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.custombutton.AdvancedCrafterModeSideButton;

public class AdvancedCrafterTileDataParameterClientListener implements TileDataParameterClientListener<Boolean>
{
    @Override
    public void onChanged(boolean initial, Boolean hasRoot)
    {
        if (!hasRoot)
        {
            BaseScreen.executeLater(AdvancedCrafterScreen.class, (gui) ->
            {
                gui.addSideButton(new AdvancedCrafterModeSideButton(gui));
            });
        }
    }
}
