package edivad.extrastorage.advancedexporter;

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

public class AdvancedExporterBlock extends AbstractDirectionalCableBlock implements EntityBlock,
    BlockItemProvider<BaseBlockItem> {

  private static final Component HELP = IdentifierUtil.createTranslation("item", "exporter.help");
  private static final ConcurrentHashMap<DirectionalCacheShapeCacheKey, VoxelShape> SHAPE_CACHE =
      new ConcurrentHashMap<>();
  private static final AbstractBlockEntityTicker<AdvancedExporterBlockEntity> TICKER =
      new NetworkNodeBlockEntityTicker<>(ESBlockEntities.ADVANCED_EXPORTER);

  public AdvancedExporterBlock() {
    super(SHAPE_CACHE);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedExporterBlockEntity(pos, state);
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
      case NORTH -> DirectionalCableBlockShapes.EXPORTER_NORTH;
      case EAST -> DirectionalCableBlockShapes.EXPORTER_EAST;
      case SOUTH -> DirectionalCableBlockShapes.EXPORTER_SOUTH;
      case WEST -> DirectionalCableBlockShapes.EXPORTER_WEST;
      case UP -> DirectionalCableBlockShapes.EXPORTER_UP;
      case DOWN -> DirectionalCableBlockShapes.EXPORTER_DOWN;
    };
  }

  @Override
  public BaseBlockItem createBlockItem() {
    return new NetworkNodeBlockItem(this, HELP);
  }
}
