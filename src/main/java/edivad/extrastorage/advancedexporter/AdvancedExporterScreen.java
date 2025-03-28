package edivad.extrastorage.advancedexporter;

import com.refinedmods.refinedstorage.common.support.AbstractFilterScreen;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.widget.FuzzyModeSideButtonWidget;
import com.refinedmods.refinedstorage.common.support.widget.SchedulingModeSideButtonWidget;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedExporterScreen extends AbstractFilterScreen<AdvancedExporterContainerMenu> {

  private static final ResourceLocation TEXTURE =
      ExtraStorage.rl("textures/gui/advanced_exporter_importer.png");

  public AdvancedExporterScreen(AdvancedExporterContainerMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.inventoryLabelY = 60;
    this.imageHeight = 211;
  }

  @Override
  protected ResourceLocation getTexture() {
    return TEXTURE;
  }

  @Override
  protected void init() {
    super.init();
    addSideButton(new FuzzyModeSideButtonWidget(
        getMenu().getProperty(PropertyTypes.FUZZY_MODE),
        () -> FuzzyModeSideButtonWidget.Type.EXTRACTING_STORAGE_NETWORK
    ));
    addSideButton(new SchedulingModeSideButtonWidget(getMenu().getProperty(PropertyTypes.SCHEDULING_MODE)));
  }

  @Override
  protected void renderTooltip(final GuiGraphics graphics, final int x, final int y) {
    if (renderExportingIndicators(graphics, leftPos, topPos, x, y, getMenu().getIndicators(),
        getMenu()::getIndicator)) {
      return;
    }
    super.renderTooltip(graphics, x, y);
  }
}
