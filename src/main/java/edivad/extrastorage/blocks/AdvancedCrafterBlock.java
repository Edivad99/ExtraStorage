package edivad.extrastorage.blocks;

import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.container.AdvancedCrafterContainer;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import edivad.extrastorage.tools.Translations;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedCrafterBlock extends NetworkNodeBlock
{
    private final CrafterTier tier;

    public AdvancedCrafterBlock(CrafterTier tier)
    {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
        this.tier = tier;
    }

    @Override
    public BlockDirection getDirection()
    {
        return BlockDirection.ANY_FACE_PLAYER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new AdvancedCrafterTile(tier);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote)
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof AdvancedCrafterTile && stack.hasDisplayName()) {
                ((AdvancedCrafterTile) tile).getNode().setDisplayName(stack.getDisplayName());
                ((AdvancedCrafterTile) tile).getNode().markDirty();
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            return NetworkUtils.attempt(worldIn, pos, player, () -> NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new PositionalTileContainerProvider<AdvancedCrafterTile>(
                            ((AdvancedCrafterTile) worldIn.getTileEntity(pos)).getNode().getName(),
                            (tile, windowId, inventory, p) -> new AdvancedCrafterContainer(windowId, player, tile),
                            pos
                    ),
                    pos
            ), Permission.MODIFY, Permission.AUTOCRAFTING);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasConnectedState()
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if(Screen.hasShiftDown())
        {
            tooltip.add(new TranslationTextComponent(Translations.SLOT_CRAFTING, tier.getSlots()).mergeStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent(Translations.BASE_SPEED, tier.getCraftingSpeed()).mergeStyle(TextFormatting.GREEN));
        }
        else
        {
            tooltip.add(new TranslationTextComponent(Translations.HOLD_SHIFT).mergeStyle(TextFormatting.GRAY));
        }
    }
}
