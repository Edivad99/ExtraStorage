package edivad.extrastorage.client.screen.dataparameter;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationClientListener;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import edivad.extrastorage.client.screen.AdvancedCrafterScreen;
import edivad.extrastorage.client.screen.custombutton.AdvancedCrafterModeSideButton;

public class AdvancedCrafterTileDataParameterClientListener implements BlockEntitySynchronizationClientListener<Boolean> {
    @Override
    public void onChanged(boolean initial, Boolean hasRoot) {
        if (!hasRoot) {
            BaseScreen.executeLater(AdvancedCrafterScreen.class, screen -> screen.addSideButton(new AdvancedCrafterModeSideButton(screen)));
        }
    }
}
