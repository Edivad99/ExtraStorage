package edivad.extrastorage.blocks;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.CableBlock;
import com.refinedmods.refinedstorage.block.shape.ShapeCache;
import com.refinedmods.refinedstorage.container.factory.BlockEntityMenuProvider;
import com.refinedmods.refinedstorage.render.ConstantsCable;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.CollisionUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.blockentity.AdvancedExporterBlockEntity;
import edivad.extrastorage.container.AdvancedExporterContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class AdvancedExporterBlock extends CableBlock {

  private static final VoxelShape LINE_NORTH_1 = box(6.0, 6.0, 0.0, 10.0, 10.0, 2.0);
  private static final VoxelShape LINE_NORTH_2 = box(5.0, 5.0, 2.0, 11.0, 11.0, 4.0);
  private static final VoxelShape LINE_NORTH_3 = box(3.0, 3.0, 4.0, 13.0, 13.0, 6.0);
  private static final VoxelShape LINE_NORTH = Shapes.or(LINE_NORTH_1, LINE_NORTH_2, LINE_NORTH_3);
  private static final VoxelShape LINE_EAST_1 = box(14.0, 6.0, 6.0, 16.0, 10.0, 10.0);
  private static final VoxelShape LINE_EAST_2 = box(12.0, 5.0, 5.0, 14.0, 11.0, 11.0);
  private static final VoxelShape LINE_EAST_3 = box(10.0, 3.0, 3.0, 12.0, 13.0, 13.0);
  private static final VoxelShape LINE_EAST = Shapes.or(LINE_EAST_1, LINE_EAST_2, LINE_EAST_3);
  private static final VoxelShape LINE_SOUTH_1 = box(6.0, 6.0, 14.0, 10.0, 10.0, 16.0);
  private static final VoxelShape LINE_SOUTH_2 = box(5.0, 5.0, 12.0, 11.0, 11.0, 14.0);
  private static final VoxelShape LINE_SOUTH_3 = box(3.0, 3.0, 10.0, 13.0, 13.0, 12.0);
  private static final VoxelShape LINE_SOUTH = Shapes.or(LINE_SOUTH_1, LINE_SOUTH_2, LINE_SOUTH_3);
  private static final VoxelShape LINE_WEST_1 = box(0.0, 6.0, 6.0, 2.0, 10.0, 10.0);
  private static final VoxelShape LINE_WEST_2 = box(2.0, 5.0, 5.0, 4.0, 11.0, 11.0);
  private static final VoxelShape LINE_WEST_3 = box(4.0, 3.0, 3.0, 6.0, 13.0, 13.0);
  private static final VoxelShape LINE_WEST = Shapes.or(LINE_WEST_1, LINE_WEST_2, LINE_WEST_3);
  private static final VoxelShape LINE_UP_1 = box(6.0, 14.0, 6.0, 10.0, 16.0, 10.0);
  private static final VoxelShape LINE_UP_2 = box(5.0, 12.0, 5.0, 11.0, 14.0, 11.0);
  private static final VoxelShape LINE_UP_3 = box(3.0, 10.0, 3.0, 13.0, 12.0, 13.0);
  private static final VoxelShape LINE_UP = Shapes.or(LINE_UP_1, LINE_UP_2, LINE_UP_3);
  private static final VoxelShape LINE_DOWN_1 = box(6.0, 0.0, 6.0, 10.0, 2.0, 10.0);
  private static final VoxelShape LINE_DOWN_2 = box(5.0, 2.0, 5.0, 11.0, 4.0, 11.0);
  private static final VoxelShape LINE_DOWN_3 = box(3.0, 4.0, 3.0, 13.0, 6.0, 13.0);
  private static final VoxelShape LINE_DOWN = Shapes.or(LINE_DOWN_1, LINE_DOWN_2, LINE_DOWN_3);

  public AdvancedExporterBlock() {
    super(BlockUtils.DEFAULT_GLASS_PROPERTIES);
  }

  public BlockDirection getDirection() {
    return BlockDirection.ANY;
  }

  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos,
      CollisionContext ctx) {
    return ConstantsCable.addCoverVoxelShapes(ShapeCache.getOrCreate(state, s -> {
      VoxelShape shape = getCableShape(s);
      shape = Shapes.or(shape, getLineShape(s));
      return shape;
    }), world, pos);
  }

  protected VoxelShape getLineShape(BlockState state) {
    return switch (state.getValue(this.getDirection().getProperty())) {
      case UP -> LINE_UP;
      case DOWN -> LINE_DOWN;
      case NORTH -> LINE_NORTH;
      case SOUTH -> LINE_SOUTH;
      case EAST -> LINE_EAST;
      case WEST -> LINE_WEST;
    };
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedExporterBlockEntity(pos, state);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
      InteractionHand hand, BlockHitResult hit) {
    if (!level.isClientSide && CollisionUtils.isInBounds(getLineShape(state), pos,
        hit.getLocation())) {
      return NetworkUtils.attemptModify(level, pos, player, () -> NetworkHooks.openScreen(
          (ServerPlayer) player,
          new BlockEntityMenuProvider<AdvancedExporterBlockEntity>(
              Component.translatable(this.getDescriptionId()),
              (blockEntity, windowId, inventory, p) -> new AdvancedExporterContainerMenu(windowId,
                  player, blockEntity),
              pos
          ),
          pos
      ));
    }

    return InteractionResult.SUCCESS;
  }
}
