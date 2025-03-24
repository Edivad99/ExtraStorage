package edivad.extrastorage.storage.advancedstorageblock;

import java.util.Optional;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.RefinedStorageClientApi;
import com.refinedmods.refinedstorage.common.api.storage.AbstractStorageContainerBlockItem;
import com.refinedmods.refinedstorage.common.api.storage.SerializableStorage;
import com.refinedmods.refinedstorage.common.api.storage.StorageRepository;
import com.refinedmods.refinedstorage.common.api.support.HelpTooltipComponent;
import com.refinedmods.refinedstorage.common.content.Blocks;
import com.refinedmods.refinedstorage.common.storage.StorageTypes;
import com.refinedmods.refinedstorage.common.storage.StorageVariant;
import com.refinedmods.refinedstorage.common.storage.UpgradeableStorageContainer;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedFluidStorageBlockBlockItem extends AbstractStorageContainerBlockItem implements
    UpgradeableStorageContainer {

  private final AdvancedFluidStorageVariant variant;
  private final Component helpText;

  public AdvancedFluidStorageBlockBlockItem(AdvancedStorageBlock<?> block, AdvancedFluidStorageVariant variant) {
    super(
        block,
        new Item.Properties().stacksTo(1).fireResistant(),
        RefinedStorageApi.INSTANCE.getStorageContainerItemHelper()
    );
    this.variant = variant;
    this.helpText = IdentifierUtil.createTranslation("item", "fluid_storage_block.help", IdentifierUtil.format(variant.getCapacityInBuckets()));
  }

  @Override
  protected Long getCapacity() {
    return variant.getCapacity();
  }

  @Override
  protected String formatAmount(final long amount) {
    return RefinedStorageClientApi.INSTANCE.getResourceRendering(FluidResource.class).formatAmount(amount);
  }

  @Override
  protected SerializableStorage createStorage(final StorageRepository storageRepository) {
    return createStorage(variant, storageRepository::markAsChanged);
  }

  static SerializableStorage createStorage(AdvancedFluidStorageVariant variant, Runnable listener) {
    return StorageTypes.FLUID.create(variant.getCapacity(), listener);
  }

  @Override
  protected ItemStack createPrimaryDisassemblyByproduct(final int count) {
    return new ItemStack(Blocks.INSTANCE.getMachineCasing(), count);
  }

  @Override
  protected ItemStack createSecondaryDisassemblyByproduct(final int count) {
    return new ItemStack(ESItems.FLUID_STORAGE_PART.get(variant).get(), count);
  }

  @Override
  protected boolean placeBlock(final BlockPlaceContext ctx, final BlockState state) {
    if (ctx.getPlayer() instanceof ServerPlayer serverPlayer && !(RefinedStorageApi.INSTANCE.canPlaceNetworkNode(
        serverPlayer,
        ctx.getLevel(),
        ctx.getClickedPos(),
        state))) {
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
