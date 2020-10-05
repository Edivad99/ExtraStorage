package edivad.extrastorage.client.screen.custombutton;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import com.refinedmods.refinedstorage.tile.CrafterTile;
import com.refinedmods.refinedstorage.tile.data.TileDataManager;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class AdvancedCrafterModeSideButton extends SideButton
{
    public AdvancedCrafterModeSideButton(BaseScreen<AdvancedCrafterContainer> screen)
    {
        super(screen);
    }

    @Override
    protected void renderButtonIcon(MatrixStack matrixStack, int x, int y)
    {
        this.screen.blit(matrixStack, x, y, AdvancedCrafterTile.MODE.getValue() * 16, 0, 16, 16);
    }

    public void onPress() {
        TileDataManager.setParameter(AdvancedCrafterTile.MODE, AdvancedCrafterTile.MODE.getValue() + 1);
    }

    @Override
    public String getTooltip()
    {
        return I18n.format("sidebutton.refinedstorage.crafter_mode", new Object[0]) + "\n" + TextFormatting.GRAY + I18n.format("sidebutton.refinedstorage.crafter_mode." + AdvancedCrafterTile.MODE.getValue(), new Object[0]);
    }
}
