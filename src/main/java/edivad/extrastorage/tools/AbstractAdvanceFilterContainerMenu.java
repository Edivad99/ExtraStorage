package edivad.extrastorage.tools;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.AbstractResourceContainerMenu;
import com.refinedmods.refinedstorage.common.support.containermenu.ResourceSlot;
import com.refinedmods.refinedstorage.common.support.containermenu.ResourceSlotType;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerImpl;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractAdvanceFilterContainerMenu<T extends BlockEntity> extends AbstractResourceContainerMenu {
  private static final int FILTER_SLOT_X = 8;
  private final Component filterHelp;

  protected AbstractAdvanceFilterContainerMenu(MenuType<?> type, int syncId, Player player, ResourceContainer resourceContainer, @Nullable UpgradeContainer upgradeContainer, T blockEntity, Component filterHelp) {
    super(type, syncId, player);
    this.filterHelp = filterHelp;
    this.registerServerProperties(blockEntity);
    this.addSlots(player, resourceContainer, upgradeContainer);
  }

  protected AbstractAdvanceFilterContainerMenu(MenuType<?> type, int syncId, Player player, ResourceContainerData resourceContainerData, @Nullable UpgradeDestinations upgradeDestination, Component filterHelp) {
    super(type, syncId);
    this.filterHelp = filterHelp;
    this.registerClientProperties();
    this.addSlots(player, ResourceContainerImpl.createForFilter(resourceContainerData), upgradeDestination == null ? null : new UpgradeContainer(upgradeDestination));
  }

  protected abstract void registerClientProperties();

  protected abstract void registerServerProperties(T var1);

  private void addSlots(Player player, ResourceContainer resourceContainer, @Nullable UpgradeContainer upgradeContainer) {
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 9; j++) {
        int index = (i * 9) + j;
        int x = 8 + (18 * j);
        int y = 20 + (18 * i);
        this.addSlot(new ResourceSlot(resourceContainer, index, this.filterHelp, x, y,
            ResourceSlotType.FILTER));
      }
    }

    if (upgradeContainer != null) {
      for(int i = 0; i < upgradeContainer.getContainerSize(); ++i) {
        this.addSlot(new UpgradeSlot(upgradeContainer, i, 187, 6 + i * 18));
      }
    }

    this.addPlayerInventory(player.getInventory(), FILTER_SLOT_X, 73);
    if (upgradeContainer != null) {
      this.transferManager.addBiTransfer(player.getInventory(), upgradeContainer);
    }

    this.transferManager.addFilterTransfer(player.getInventory());
  }
}
