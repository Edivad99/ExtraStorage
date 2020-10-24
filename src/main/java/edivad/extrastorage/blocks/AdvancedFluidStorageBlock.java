package edivad.extrastorage.blocks;

import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.container.AdvancedFluidStorageBlockContainer;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AdvancedFluidStorageBlock extends NetworkNodeBlock
{
    private final FluidStorageType type;

    public AdvancedFluidStorageBlock(FluidStorageType type)
    {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
        this.type = type;
    }

    public FluidStorageType getType()
    {
        return type;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(!worldIn.isRemote)
        {
            AdvancedFluidStorageNetworkNode storage = ((AdvancedFluidStorageBlockTile) worldIn.getTileEntity(pos)).getNode();
            if(stack.hasTag() && stack.getTag().hasUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID))
            {
                storage.setStorageId(stack.getTag().getUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID));
            }
            storage.loadStorage(placer instanceof PlayerEntity ? (PlayerEntity) placer : null);
        }
        // Call this after loading the storage, so the network discovery can use the loaded storage.
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new AdvancedFluidStorageBlockTile(type);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            return NetworkUtils.attemptModify(worldIn, pos, player, () -> NetworkHooks.openGui((ServerPlayerEntity) player, new PositionalTileContainerProvider<AdvancedFluidStorageBlockTile>(
                    ((AdvancedFluidStorageBlockTile) worldIn.getTileEntity(pos)).getNode().getTitle(),
                    (tile, windowId, inventory, p) -> new AdvancedFluidStorageBlockContainer(windowId, player, tile),
                    pos
            ), pos));
        }

        return ActionResultType.SUCCESS;
    }
}
