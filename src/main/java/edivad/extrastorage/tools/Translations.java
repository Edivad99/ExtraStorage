package edivad.extrastorage.tools;

import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.extrastorage.ExtraStorage;

public class Translations {

  public static final String HOLD_SHIFT = "tooltip." + ExtraStorage.ID + ".holdShift";
  public static final String SLOT_CRAFTING = "tooltip." + ExtraStorage.ID + ".slotCrafting";
  public static final String BASE_SPEED = "tooltip." + ExtraStorage.ID + ".baseSpeed";
  public static final String CURRENT_SPEED = "top." + ExtraStorage.ID + ".currentSpeed";
  public static final String LIMITED_SPEED = "top." + ExtraStorage.ID + ".limitedSpeed";
  public static final String OCCUPIED_SPACE = "top." + ExtraStorage.ID + ".occupiedSpace";

  public static final TranslationsAdvancement IRON_CRAFTER = new TranslationsAdvancement(
      ExtraStorage.ID,
      "iron_crafter");
  public static final TranslationsAdvancement ADVANCED_EXPORTER = new TranslationsAdvancement(
      ExtraStorage.ID, "advanced_exporter");
  public static final TranslationsAdvancement ADVANCED_IMPORTER = new TranslationsAdvancement(
      ExtraStorage.ID, "advanced_importer");
}
