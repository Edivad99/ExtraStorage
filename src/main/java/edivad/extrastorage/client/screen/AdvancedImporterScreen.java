package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.WhitelistBlacklistSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.container.AdvancedImporterContainer;
import edivad.extrastorage.tiles.AdvancedImporterTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AdvancedImporterScreen extends BaseScreen<AdvancedImporterContainer>
{
    public AdvancedImporterScreen(AdvancedImporterContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, 211, 155, inventory, title);
    }

    @Override
    public void onPostInit(int x, int y)
    {
        addSideButton(new RedstoneModeSideButton(this, AdvancedImporterTile.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, AdvancedImporterTile.TYPE));
        addSideButton(new WhitelistBlacklistSideButton(this, AdvancedImporterTile.WHITELIST_BLACKLIST));
        addSideButton(new ExactModeSideButton(this, AdvancedImporterTile.COMPARE));
    }

    @Override
    public void tick(int x, int y)
    {
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY)
    {
        bindTexture(Main.MODID, "gui/advanced_exporter_importer.png");
        blit(matrixStack, x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void renderForeground(MatrixStack matrixStack, int i, int i1)
    {
        renderString(matrixStack, 7, 7, RenderUtils.shorten(title.getString(), 26));
        renderString(matrixStack, 7, 60, new TranslationTextComponent("container.inventory").getString());
    }
}
