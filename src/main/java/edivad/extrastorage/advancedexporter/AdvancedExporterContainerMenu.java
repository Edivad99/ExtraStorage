package edivad.extrastorage.advancedexporter;

import java.util.function.Predicate;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.exporter.ExporterData;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.SchedulingModeType;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicator;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicatorListener;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicators;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.tools.AbstractAdvanceFilterContainerMenu;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AdvancedExporterContainerMenu extends AbstractAdvanceFilterContainerMenu<AdvancedExporterBlockEntity>
    implements ExportingIndicatorListener {

  private static final MutableComponent FILTER_HELP = IdentifierUtil.createTranslation("gui", "exporter.filter_help");

  private final ExportingIndicators indicators;
  private final Predicate<Player> stillValid;

  public AdvancedExporterContainerMenu(int windowId, Inventory inventory, ExporterData data) {
    super(ESContainer.ADVANCED_EXPORTER.get(), windowId, inventory.player, data.resourceContainerData(),
        UpgradeDestinations.EXPORTER, FILTER_HELP);
    this.indicators = new ExportingIndicators(data.exportingIndicators());
    this.stillValid = __ -> true;
  }

  public AdvancedExporterContainerMenu(int windowId, Player player, AdvancedExporterBlockEntity exporter,
      ResourceContainer resourceContainer, UpgradeContainer upgradeContainer, ExportingIndicators indicators) {
    super(ESContainer.ADVANCED_EXPORTER.get(), windowId, player, resourceContainer,
        upgradeContainer, exporter, FILTER_HELP);
    this.indicators = indicators;
    this.stillValid = p -> Container.stillValidBlockEntity(exporter, p);
  }

  public ExportingIndicator getIndicator(final int idx) {
    return indicators.get(idx);
  }

  public int getIndicators() {
    return indicators.size();
  }

  @Override
  public void broadcastChanges() {
    super.broadcastChanges();
    if (player instanceof ServerPlayer serverPlayer) {
      indicators.detectChanges(serverPlayer);
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid.test(player);
  }

  @Override
  protected void registerClientProperties() {
    registerProperty(new ClientProperty<>(PropertyTypes.FUZZY_MODE, false));
    registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
    registerProperty(new ClientProperty<>(PropertyTypes.SCHEDULING_MODE, SchedulingModeType.DEFAULT));
  }

  @Override
  protected void registerServerProperties(final AdvancedExporterBlockEntity blockEntity) {
    registerProperty(new ServerProperty<>(
        PropertyTypes.FUZZY_MODE,
        blockEntity::isFuzzyMode,
        blockEntity::setFuzzyMode
    ));
    registerProperty(new ServerProperty<>(
        PropertyTypes.REDSTONE_MODE,
        blockEntity::getRedstoneMode,
        blockEntity::setRedstoneMode
    ));
    registerProperty(new ServerProperty<>(
        PropertyTypes.SCHEDULING_MODE,
        blockEntity::getSchedulingModeType,
        blockEntity::setSchedulingModeType
    ));
  }

  @Override
  public void indicatorChanged(final int index, final ExportingIndicator indicator) {
    indicators.set(index, indicator);
  }
}
