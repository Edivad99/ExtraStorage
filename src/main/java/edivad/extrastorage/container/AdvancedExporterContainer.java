package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainer;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.tile.config.IType;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedExporterContainer extends BaseContainer
{
    private final AdvancedExporterBlockEntity tile;
    private boolean hasRegulatorMode;

    public AdvancedExporterContainer(int windowId, Player player, AdvancedExporterBlockEntity tile)
    {
        super(Registration.ADVANCED_EXPORTER_CONTAINER.get(), tile, player, windowId);
        this.tile = tile;
        this.hasRegulatorMode = hasRegulatorMode();
        initSlots();
    }

    private boolean hasRegulatorMode()
    {
        return tile.getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);
    }

    @Override
    public void broadcastChanges()
    {
        super.broadcastChanges();

        boolean updatedHasRegulatorMode = hasRegulatorMode();
        if (hasRegulatorMode != updatedHasRegulatorMode)
        {
            hasRegulatorMode = updatedHasRegulatorMode;
            initSlots();
        }
    }

    public void initSlots() {
        this.slots.clear();
        this.lastSlots.clear();

        this.transferManager.clearTransfers();

        for (int i = 0; i < 4; i++)
            addSlot(new SlotItemHandler(tile.getNode().getUpgrades(), i, 187, 6 + (i * 18)));

        boolean hasRegulator = tile.getNode().getUpgrades().hasUpgrade(UpgradeItem.Type.REGULATOR);

        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                int index = (i * 9) + j;
                int x = 8 + (18 * j);
                int y = 20 + (18 * i);

                addSlot(new FilterSlot(
                        tile.getNode().getItemFilters(),
                        index, x, y,
                        hasRegulator ? FilterSlot.FILTER_ALLOW_SIZE : 0
                ).setEnableHandler(() -> tile.getNode().getType() == IType.ITEMS));

                addSlot(new FluidFilterSlot(
                        tile.getNode().getFluidFilters(),
                        index, x, y,
                        hasRegulator ? FluidFilterSlot.FILTER_ALLOW_SIZE : 0
                ).setEnableHandler(() -> tile.getNode().getType() == IType.FLUIDS));
            }
        }

        addPlayerInventory(8, 73);

        transferManager.addBiTransfer(getPlayer().getInventory(), tile.getNode().getUpgrades());
        transferManager.addFilterTransfer(getPlayer().getInventory(), tile.getNode().getItemFilters(), tile.getNode().getFluidFilters(), tile.getNode()::getType);
    }

    @Override
    public AdvancedExporterBlockEntity getTile()
    {
        return tile;
    }
}
