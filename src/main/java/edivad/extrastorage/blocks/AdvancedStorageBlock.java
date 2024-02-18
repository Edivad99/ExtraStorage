package edivad.extrastorage.blocks;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.BlockEntityMenuProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.container.AdvancedStorageBlockContainerMenu;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AdvancedStorageBlock extends NetworkNodeBlock {

  private final ItemStorageType type;

  public AdvancedStorageBlock(ItemStorageType type) {
    super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
    this.type = type;
  }

  public ItemStorageType getType() {
    return type;
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity,
      ItemStack stack) {
    if (!level.isClientSide) {
      var storage = ((AdvancedStorageBlockEntity) level.getBlockEntity(pos)).getNode();
      if (stack.hasTag() && stack.getTag().hasUUID(AdvancedStorageNetworkNode.NBT_ID)) {
        storage.setStorageId(stack.getTag().getUUID(AdvancedStorageNetworkNode.NBT_ID));
      }
      storage.loadStorage(entity instanceof Player player ? player : null);
    }
    // Call this after loading the storage, so the network discovery can use the loaded storage.
    super.setPlacedBy(level, pos, state, entity, stack);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedStorageBlockEntity(type, pos, state);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand handIn, BlockHitResult hit) {
    if (!level.isClientSide) {
      return NetworkUtils.attemptModify(level, pos, player, () -> player.openMenu(
          new BlockEntityMenuProvider<AdvancedStorageBlockEntity>(
              ((AdvancedStorageBlockEntity) level.getBlockEntity(pos)).getNode().getTitle(),
              (tile, windowId, inventory, p) ->
                  new AdvancedStorageBlockContainerMenu(windowId, player, tile),
              pos
          ), pos));
    }

    return InteractionResult.SUCCESS;
  }
}
