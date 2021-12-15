package edivad.extrastorage.client.screen.custombutton;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import com.refinedmods.refinedstorage.tile.data.TileDataManager;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;

public class AdvancedCrafterModeSideButton extends SideButton
{
    public AdvancedCrafterModeSideButton(BaseScreen<AdvancedCrafterContainer> screen) {
        super(screen);
    }

    @Override
    protected void renderButtonIcon(PoseStack poseStack, int x, int y) {
        this.screen.blit(poseStack, x, y, AdvancedCrafterBlockEntity.MODE.getValue() * 16, 0, 16, 16);
    }

    public void onPress() {
        TileDataManager.setParameter(AdvancedCrafterBlockEntity.MODE, AdvancedCrafterBlockEntity.MODE.getValue() + 1);
    }

    @Override
    public String getTooltip() {
        return I18n.get("sidebutton.refinedstorage.crafter_mode") + "\n" + ChatFormatting.GRAY + I18n.get("sidebutton.refinedstorage.crafter_mode." + AdvancedCrafterBlockEntity.MODE.getValue());
    }
}
