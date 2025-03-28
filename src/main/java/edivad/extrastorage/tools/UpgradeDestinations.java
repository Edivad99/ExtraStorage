package edivad.extrastorage.tools;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeDestination;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.setup.ESItems;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public enum UpgradeDestinations implements UpgradeDestination {
  ADVANCED_IMPORTER(Translations.ADVANCED_IMPORTER.translateTitle(),
      () -> new ItemStack(ESItems.ADVANCED_IMPORTER.get())),
  ADVANCED_EXPORTER(Translations.ADVANCED_EXPORTER.translateTitle(), () -> new ItemStack(ESItems.ADVANCED_EXPORTER.get())),
  ADVANCED_AUTOCRAFTER(Translations.IRON_CRAFTER.translateTitle(),
      () -> new ItemStack(ESItems.CRAFTER.get(CrafterTier.IRON).get()));

  @Getter
  private final Component name;
  private final Supplier<ItemStack> stackFactory;
  @Nullable
  private ItemStack cachedStack;

  UpgradeDestinations(final Component name, final Supplier<ItemStack> stackFactory) {
    this.name = name;
    this.stackFactory = stackFactory;
  }

  public ItemStack getStackRepresentation() {
    if (this.cachedStack == null) {
      this.cachedStack = this.stackFactory.get();
    }
    return this.cachedStack;
  }
}
