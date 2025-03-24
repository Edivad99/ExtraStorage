package edivad.extrastorage.storage.advancedstorageblock;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.api.storage.StorageBlockProvider;
import com.refinedmods.refinedstorage.common.support.AbstractBaseBlock;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.network.AbstractBaseNetworkNodeContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedStorageBlock<T extends BlockEntity> extends AbstractBaseBlock implements EntityBlock {

  private final AbstractBlockEntityTicker<T> ticker;
  private final StorageBlockProvider provider;

  public AdvancedStorageBlock(final Properties properties, StorageBlockProvider provider) {
    super(properties);
    this.provider = provider;
    this.ticker =
        new AbstractBlockEntityTicker<>(() -> (BlockEntityType<T>) this.provider.getBlockEntityType()) {
      @Override
      public void tick(Level level, BlockPos blockPos, BlockState blockState,
          BlockEntity blockEntity) {
        if (blockEntity instanceof AbstractBaseNetworkNodeContainerBlockEntity<?> networkNode) {
          networkNode.updateActiveness(blockState, null);
          networkNode.doWork();
        }
      }
    };
  }

  @Nullable
  @Override
  public <O extends BlockEntity> BlockEntityTicker<O> getTicker(Level level, BlockState blockState,
      BlockEntityType<O> type) {
    return ticker.get(level, type);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedStorageBlockBlockEntity(pos, state, provider);
  }
}
