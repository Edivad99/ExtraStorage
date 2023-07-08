package edivad.extrastorage.container;

import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.setup.ESContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class AdvancedCrafterContainerMenu extends BaseContainerMenu {

  private final AdvancedCrafterBlockEntity crafterBlockEntity;

  public AdvancedCrafterContainerMenu(int windowId, Player player,
      AdvancedCrafterBlockEntity crafterBlockEntity) {
    super(ESContainer.CRAFTER.get(crafterBlockEntity.getTier()).get(),
        crafterBlockEntity, player, windowId);
    this.crafterBlockEntity = crafterBlockEntity;

    CrafterTier tier = crafterBlockEntity.getTier();
    for (int i = 0; i < tier.getRowsOfSlots(); i++) {
      for (int j = 0; j < 9; j++) {
        addSlot(new SlotItemHandler(crafterBlockEntity.getNode().getPatternItems(), (i * 9) + j,
            8 + (18 * j), 20 + (18 * i)));
      }
    }

    for (int i = 0; i < 4; i++) {
      addSlot(
          new SlotItemHandler(crafterBlockEntity.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
    }

    switch (tier) {
      case IRON -> addPlayerInventory(8, 91);
      case GOLD -> addPlayerInventory(8, 127);
      case DIAMOND -> addPlayerInventory(8, 163);
      case NETHERITE -> addPlayerInventory(8, 199);
    }

    transferManager.addBiTransfer(player.getInventory(),
        crafterBlockEntity.getNode().getUpgrades());
    transferManager.addBiTransfer(player.getInventory(),
        crafterBlockEntity.getNode().getPatternItems());
  }

  @Override
  public AdvancedCrafterBlockEntity getBlockEntity() {
    return crafterBlockEntity;
  }
}
