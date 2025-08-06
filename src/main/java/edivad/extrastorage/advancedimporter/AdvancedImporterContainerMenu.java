package edivad.extrastorage.advancedimporter;

import java.util.function.Predicate;
import com.refinedmods.refinedstorage.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.setup.ESContainer;
import edivad.extrastorage.tools.AbstractAdvanceFilterContainerMenu;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AdvancedImporterContainerMenu extends
    AbstractAdvanceFilterContainerMenu<AdvancedImporterBlockEntity> {

  private static final MutableComponent FILTER_HELP = IdentifierUtil.createTranslation("gui", "importer.filter_help");

  private final Predicate<Player> stillValid;

  public AdvancedImporterContainerMenu(int windowId, Inventory inventory,
      ResourceContainerData resourceContainerData) {
    super(ESContainer.ADVANCED_IMPORTER.get(), windowId, inventory.player, resourceContainerData,
        UpgradeDestinations.IMPORTER, FILTER_HELP);
    this.stillValid = __ -> true;
  }

  public AdvancedImporterContainerMenu(int windowId, Player player, AdvancedImporterBlockEntity importer,
      ResourceContainer resourceContainer, UpgradeContainer upgradeContainer, Predicate<Player> stillValid) {
    super(ESContainer.ADVANCED_IMPORTER.get(), windowId, player, resourceContainer,
        upgradeContainer, importer, FILTER_HELP);
    this.stillValid = stillValid;
  }

  @Override
  protected void registerClientProperties() {
    registerProperty(new ClientProperty<>(PropertyTypes.FILTER_MODE, FilterMode.BLOCK));
    registerProperty(new ClientProperty<>(PropertyTypes.FUZZY_MODE, false));
    registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
  }

  @Override
  protected void registerServerProperties(final AdvancedImporterBlockEntity blockEntity) {
    registerProperty(new ServerProperty<>(
        PropertyTypes.FILTER_MODE,
        blockEntity::getFilterMode,
        blockEntity::setFilterMode
    ));
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
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid.test(player);
  }
}
