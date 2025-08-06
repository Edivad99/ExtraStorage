package edivad.extrastorage.autocrafting.advancedautocrafter;

import org.jetbrains.annotations.Nullable;
import com.google.common.util.concurrent.RateLimiter;
import com.refinedmods.refinedstorage.common.autocrafting.PatternInventory;
import com.refinedmods.refinedstorage.common.autocrafting.PatternSlot;
import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.AutocrafterData;
import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.LockMode;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.FilteredContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeSlot;
import edivad.extrastorage.network.to_client.AdvancedAutocrafterLockedUpdatePacket;
import edivad.extrastorage.network.to_client.AdvancedAutocrafterNameUpdatePacket;
import edivad.extrastorage.network.to_server.AdvancedAutocrafterNameChangePacket;
import edivad.extrastorage.setup.ESContainer;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class AdvancedAutocrafterContainerMenu extends AbstractBaseContainerMenu {

  private static final int PATTERN_SLOT_X = 8;
  private static final int PATTERN_SLOT_Y = 20;

  private final Player player;
  @Getter
  private final CrafterTier tier;
  @Getter
  private final boolean partOfChain;
  @Getter
  private final boolean headOfChain;
  @Getter
  private boolean locked;
  private final RateLimiter nameRateLimiter = RateLimiter.create(0.5);

  @Nullable
  private AdvancedAutocrafterBlockEntity autocrafter;
  @Nullable
  private Listener listener;
  private Component name;

  public AdvancedAutocrafterContainerMenu(int windowId, Inventory inventory,
      AutocrafterData data, CrafterTier tier) {
    super(ESContainer.CRAFTER.get(tier).get(), windowId);
    this.player = inventory.player;
    this.tier = tier;
    registerProperty(new ClientProperty<>(AutocrafterPropertyTypes.LOCK_MODE, LockMode.NEVER));
    registerProperty(new ClientProperty<>(AutocrafterPropertyTypes.PRIORITY, 0));
    registerProperty(
        new ClientProperty<>(AutocrafterPropertyTypes.VISIBLE_TO_THE_AUTOCRAFTER_MANAGER, true));
    addSlots(
        new PatternInventory(tier.getSlots(), inventory.player::level),
        new UpgradeContainer(UpgradeDestinations.AUTOCRAFTER)
    );
    this.name = Component.empty();
    this.partOfChain = data.partOfChain();
    this.headOfChain = data.headOfChain();
    this.locked = data.locked();
  }

  public AdvancedAutocrafterContainerMenu(int windowId, Inventory inventory,
      AdvancedAutocrafterBlockEntity autocrafter, CrafterTier tier) {
    super(ESContainer.CRAFTER.get(tier).get(), windowId);
    this.autocrafter = autocrafter;
    this.player = inventory.player;
    this.name = autocrafter.getDisplayName();
    this.partOfChain = false;
    this.headOfChain = false;
    this.locked = autocrafter.isLocked();
    this.tier = tier;
    registerProperty(new ServerProperty<>(
        AutocrafterPropertyTypes.LOCK_MODE,
        autocrafter::getLockMode,
        autocrafter::setLockMode
    ));
    registerProperty(new ServerProperty<>(
        AutocrafterPropertyTypes.PRIORITY,
        autocrafter::getPriority,
        autocrafter::setPriority
    ));
    registerProperty(new ServerProperty<>(
        AutocrafterPropertyTypes.VISIBLE_TO_THE_AUTOCRAFTER_MANAGER,
        autocrafter::isVisibleToTheAutocrafterManager,
        autocrafter::setVisibleToTheAutocrafterManager
    ));
    addSlots(autocrafter.getPatternContainer(), autocrafter.getUpgradeContainer());
  }

  public boolean canChangeName() {
    return !partOfChain;
  }

  public void setListener(@Nullable final Listener listener) {
    this.listener = listener;
  }

  @Override
  public void broadcastChanges() {
    super.broadcastChanges();
    if (autocrafter == null) {
      return;
    }
    if (nameRateLimiter.tryAcquire()) {
      detectNameChange();
    }
    final boolean newLocked = autocrafter.isLocked();
    if (locked != newLocked) {
      locked = newLocked;
      PacketDistributor.sendToPlayer((ServerPlayer) player,
          new AdvancedAutocrafterLockedUpdatePacket(locked));
    }
  }

  @Override
  public boolean stillValid(Player player) {
    if (autocrafter == null) {
      return true;
    }
    return Container.stillValidBlockEntity(autocrafter, player);
  }

  private void detectNameChange() {
    if (autocrafter == null) {
      return;
    }
    final Component newName = autocrafter.getDisplayName();
    if (!newName.equals(name)) {
      this.name = newName;
      PacketDistributor.sendToPlayer((ServerPlayer) player,
          new AdvancedAutocrafterNameUpdatePacket(newName));
    }
  }

  private void addSlots(final FilteredContainer patternContainer, final UpgradeContainer upgradeContainer) {
    for (int i = 0; i < tier.getRowsOfSlots(); i++) {
      for (int j = 0; j < 9; j++) {
        addSlot(new PatternSlot(patternContainer, (i * 9) + j,
            PATTERN_SLOT_X + (18 * j), PATTERN_SLOT_Y + (18 * i), player.level()));
      }
    }

    for (int i = 0; i < upgradeContainer.getContainerSize(); ++i) {
      addSlot(new UpgradeSlot(upgradeContainer, i, 187, 6 + (i * 18)));
    }

    switch (tier) {
      case IRON -> addPlayerInventory(player.getInventory(), 8, 91);
      case GOLD -> addPlayerInventory(player.getInventory(), 8, 127);
      case DIAMOND -> addPlayerInventory(player.getInventory(), 8, 163);
      case NETHERITE -> addPlayerInventory(player.getInventory(), 8, 199);
    }

    transferManager.addBiTransfer(player.getInventory(), upgradeContainer);
    transferManager.addBiTransfer(player.getInventory(), patternContainer);
  }

  public boolean containsPattern(final ItemStack stack) {
    for (final Slot slot : slots) {
      if (slot instanceof PatternSlot patternSlot && patternSlot.getItem() == stack) {
        return true;
      }
    }
    return false;
  }

  public void changeName(final String newName) {
    if (partOfChain) {
      return;
    }
    if (autocrafter != null) {
      autocrafter.setCustomName(newName);
      detectNameChange();
    } else {
      PacketDistributor.sendToServer(new AdvancedAutocrafterNameChangePacket(newName));
    }
  }

  public void nameChanged(final Component newName) {
    if (listener != null) {
      listener.nameChanged(newName);
    }
  }

  public void lockedChanged(final boolean newLocked) {
    this.locked = newLocked;
    if (listener != null) {
      listener.lockedChanged(newLocked);
    }
  }

  public interface Listener {
    void nameChanged(Component name);

    void lockedChanged(boolean locked);
  }
}
