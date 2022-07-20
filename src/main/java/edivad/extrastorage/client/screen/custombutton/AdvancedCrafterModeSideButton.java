package edivad.extrastorage.client.screen.custombutton;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;

public class AdvancedCrafterModeSideButton extends SideButton
{
    private final AdvancedCrafterBlockEntity blockEntity;

    public AdvancedCrafterModeSideButton(BaseScreen<AdvancedCrafterContainerMenu> screen, AdvancedCrafterBlockEntity blockEntity) {
        super(screen);
        this.blockEntity = blockEntity;
    }

    @Override
    protected void renderButtonIcon(PoseStack poseStack, int x, int y) {
        this.screen.blit(poseStack, x, y, blockEntity.getCrafterMode().ordinal() * 16, 0, 16, 16);
    }

    @Override
    public String getTooltip() {
        return I18n.get("sidebutton.refinedstorage.crafter_mode") + "\n" + ChatFormatting.GRAY + I18n.get("sidebutton.refinedstorage.crafter_mode." + blockEntity.getCrafterMode().ordinal());
    }
}
