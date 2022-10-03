package edivad.extrastorage.tools;

import edivad.edivadlib.tools.TranslationsAdvancement;
import edivad.extrastorage.Main;

public class Translations
{
    public static final String HOLD_SHIFT = "tooltip." + Main.MODID + ".holdShift";
    public static final String SLOT_CRAFTING = "tooltip." + Main.MODID + ".slotCrafting";
    public static final String BASE_SPEED = "tooltip." + Main.MODID + ".baseSpeed";
    public static final String CURRENT_SPEED = "top." + Main.MODID + ".currentSpeed";
    public static final String LIMITED_SPEED = "top." + Main.MODID + ".limitedSpeed";
    public static final String OCCUPIED_SPACE = "top." + Main.MODID + ".occupiedSpace";

    public static final TranslationsAdvancement IRON_CRAFTER = new TranslationsAdvancement(Main.MODID, "iron_crafter");
    public static final TranslationsAdvancement ADVANCED_EXPORTER = new TranslationsAdvancement(Main.MODID, "advanced_exporter");
    public static final TranslationsAdvancement ADVANCED_IMPORTER = new TranslationsAdvancement(Main.MODID, "advanced_importer");
}
