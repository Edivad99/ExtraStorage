package edivad.extrastorage.autocrafting.advancedautocrafter;

import com.refinedmods.refinedstorage.common.autocrafting.autocrafter.LockMode;

class LockModeSettings {
  private static final int NEVER = 0;
  private static final int LOCK_UNTIL_REDSTONE_PULSE_RECEIVED = 1;
  private static final int LOCK_UNTIL_CONNECTED_MACHINE_IS_EMPTY = 2;
  private static final int LOCK_UNTIL_ALL_OUTPUTS_ARE_RECEIVED = 3;
  private static final int LOCK_UNTIL_HIGH_REDSTONE_SIGNAL = 4;
  private static final int LOCK_UNTIL_LOW_REDSTONE_SIGNAL = 5;

  private LockModeSettings() {
  }

  static LockMode getLockMode(final int lockMode) {
    return switch (lockMode) {
      case LOCK_UNTIL_REDSTONE_PULSE_RECEIVED -> LockMode.LOCK_UNTIL_REDSTONE_PULSE_RECEIVED;
      case LOCK_UNTIL_CONNECTED_MACHINE_IS_EMPTY -> LockMode.LOCK_UNTIL_CONNECTED_MACHINE_IS_EMPTY;
      case LOCK_UNTIL_ALL_OUTPUTS_ARE_RECEIVED -> LockMode.LOCK_UNTIL_ALL_OUTPUTS_ARE_RECEIVED;
      case LOCK_UNTIL_HIGH_REDSTONE_SIGNAL -> LockMode.LOCK_UNTIL_HIGH_REDSTONE_SIGNAL;
      case LOCK_UNTIL_LOW_REDSTONE_SIGNAL -> LockMode.LOCK_UNTIL_LOW_REDSTONE_SIGNAL;
      default -> LockMode.NEVER;
    };
  }

  static int getLockMode(final LockMode lockMode) {
    return switch (lockMode) {
      case LOCK_UNTIL_REDSTONE_PULSE_RECEIVED -> LOCK_UNTIL_REDSTONE_PULSE_RECEIVED;
      case LOCK_UNTIL_CONNECTED_MACHINE_IS_EMPTY -> LOCK_UNTIL_CONNECTED_MACHINE_IS_EMPTY;
      case LOCK_UNTIL_ALL_OUTPUTS_ARE_RECEIVED -> LOCK_UNTIL_ALL_OUTPUTS_ARE_RECEIVED;
      case LOCK_UNTIL_HIGH_REDSTONE_SIGNAL -> LOCK_UNTIL_HIGH_REDSTONE_SIGNAL;
      case LOCK_UNTIL_LOW_REDSTONE_SIGNAL -> LOCK_UNTIL_LOW_REDSTONE_SIGNAL;
      default -> NEVER;
    };
  }
}
