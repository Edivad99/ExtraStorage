package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.container.AdvancedExporterContainer;
import edivad.extrastorage.tiles.AdvancedExporterTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AdvancedExporterScreen extends BaseScreen<AdvancedExporterContainer>
{
    private boolean hasRegulatorMode;

    public AdvancedExporterScreen(AdvancedExporterContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, 211, 155, inventory, title);
        this.hasRegulatorMode = hasRegulatorMode();
    }

    private boolean hasRegulatorMode()
    {
        return container.getTile().getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);
    }

    @Override
    public void onPostInit(int x, int y)
    {
        addSideButton(new RedstoneModeSideButton(this, AdvancedExporterTile.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, AdvancedExporterTile.TYPE));
        addSideButton(new ExactModeSideButton(this, AdvancedExporterTile.COMPARE));
    }

    @Override
    public void tick(int x, int y)
    {
        boolean updatedHasRegulatorMode = hasRegulatorMode();
        if (hasRegulatorMode != updatedHasRegulatorMode)
        {
            hasRegulatorMode = updatedHasRegulatorMode;
            container.initSlots();
        }
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY)
    {
        bindTexture(Main.MODID, "gui/advanced_exporter.png");
        blit(matrixStack, x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void renderForeground(MatrixStack matrixStack, int i, int i1)
    {
        renderString(matrixStack, 7, 7, RenderUtils.shorten(title.getString(), 26));
        renderString(matrixStack, 7, 60, new TranslationTextComponent("container.inventory").getString());
    }
}
