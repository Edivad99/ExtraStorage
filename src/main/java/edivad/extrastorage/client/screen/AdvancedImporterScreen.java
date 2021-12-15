package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.ExactModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.WhitelistBlacklistSideButton;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.container.AdvancedImporterContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedImporterScreen extends BaseScreen<AdvancedImporterContainer>
{
    public AdvancedImporterScreen(AdvancedImporterContainer container, Inventory inventory, Component title) {
        super(container, 211, 155, inventory, title);
    }

    @Override
    public void onPostInit(int x, int y)
    {
        addSideButton(new RedstoneModeSideButton(this, AdvancedImporterBlockEntity.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, AdvancedImporterBlockEntity.TYPE));
        addSideButton(new WhitelistBlacklistSideButton(this, AdvancedImporterBlockEntity.WHITELIST_BLACKLIST));
        addSideButton(new ExactModeSideButton(this, AdvancedImporterBlockEntity.COMPARE));
    }

    @Override
    public void tick(int x, int y)
    {
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
