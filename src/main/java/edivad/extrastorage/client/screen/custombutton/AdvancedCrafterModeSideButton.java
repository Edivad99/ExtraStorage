package edivad.extrastorage.client.screen.custombutton;

import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;

public class AdvancedCrafterModeSideButton extends SideButton {

  public AdvancedCrafterModeSideButton(BaseScreen<AdvancedCrafterContainerMenu> screen) {
    super(screen);
  }

  @Override
  protected void renderButtonIcon(GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.blit(BaseScreen.ICONS_TEXTURE, x, y,
        AdvancedCrafterBlockEntity.MODE.getValue() * 16, 0, 16, 16);
  }

  public void onPress() {
    BlockEntitySynchronizationManager.setParameter(AdvancedCrafterBlockEntity.MODE,
        AdvancedCrafterBlockEntity.MODE.getValue() + 1);
  }

  @Override
  protected String getSideButtonTooltip() {
    return I18n.get("sidebutton.refinedstorage.crafter_mode") + "\n" + ChatFormatting.GRAY
        + I18n.get(
        "sidebutton.refinedstorage.crafter_mode." + AdvancedCrafterBlockEntity.MODE.getValue());
  }
}
