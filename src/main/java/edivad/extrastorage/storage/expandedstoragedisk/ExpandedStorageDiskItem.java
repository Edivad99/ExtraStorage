package edivad.extrastorage.storage.expandedstoragedisk;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.format;

import java.util.Optional;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.AbstractStorageContainerItem;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageRepository;
import com.refinedmods.refinedstorage.common.api.support.HelpTooltipComponent;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.common.storage.StorageTypes;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExpandedStorageDiskItem extends AbstractStorageContainerItem implements
    UpgradeableStorageContainer {

  private final AdvancedItemStorageVariant variant;
  private final Component helpText;

  public ExpandedStorageDiskItem(AdvancedItemStorageVariant variant) {
    super(
        new Item.Properties().stacksTo(1).fireResistant(),
        RefinedStorageApi.INSTANCE.getStorageContainerItemHelper()
    );
    this.variant = variant;
    this.helpText = createTranslation("item", "storage_disk.help", format(variant.getCapacity()));
  }

  @Override
  protected Long getCapacity() {
    return variant.getCapacity();
  }

  @Override
  protected String formatAmount(final long amount) {
    return format(amount);
  }

  @Override
  protected SerializableStorage createStorage(final StorageRepository storageRepository) {
    return StorageTypes.ITEM.create(variant.getCapacity(), storageRepository::markAsChanged);
  }

  @Override
  protected ItemStack createPrimaryDisassemblyByproduct(final int count) {
    return new ItemStack(Items.INSTANCE.getStorageHousing(), count);
  }

  @Override
  protected ItemStack createSecondaryDisassemblyByproduct(final int count) {
    return new ItemStack(ESItems.ITEM_STORAGE_PART.get(variant).get(), count);
  }

  @Override
  public Optional<TooltipComponent> getTooltipImage(final ItemStack stack) {
    return Optional.of(new HelpTooltipComponent(helpText));
  }

  @Override
  public StorageVariant getVariant() {
    return variant;
  }

  @Override
  public void transferTo(final ItemStack from, final ItemStack to) {
    helper.markAsToTransfer(from, to);
  }
}
