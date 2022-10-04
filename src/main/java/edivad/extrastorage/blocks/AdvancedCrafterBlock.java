package edivad.extrastorage.blocks;

import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.block.BlockDirection;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.BlockEntityMenuProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedCrafterBlock extends NetworkNodeBlock {
    private final CrafterTier tier;

    public AdvancedCrafterBlock(CrafterTier tier) {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
        this.tier = tier;
    }

    @Override
    public BlockDirection getDirection() {
        return BlockDirection.ANY_FACE_PLAYER;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvancedCrafterBlockEntity(tier, pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof AdvancedCrafterBlockEntity be && stack.hasCustomHoverName()) {
                be.getNode().setDisplayName(stack.getHoverName());
                be.getNode().markDirty();
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            return NetworkUtils.attempt(level, pos, player, () -> NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new BlockEntityMenuProvider<AdvancedCrafterBlockEntity>(
                            ((AdvancedCrafterBlockEntity) level.getBlockEntity(pos)).getNode().getName(),
                            (tile, windowId, inventory, p) -> new AdvancedCrafterContainerMenu(windowId, player, tile),
                            pos
                    ),
                    pos
            ), Permission.MODIFY, Permission.AUTOCRAFTING);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasConnectedState() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable(Translations.SLOT_CRAFTING, tier.getSlots()).withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable(Translations.BASE_SPEED, tier.getCraftingSpeed()).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable(Translations.HOLD_SHIFT).withStyle(ChatFormatting.GRAY));
        }
    }
}
