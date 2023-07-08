package edivad.extrastorage.data;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.tools.Translations;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ExtraStorageLanguageProvider extends LanguageProvider {

  public ExtraStorageLanguageProvider(PackOutput packOutput) {
    super(packOutput, ExtraStorage.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    for (ItemStorageType type : ItemStorageType.values()) {
      add(ESItems.ITEM_STORAGE_PART.get(type).get(), type.getName() + " Storage Part");
      add(ESItems.ITEM_DISK.get(type).get(), type.getName() + " Storage Disk");
      add(ESBlocks.ITEM_STORAGE.get(type).get(), type.getName() + " Storage Block");
    }

    for (FluidStorageType type : FluidStorageType.values()) {
      add(ESItems.FLUID_STORAGE_PART.get(type).get(), type.getName() + " Fluid Storage Part");
      add(ESItems.FLUID_DISK.get(type).get(), type.getName() + " Fluid Storage Disk");
      add(ESBlocks.FLUID_STORAGE.get(type).get(), type.getName() + " Fluid Storage Block");
    }

    for (CrafterTier tier : CrafterTier.values()) {
      String baseName = tier.name().toLowerCase();
      String TierName = baseName.substring(0, 1).toUpperCase() + baseName.substring(1);
      add(ESItems.CRAFTER.get(tier).get(), TierName + " Crafter");
    }

    add(ESItems.ADVANCED_EXPORTER.get(), "Advanced Exporter");
    add(ESItems.ADVANCED_IMPORTER.get(), "Advanced Importer");
    add(ESItems.RAW_NEURAL_PROCESSOR.get(), "Raw Neural Processor");
    add(ESItems.NEURAL_PROCESSOR.get(), "Neural Processor");
    add(Translations.HOLD_SHIFT, "Hold Shift for details");
    add(Translations.SLOT_CRAFTING, "Slots for patterns: %s");
    add(Translations.BASE_SPEED, "Base speed: %sx");
    add(Translations.CURRENT_SPEED, "Current speed: %sx");
    add(Translations.LIMITED_SPEED, "Current speed (limited by %s): %sx");
    add(Translations.OCCUPIED_SPACE, "Occupied space: %s/%s");

    add(Translations.IRON_CRAFTER.title(), "Iron Crafter");
    add(Translations.IRON_CRAFTER.desc(), "The next generation of crafters!");
    add(Translations.ADVANCED_EXPORTER.title(), "Advanced Exporter");
    add(Translations.ADVANCED_EXPORTER.desc(), "A much larger exporter!");
    add(Translations.ADVANCED_IMPORTER.title(), "Advanced Importer");
    add(Translations.ADVANCED_IMPORTER.desc(), "Many more filters!");
  }
}
