package edivad.extrastorage.advancedimporter;

import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.AbstractDirectionalCableBlock;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.BlockItemProvider;
import com.refinedmods.refinedstorage.common.support.DirectionalCableBlockShapes;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AdvancedImporterBlock extends AbstractDirectionalCableBlock implements EntityBlock,
    BlockItemProvider<BaseBlockItem> {

  private static final Component HELP = IdentifierUtil.createTranslation("item", "importer.help");
  private static final ConcurrentHashMap<DirectionalCacheShapeCacheKey, VoxelShape> SHAPE_CACHE =
      new ConcurrentHashMap<>();
  private static final AbstractBlockEntityTicker<AdvancedImporterBlockEntity> TICKER =
      new NetworkNodeBlockEntityTicker<>(ESBlockEntities.ADVANCED_IMPORTER);

  public AdvancedImporterBlock() {
    super(SHAPE_CACHE);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedImporterBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,
      BlockState blockState, BlockEntityType<T> type) {
    return TICKER.get(level, type);
  }

  @Override
  protected VoxelShape getExtensionShape(final Direction direction) {
    return switch (direction) {
      case NORTH -> DirectionalCableBlockShapes.IMPORTER_NORTH;
      case EAST -> DirectionalCableBlockShapes.IMPORTER_EAST;
      case SOUTH -> DirectionalCableBlockShapes.IMPORTER_SOUTH;
      case WEST -> DirectionalCableBlockShapes.IMPORTER_WEST;
      case UP -> DirectionalCableBlockShapes.IMPORTER_UP;
      case DOWN -> DirectionalCableBlockShapes.IMPORTER_DOWN;
    };
  }

  @Override
  public BaseBlockItem createBlockItem() {
    return new NetworkNodeBlockItem(this, HELP);
  }
}
