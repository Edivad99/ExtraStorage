package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.container.AdvancedExporterContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedExporterScreen extends BaseScreen<AdvancedExporterContainer>
{
    private boolean hasRegulatorMode;

    public AdvancedExporterScreen(AdvancedExporterContainer container, Inventory inventory, Component title)
    {
        super(container, 211, 155, inventory, title);
        this.hasRegulatorMode = hasRegulatorMode();
    }

    private boolean hasRegulatorMode()
    {
        return menu.getTile().getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);
    }

    @Override
    public void onPostInit(int x, int y)
    {
        addSideButton(new RedstoneModeSideButton(this, AdvancedExporterBlockEntity.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, AdvancedExporterBlockEntity.TYPE));
        addSideButton(new ExactModeSideButton(this, AdvancedExporterBlockEntity.COMPARE));
    }

    @Override
    public void tick(int x, int y)
    {
        boolean updatedHasRegulatorMode = hasRegulatorMode();
        if (hasRegulatorMode != updatedHasRegulatorMode)
        {
            hasRegulatorMode = updatedHasRegulatorMode;
            menu.initSlots();
        }
    }

    @Override
    public void renderBackground(PoseStack poseStack, int x, int y, int mouseX, int mouseY)
    {
        bindTexture(Main.MODID, "gui/advanced_exporter_importer.png");
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int i, int i1)
    {
        renderString(poseStack, 7, 7, RenderUtils.shorten(title.getString(), 26));
        renderString(poseStack, 7, 60, new TranslatableComponent("container.inventory").getString());
    }
}
