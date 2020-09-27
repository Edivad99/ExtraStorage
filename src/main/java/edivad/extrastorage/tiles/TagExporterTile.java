//package edivad.expandedstorage.tiles;
//
//import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
//import edivad.expandedstorage.setup.Registration;
//import edivad.expandedstorage.tiles.node.TagExporterNode;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class TagExporterTile extends NetworkNodeTile<TagExporterNode>
//{
//    public TagExporterTile()
//    {
//        super(Registration.TAG_EXPORTER_TILE.get());
//    }
//
//    @Override
//    public TagExporterNode createNode(World world, BlockPos blockPos)
//    {
//        return new TagExporterNode(world, blockPos);
//    }
//}