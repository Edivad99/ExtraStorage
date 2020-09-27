package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainer;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedCrafterContainer extends BaseContainer
{
    private final AdvancedCrafterTile tile;

    public AdvancedCrafterContainer(int windowId, PlayerEntity player, AdvancedCrafterTile tile)
    {
        super(Registration.CRAFTER_CONTAINER.get(tile.getTier()).get(), tile, player, windowId);
        this.tile = tile;

        CrafterTier tier = tile.getTier();
        for(int i = 0; i < tier.getRowsOfSlots(); i++)
            for(int j = 0; j < 9; j++)
                addSlot(new SlotItemHandler(tile.getNode().getPatternItems(), (i * 9) + j, 8 + (18 * j), 20 + (18 * i)));

        for (int i = 0; i < 4; i++)
            addSlot(new SlotItemHandler(tile.getNode().getUpgrades(), i, 187, 6 + (i * 18)));

        switch (tier)
        {
            case IRON: addPlayerInventory(8, 91); break;
            case GOLD: addPlayerInventory(8, 127); break;
            case DIAMOND: addPlayerInventory(8, 163); break;
            case NETHERITE: addPlayerInventory(8, 199); break;
        }

        transferManager.addBiTransfer(player.inventory, tile.getNode().getUpgrades());
        transferManager.addBiTransfer(player.inventory, tile.getNode().getPatternItems());
    }

    @Override
    public AdvancedCrafterTile getTile()
    {
        return tile;
    }
}
