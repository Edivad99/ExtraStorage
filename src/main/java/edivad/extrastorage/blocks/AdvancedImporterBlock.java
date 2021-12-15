package edivad.extrastorage.blocks;

import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.CableBlock;
import com.refinedmods.refinedstorage.block.shape.ShapeCache;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.render.ConstantsCable;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.CollisionUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.blockentity.AdvancedImporterBlockEntity;
import edivad.extrastorage.container.AdvancedImporterContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
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

import javax.annotation.Nullable;

public class AdvancedImporterBlock extends CableBlock
{
    private static final VoxelShape LINE_NORTH_1 = box(6, 6, 4, 10, 10, 6);
    private static final VoxelShape LINE_NORTH_2 = box(5, 5, 2, 11, 11, 4);
    private static final VoxelShape LINE_NORTH_3 = box(3, 3, 0, 13, 13, 2);
    private static final VoxelShape LINE_NORTH = Shapes.or(LINE_NORTH_1, LINE_NORTH_2, LINE_NORTH_3);
    private static final VoxelShape LINE_EAST_1 = box(10, 6, 6, 12, 10, 10);
    private static final VoxelShape LINE_EAST_2 = box(12, 5, 5, 14, 11, 11);
    private static final VoxelShape LINE_EAST_3 = box(14, 3, 3, 16, 13, 13);
    private static final VoxelShape LINE_EAST = Shapes.or(LINE_EAST_1, LINE_EAST_2, LINE_EAST_3);
    private static final VoxelShape LINE_SOUTH_1 = box(6, 6, 10, 10, 10, 12);
    private static final VoxelShape LINE_SOUTH_2 = box(5, 5, 12, 11, 11, 14);
    private static final VoxelShape LINE_SOUTH_3 = box(3, 3, 14, 13, 13, 16);
    private static final VoxelShape LINE_SOUTH = Shapes.or(LINE_SOUTH_1, LINE_SOUTH_2, LINE_SOUTH_3);
    private static final VoxelShape LINE_WEST_1 = box(4, 6, 6, 6, 10, 10);
    private static final VoxelShape LINE_WEST_2 = box(2, 5, 5, 4, 11, 11);
    private static final VoxelShape LINE_WEST_3 = box(0, 3, 3, 2, 13, 13);
    private static final VoxelShape LINE_WEST = Shapes.or(LINE_WEST_1, LINE_WEST_2, LINE_WEST_3);
    private static final VoxelShape LINE_UP_1 = box(6, 10, 6, 10, 12, 10);
    private static final VoxelShape LINE_UP_2 = box(5, 12, 5, 11, 14, 11);
    private static final VoxelShape LINE_UP_3 = box(3, 14, 3, 13, 16, 13);
    private static final VoxelShape LINE_UP = Shapes.or(LINE_UP_1, LINE_UP_2, LINE_UP_3);
    private static final VoxelShape LINE_DOWN_1 = box(6, 4, 6, 10, 6, 10);
    private static final VoxelShape LINE_DOWN_2 = box(5, 2, 5, 11, 4, 11);
    private static final VoxelShape LINE_DOWN_3 = box(3, 0, 3, 13, 2, 13);
    private static final VoxelShape LINE_DOWN = Shapes.or(LINE_DOWN_1, LINE_DOWN_2, LINE_DOWN_3);

    public AdvancedImporterBlock()
    {
        super(BlockUtils.DEFAULT_GLASS_PROPERTIES);
    }

    public BlockDirection getDirection()
    {
        return BlockDirection.ANY;
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
    {
        return ConstantsCable.addCoverVoxelShapes(ShapeCache.getOrCreate(state, s -> {
            VoxelShape shape = getCableShape(s);
            shape = Shapes.or(shape, this.getLineShape(s));
            return shape;
        }), world, pos);
    }

    protected VoxelShape getLineShape(BlockState state)
    {
        return switch(state.getValue(this.getDirection().getProperty())) {
            case UP -> LINE_UP;
            case DOWN -> LINE_DOWN;
            case NORTH -> LINE_NORTH;
            case SOUTH -> LINE_SOUTH;
            case EAST -> LINE_EAST;
            case WEST -> LINE_WEST;
            default -> Shapes.empty();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new AdvancedImporterBlockEntity(pos, state);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && CollisionUtils.isInBounds(getLineShape(state), pos, hit.getLocation())) {
            return NetworkUtils.attemptModify(level, pos, player, () -> NetworkHooks.openGui(
                    (ServerPlayer) player,
                    new PositionalTileContainerProvider<AdvancedImporterBlockEntity>(
                            new TranslatableComponent(this.getDescriptionId()),
                            (blockEntity, windowId, inventory, p) -> new AdvancedImporterContainer(windowId, player, blockEntity),
                            pos
                    ),
                    pos
            ));
        }

        return InteractionResult.SUCCESS;
    }
}
