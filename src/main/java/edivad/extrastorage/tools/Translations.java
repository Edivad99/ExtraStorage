package edivad.extrastorage.tools;

import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.extrastorage.ExtraStorage;

public class Translations {

  public static final String HOLD_SHIFT = "tooltip." + ExtraStorage.MODID + ".holdShift";
  public static final String SLOT_CRAFTING = "tooltip." + ExtraStorage.MODID + ".slotCrafting";
  public static final String BASE_SPEED = "tooltip." + ExtraStorage.MODID + ".baseSpeed";
  public static final String CURRENT_SPEED = "top." + ExtraStorage.MODID + ".currentSpeed";
  public static final String LIMITED_SPEED = "top." + ExtraStorage.MODID + ".limitedSpeed";
  public static final String OCCUPIED_SPACE = "top." + ExtraStorage.MODID + ".occupiedSpace";

  public static final TranslationsAdvancement IRON_CRAFTER = new TranslationsAdvancement(
      ExtraStorage.MODID,
      "iron_crafter");
  public static final TranslationsAdvancement ADVANCED_EXPORTER = new TranslationsAdvancement(
      ExtraStorage.MODID, "advanced_exporter");
  public static final TranslationsAdvancement ADVANCED_IMPORTER = new TranslationsAdvancement(
      ExtraStorage.MODID, "advanced_importer");
}
