package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.setup.ESContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedImporterContainerMenu extends BaseContainerMenu {

  private final AdvancedImporterBlockEntity importerBlockEntity;

  public AdvancedImporterContainerMenu(int windowId, Player player,
      AdvancedImporterBlockEntity importerBlockEntity) {
    super(ESContainer.ADVANCED_IMPORTER.get(), importerBlockEntity, player, windowId);
    this.importerBlockEntity = importerBlockEntity;
    initSlots();
  }

  public void initSlots() {
    for (int i = 0; i < 4; i++) {
      addSlot(
          new SlotItemHandler(importerBlockEntity.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
    }

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 9; j++) {
        int index = (i * 9) + j;
        int x = 8 + (18 * j);
        int y = 20 + (18 * i);

        addSlot(new FilterSlot(
            importerBlockEntity.getNode().getItemFilters(),
            index, x, y
        ).setEnableHandler(() -> importerBlockEntity.getNode().getType() == IType.ITEMS));

        addSlot(new FluidFilterSlot(
            importerBlockEntity.getNode().getFluidFilters(),
            index, x, y
        ).setEnableHandler(() -> importerBlockEntity.getNode().getType() == IType.FLUIDS));
      }
    }

    addPlayerInventory(8, 73);

    transferManager.addBiTransfer(getPlayer().getInventory(),
        importerBlockEntity.getNode().getUpgrades());
    transferManager.addFilterTransfer(getPlayer().getInventory(),
        importerBlockEntity.getNode().getItemFilters(),
        importerBlockEntity.getNode().getFluidFilters(), importerBlockEntity.getNode()::getType);
  }

  @Override
  public AdvancedImporterBlockEntity getBlockEntity() {
    return importerBlockEntity;
  }
}
