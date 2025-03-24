package edivad.extrastorage.storage.advancedstorageblock;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.storage.AbstractStorageContainerBlockItem;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageRepository;
import com.refinedmods.refinedstorage.common.api.support.HelpTooltipComponent;
import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.storage.StorageTypes;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedStorageBlockItem extends AbstractStorageContainerBlockItem implements
    UpgradeableStorageContainer {

  private final AdvancedItemStorageVariant variant;
  private final Component helpText;

  public AdvancedStorageBlockItem(AdvancedStorageBlock<?> block, AdvancedItemStorageVariant variant) {
    super(block,
        new Item.Properties().stacksTo(1).fireResistant(),
        RefinedStorageApi.INSTANCE.getStorageContainerItemHelper());
    this.variant = variant;
    this.helpText = IdentifierUtil.createTranslation("item", "storage_block.help", IdentifierUtil.format(variant.getCapacity()));
  }

  @Override
  protected Long getCapacity() {
    return this.variant.getCapacity();
  }

  @Override
  protected String formatAmount(long amount) {
    return IdentifierUtil.format(amount);
  }

  @Override
  protected SerializableStorage createStorage(final StorageRepository storageRepository) {
    return createStorage(variant, storageRepository::markAsChanged);
  }

  static SerializableStorage createStorage(AdvancedItemStorageVariant type, final Runnable listener) {
    return StorageTypes.ITEM.create(type.getCapacity(), listener);
  }

  @Override
  protected ItemStack createPrimaryDisassemblyByproduct(final int count) {
    return new ItemStack(Blocks.INSTANCE.getMachineCasing(), count);
  }

  @Override
  @Nullable
  protected ItemStack createSecondaryDisassemblyByproduct(final int count) {
    return new ItemStack(ESItems.ITEM_STORAGE_PART.get(variant).get(), count);
  }

  @Override
  protected boolean placeBlock(final BlockPlaceContext ctx, final BlockState state) {
    if (ctx.getPlayer() instanceof ServerPlayer serverPlayer && !(RefinedStorageApi.INSTANCE.canPlaceNetworkNode(
        serverPlayer,
        ctx.getLevel(),
        ctx.getClickedPos(),
        state
    ))) {
      return false;
    }
    return super.placeBlock(ctx, state);
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
