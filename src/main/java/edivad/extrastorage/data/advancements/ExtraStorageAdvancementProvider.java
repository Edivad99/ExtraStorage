package edivad.extrastorage.data.advancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ExtraStorageAdvancementProvider extends AdvancementProvider {

  public ExtraStorageAdvancementProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries,
      ExistingFileHelper existingFileHelper) {
    super(output, registries, existingFileHelper, List.of(
      new ExtraStorageAdvancements()
    ));
  }
}
