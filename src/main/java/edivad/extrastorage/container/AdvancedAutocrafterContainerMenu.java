package edivad.extrastorage.container;

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
import com.refinedmods.refinedstorage.common.support.packet.c2s.C2SPackets;
import com.refinedmods.refinedstorage.common.support.packet.s2c.S2CPackets;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeSlot;
import edivad.extrastorage.blockentity.AdvancedAutocrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.fromrs.AutocrafterPropertyTypes;
import edivad.extrastorage.setup.ESContainer;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AdvancedAutocrafterContainerMenu extends AbstractBaseContainerMenu {

  private static final int PATTERN_SLOT_X = 8;
  private static final int PATTERN_SLOT_Y = 20;

  private final Player player;
  @Getter
  private final CrafterTier tier;
  private final boolean partOfChain;
  private final boolean headOfChain;
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

  public boolean isPartOfChain() {
    return partOfChain;
  }

  public boolean isHeadOfChain() {
    return headOfChain;
  }

  public boolean isLocked() {
    return locked;
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
      S2CPackets.sendAutocrafterLockedUpdate((ServerPlayer) player, locked);
    }
  }

  private void detectNameChange() {
    if (autocrafter == null) {
      return;
    }
    final Component newName = autocrafter.getDisplayName();
    if (!newName.equals(name)) {
      this.name = newName;
      S2CPackets.sendAutocrafterNameUpdate((ServerPlayer) player, newName);
    }
  }

  private void addSlots(final FilteredContainer patternContainer, final UpgradeContainer upgradeContainer) {
    for (int i = 0; i < patternContainer.getContainerSize(); ++i) {
      addSlot(createPatternSlot(patternContainer, i, player.level()));
    }
    for (int i = 0; i < upgradeContainer.getContainerSize(); ++i) {
      addSlot(new UpgradeSlot(upgradeContainer, i, 187, 6 + (i * 18)));
    }
    addPlayerInventory(player.getInventory(), 8, 55);
    transferManager.addBiTransfer(player.getInventory(), upgradeContainer);
    transferManager.addBiTransfer(player.getInventory(), patternContainer);
  }

  private Slot createPatternSlot(final FilteredContainer patternContainer,
      final int i,
      final Level level) {
    final int x = PATTERN_SLOT_X + (18 * i);
    return new PatternSlot(patternContainer, i, x, PATTERN_SLOT_Y, level);
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
      C2SPackets.sendAutocrafterNameChange(newName);
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
