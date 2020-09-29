package edivad.extrastorage.blocks;

import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.CableBlock;
import com.refinedmods.refinedstorage.block.shape.ShapeCache;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.CollisionUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.container.AdvancedExporterContainer;
import edivad.extrastorage.tiles.AdvancedExporterTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AdvancedExporter extends CableBlock
{
    private static final VoxelShape LINE_NORTH_1 = makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 2.0D);
    private static final VoxelShape LINE_NORTH_2 = makeCuboidShape(5.0D, 5.0D, 2.0D, 11.0D, 11.0D, 4.0D);
    private static final VoxelShape LINE_NORTH_3 = makeCuboidShape(3.0D, 3.0D, 4.0D, 13.0D, 13.0D, 6.0D);
    private static final VoxelShape LINE_NORTH = VoxelShapes.or(LINE_NORTH_1, new VoxelShape[]{LINE_NORTH_2, LINE_NORTH_3});
    private static final VoxelShape LINE_EAST_1 = makeCuboidShape(14.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape LINE_EAST_2 = makeCuboidShape(12.0D, 5.0D, 5.0D, 14.0D, 11.0D, 11.0D);
    private static final VoxelShape LINE_EAST_3 = makeCuboidShape(10.0D, 3.0D, 3.0D, 12.0D, 13.0D, 13.0D);
    private static final VoxelShape LINE_EAST = VoxelShapes.or(LINE_EAST_1, new VoxelShape[]{LINE_EAST_2, LINE_EAST_3});
    private static final VoxelShape LINE_SOUTH_1 = makeCuboidShape(6.0D, 6.0D, 14.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape LINE_SOUTH_2 = makeCuboidShape(5.0D, 5.0D, 12.0D, 11.0D, 11.0D, 14.0D);
    private static final VoxelShape LINE_SOUTH_3 = makeCuboidShape(3.0D, 3.0D, 10.0D, 13.0D, 13.0D, 12.0D);
    private static final VoxelShape LINE_SOUTH = VoxelShapes.or(LINE_SOUTH_1, new VoxelShape[]{LINE_SOUTH_2, LINE_SOUTH_3});
    private static final VoxelShape LINE_WEST_1 = makeCuboidShape(0.0D, 6.0D, 6.0D, 2.0D, 10.0D, 10.0D);
    private static final VoxelShape LINE_WEST_2 = makeCuboidShape(2.0D, 5.0D, 5.0D, 4.0D, 11.0D, 11.0D);
    private static final VoxelShape LINE_WEST_3 = makeCuboidShape(4.0D, 3.0D, 3.0D, 6.0D, 13.0D, 13.0D);
    private static final VoxelShape LINE_WEST = VoxelShapes.or(LINE_WEST_1, new VoxelShape[]{LINE_WEST_2, LINE_WEST_3});
    private static final VoxelShape LINE_UP_1 = makeCuboidShape(6.0D, 14.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape LINE_UP_2 = makeCuboidShape(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D);
    private static final VoxelShape LINE_UP_3 = makeCuboidShape(3.0D, 10.0D, 3.0D, 13.0D, 12.0D, 13.0D);
    private static final VoxelShape LINE_UP = VoxelShapes.or(LINE_UP_1, new VoxelShape[]{LINE_UP_2, LINE_UP_3});
    private static final VoxelShape LINE_DOWN_1 = makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 2.0D, 10.0D);
    private static final VoxelShape LINE_DOWN_2 = makeCuboidShape(5.0D, 2.0D, 5.0D, 11.0D, 4.0D, 11.0D);
    private static final VoxelShape LINE_DOWN_3 = makeCuboidShape(3.0D, 4.0D, 3.0D, 13.0D, 6.0D, 13.0D);
    private static final VoxelShape LINE_DOWN = VoxelShapes.or(LINE_DOWN_1, new VoxelShape[]{LINE_DOWN_2, LINE_DOWN_3});

    public AdvancedExporter()
    {
        super(BlockUtils.DEFAULT_GLASS_PROPERTIES);
    }

    public BlockDirection getDirection()
    {
        return BlockDirection.ANY;
    }

    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx)
    {
        return ShapeCache.getOrCreate(state, (s) ->
        {
            VoxelShape shape = getCableShape(s);
            shape = VoxelShapes.or(shape, this.getLineShape(s));
            return shape;
        });
    }

    protected VoxelShape getLineShape(BlockState state)
    {
        switch (state.get(this.getDirection().getProperty()))
        {
            case UP: return LINE_UP;
            case DOWN: return LINE_DOWN;
            case NORTH: return LINE_NORTH;
            case SOUTH: return LINE_SOUTH;
            case EAST: return LINE_EAST;
            case WEST: return LINE_WEST;
            default: return VoxelShapes.empty();
        }
    }

    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new AdvancedExporterTile();
    }

    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if(!world.isRemote && CollisionUtils.isInBounds(this.getLineShape(state), pos, hit.getHitVec()))
        {
            return NetworkUtils.attemptModify(world, pos, hit.getFace(), player, () ->
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, new PositionalTileContainerProvider(new TranslationTextComponent(this.getTranslationKey()), (tile, windowId, inventory, p) ->
                {
                    return new AdvancedExporterContainer(windowId, player, (AdvancedExporterTile) tile);
                }, pos), pos);
            });
        }
        return ActionResultType.SUCCESS;
    }
}
