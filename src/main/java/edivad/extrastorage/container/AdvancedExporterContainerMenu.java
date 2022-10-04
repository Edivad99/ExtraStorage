package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.setup.Registration;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedExporterContainerMenu extends BaseContainerMenu {
    private final AdvancedExporterBlockEntity exporterBlockEntity;
    private boolean hasRegulatorMode;

    public AdvancedExporterContainerMenu(int windowId, Player player, AdvancedExporterBlockEntity exporterBlockEntity) {
        super(Registration.ADVANCED_EXPORTER_CONTAINER.get(), exporterBlockEntity, player, windowId);
        this.exporterBlockEntity = exporterBlockEntity;
        this.hasRegulatorMode = hasRegulatorMode();
        initSlots();
    }

    private boolean hasRegulatorMode() {
        return exporterBlockEntity.getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        boolean updatedHasRegulatorMode = hasRegulatorMode();
        if (hasRegulatorMode != updatedHasRegulatorMode) {
            hasRegulatorMode = updatedHasRegulatorMode;
            initSlots();
        }
    }

    public void initSlots() {
        this.slots.clear();
        this.lastSlots.clear();

        this.transferManager.clearTransfers();

        for (int i = 0; i < 4; i++) {
            addSlot(new SlotItemHandler(exporterBlockEntity.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
        }

        boolean hasRegulator = exporterBlockEntity.getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                int index = (i * 9) + j;
                int x = 8 + (18 * j);
                int y = 20 + (18 * i);

                addSlot(new FilterSlot(
                        exporterBlockEntity.getNode().getItemFilters(),
                        index, x, y,
                        hasRegulator ? FilterSlot.FILTER_ALLOW_SIZE : 0
                ).setEnableHandler(() -> exporterBlockEntity.getNode().getType() == IType.ITEMS));

                addSlot(new FluidFilterSlot(
                        exporterBlockEntity.getNode().getFluidFilters(),
                        index, x, y,
                        hasRegulator ? FluidFilterSlot.FILTER_ALLOW_SIZE : 0
                ).setEnableHandler(() -> exporterBlockEntity.getNode().getType() == IType.FLUIDS));
            }
        }

        addPlayerInventory(8, 73);

        transferManager.addBiTransfer(getPlayer().getInventory(), exporterBlockEntity.getNode().getUpgrades());
        transferManager.addFilterTransfer(getPlayer().getInventory(), exporterBlockEntity.getNode().getItemFilters(), exporterBlockEntity.getNode().getFluidFilters(), exporterBlockEntity.getNode()::getType);
    }

    @Override
    public AdvancedExporterBlockEntity getBlockEntity() {
        return exporterBlockEntity;
    }
}
