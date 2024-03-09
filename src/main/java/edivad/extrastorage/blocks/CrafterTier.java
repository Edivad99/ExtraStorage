package edivad.extrastorage.blocks;

import java.util.Locale;

public enum CrafterTier {
  IRON, GOLD, DIAMOND, NETHERITE;

  private final int craftingSpeed;
  private final int rowsOfSlots;

  CrafterTier() {
    craftingSpeed = (int) Math.pow(5, this.ordinal());
    rowsOfSlots = 3 + 2 * (this.ordinal());
  }

  public int getCraftingSpeed() {
    return craftingSpeed;
  }

  public int getRowsOfSlots() {
    return rowsOfSlots;
  }

  public int getSlots() {
    return rowsOfSlots * 9;
  }

  public String getID() {
    return this.name().toLowerCase(Locale.ROOT) + "_crafter";
  }
}

/*
+---------+----------+-------+-------+-------+-------+
|Level    |Base speed|1 speed|2 speed|3 speed|4 speed|
+---------+----------+-------+-------+-------+-------+
|Iron     |    1     |   2   |   3   |   4   |   5   |
|Gold     |    5     |   6   |   7   |   8   |   9   |
|Diamond  |   25     |  30   |  35   |  40   |  45   |
|Netherite|  125     | 150   | 175   | 200   | 225   |
+---------+----------+-------+-------+-------+-------+
 */
