package edivad.extrastorage.advancedexporter;

import com.refinedmods.refinedstorage.common.support.AbstractFilterScreen;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.widget.FuzzyModeSideButtonWidget;
import com.refinedmods.refinedstorage.common.support.widget.SchedulingModeSideButtonWidget;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedExporterScreen extends AbstractFilterScreen<AdvancedExporterContainerMenu> {

  private static final Identifier TEXTURE =
      ExtraStorage.rl("textures/gui/advanced_exporter_importer.png");

  public AdvancedExporterScreen(AdvancedExporterContainerMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title, true);
    this.inventoryLabelY = 60;
    this.imageHeight = 211;
  }

  @Override
  protected Identifier getTexture() {
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
  protected void extractTooltip(final GuiGraphicsExtractor graphics, final int x, final int y) {
    if (renderExportingIndicators(font, graphics, leftPos, topPos, x, y, getMenu().getIndicators(),
        getMenu()::getIndicator)) {
      return;
    }
    super.extractTooltip(graphics, x, y);
  }
}
