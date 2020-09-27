package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AdvancedCrafterScreen extends BaseScreen<AdvancedCrafterContainer>
{
    private final CrafterTier tier;

    public AdvancedCrafterScreen(AdvancedCrafterContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, 211, 173 + (container.getTile().getTier().ordinal() * 36), inventory, title);
        this.tier = container.getTile().getTier();
    }

    @Override
    public void onPostInit(int i, int i1)
    {
    }

    @Override
    public void tick(int i, int i1)
    {
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY)
    {
        bindTexture(Main.MODID, "gui/" + tier.getID() + ".png");
        if(ySize <= 256)
            blit(matrixStack, x, y, 0, 0, xSize, ySize);
        else
            blit(matrixStack, x, y, 0, 0, xSize, ySize, 512, 512);
    }

    @Override
    public void renderForeground(MatrixStack matrixStack, int i, int i1)
    {
        renderString(matrixStack, 7, 7, RenderUtils.shorten(title.getString(), 26));
        renderString(matrixStack, 7, 78 + (36 * tier.ordinal()), new TranslationTextComponent("container.inventory").getString());
    }
}
