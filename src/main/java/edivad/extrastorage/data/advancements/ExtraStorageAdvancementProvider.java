package edivad.extrastorage.data.advancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class ExtraStorageAdvancementProvider extends ForgeAdvancementProvider {

  public ExtraStorageAdvancementProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries,
      ExistingFileHelper existingFileHelper) {
    super(output, registries, existingFileHelper, List.of(
      new ExtraStorageAdvancements()
    ));
  }
}
