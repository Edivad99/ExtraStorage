package edivad.extrastorage.advancedimporter;

import com.refinedmods.refinedstorage.common.storage.FilterModeSideButtonWidget;
import com.refinedmods.refinedstorage.common.support.AbstractFilterScreen;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.widget.FuzzyModeSideButtonWidget;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.ExtraStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedImporterScreen extends AbstractFilterScreen<AdvancedImporterContainerMenu> {

  private static final ResourceLocation TEXTURE =
      ExtraStorage.rl("textures/gui/advanced_exporter_importer.png");

  public AdvancedImporterScreen(AdvancedImporterContainerMenu menu, Inventory inventory,
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
    addSideButton(new FilterModeSideButtonWidget(
        getMenu().getProperty(PropertyTypes.FILTER_MODE),
        IdentifierUtil.createTranslation("gui", "importer.filter_mode.allow.help"),
        IdentifierUtil.createTranslation("gui", "importer.filter_mode.block.help")
    ));
    addSideButton(new FuzzyModeSideButtonWidget(
        getMenu().getProperty(PropertyTypes.FUZZY_MODE),
        () -> FuzzyModeSideButtonWidget.Type.EXTRACTING_SOURCE
    ));
  }
}
