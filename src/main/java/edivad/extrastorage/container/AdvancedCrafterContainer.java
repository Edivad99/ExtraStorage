package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainer;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedCrafterContainer extends BaseContainer
{
    private final AdvancedCrafterBlockEntity tile;

    public AdvancedCrafterContainer(int windowId, Player player, AdvancedCrafterBlockEntity crafter)
    {
        super(Registration.CRAFTER_CONTAINER.get(crafter.getTier()).get(), crafter, player, windowId);
        this.tile = crafter;

        CrafterTier tier = crafter.getTier();
        for(int i = 0; i < tier.getRowsOfSlots(); i++)
            for(int j = 0; j < 9; j++)
                addSlot(new SlotItemHandler(crafter.getNode().getPatternItems(), (i * 9) + j, 8 + (18 * j), 20 + (18 * i)));

        for (int i = 0; i < 4; i++)
            addSlot(new SlotItemHandler(crafter.getNode().getUpgrades(), i, 187, 6 + (i * 18)));

        switch(tier) {
            case IRON -> addPlayerInventory(8, 91);
            case GOLD -> addPlayerInventory(8, 127);
            case DIAMOND -> addPlayerInventory(8, 163);
            case NETHERITE -> addPlayerInventory(8, 199);
        }

        transferManager.addBiTransfer(player.getInventory(), crafter.getNode().getUpgrades());
        transferManager.addBiTransfer(player.getInventory(), crafter.getNode().getPatternItems());
    }

    @Override
    public AdvancedCrafterBlockEntity getTile()
    {
        return tile;
    }
}
