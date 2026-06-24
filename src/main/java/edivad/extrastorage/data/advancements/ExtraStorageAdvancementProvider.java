package edivad.extrastorage.data.advancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

public class ExtraStorageAdvancementProvider extends AdvancementProvider {

  public ExtraStorageAdvancementProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, List.of(
      new ExtraStorageAdvancements()
    ));
  }
}
