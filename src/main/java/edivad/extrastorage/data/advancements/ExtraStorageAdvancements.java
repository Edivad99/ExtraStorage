package edivad.extrastorage.data.advancements;

import java.util.function.Consumer;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.tools.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class ExtraStorageAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

  @Override
  public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer,
      ExistingFileHelper existingFileHelper) {
    Advancement.Builder.advancement()
        //.parent(new ResourceLocation("refinedstorage", "autocrafting"))
        .display(
            ESBlocks.CRAFTER.get(CrafterTier.IRON).get(),
            Component.translatable(Translations.IRON_CRAFTER.title()),
            Component.translatable(Translations.IRON_CRAFTER.desc()),
            null,
            FrameType.CHALLENGE,
            true, false, false)
        .addCriterion("inv_changed",
            InventoryChangeTrigger.TriggerInstance.hasItems(ESBlocks.CRAFTER.get(CrafterTier.IRON).get()))
        .save(consumer, ExtraStorage.rl("iron_crafter"), existingFileHelper);
  }
}
