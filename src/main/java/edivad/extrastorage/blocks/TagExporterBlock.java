//package edivad.expandedstorage.blocks;
//
//import edivad.expandedstorage.tiles.TagExporterTile;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.BlockRayTraceResult;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//
//import javax.annotation.Nullable;
//
//public class TagExporterBlock extends AdvancedExporter
//{
//    @Nullable
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TagExporterTile();
//    }
//
//    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
//    {
////        if(!world.isRemote && CollisionUtils.isInBounds(this.getLineShape(state), pos, hit.getHitVec()))
////        {
////            return NetworkUtils.attemptModify(world, pos, hit.getFace(), player, () ->
////            {
////                NetworkHooks.openGui((ServerPlayerEntity) player, new PositionalTileContainerProvider(new TranslationTextComponent("gui.refinedstorage.exporter"), (tile, windowId, inventory, p) ->
////                {
////                    return new TagExporterContainer(windowId, player, (TagExporterTile) tile);
////                }, pos), pos);
////            });
////        }
////        else
////        {
////            return ActionResultType.SUCCESS;
////        }
//        return ActionResultType.SUCCESS;
//    }
//}
