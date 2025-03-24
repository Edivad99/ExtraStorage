package edivad.extrastorage.autocrafting.advancedautocrafter;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.content.BlockConstants;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.AbstractDirectionalBlock;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.BlockItemProvider;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.common.support.direction.DefaultDirectionType;
import com.refinedmods.refinedstorage.common.support.direction.DirectionType;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;
import edivad.extrastorage.setup.ESBlockEntities;
import edivad.extrastorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;


public class AdvancedAutocrafterBlock extends AbstractDirectionalBlock<Direction> implements BlockItemProvider<BaseBlockItem>,
    EntityBlock {

  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
  private static final Component HELP = createTranslation("item", "autocrafter.help");
  private final AbstractBlockEntityTicker<AdvancedAutocrafterBlockEntity> ticker;
  private final CrafterTier tier;

  public AdvancedAutocrafterBlock(CrafterTier tier) {
    super(BlockConstants.PROPERTIES);
    this.tier = tier;
    this.ticker = new NetworkNodeBlockEntityTicker<>(
        ESBlockEntities.CRAFTER.get(this.tier),
        ACTIVE
    );
  }

  @Override
  protected DirectionType<Direction> getDirectionType() {
    return DefaultDirectionType.FACE_CLICKED;
  }

  @Override
  protected BlockState getDefaultState() {
    return super.getDefaultState().setValue(ACTIVE, false);
  }

  @Override
  protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(ACTIVE);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new AdvancedAutocrafterBlockEntity(tier, pos, state);
  }

  @Nullable
  @Override
  public <O extends BlockEntity> BlockEntityTicker<O> getTicker(final Level level,
      final BlockState blockState,
      final BlockEntityType<O> type) {
    return ticker.get(level, type);
  }

  @Override
  public BaseBlockItem createBlockItem() {
    return new NetworkNodeBlockItem(this, HELP);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext context,
      List<Component> tooltip, TooltipFlag tooltipFlag) {
    if (Screen.hasShiftDown()) {
      tooltip.add(Component.translatable(Translations.SLOT_CRAFTING, tier.getSlots())
          .withStyle(ChatFormatting.GREEN));
      tooltip.add(Component.translatable(Translations.BASE_SPEED, tier.getCraftingSpeed())
          .withStyle(ChatFormatting.GREEN));
    } else {
      tooltip.add(Component.translatable(Translations.HOLD_SHIFT).withStyle(ChatFormatting.GRAY));
    }
  }
}
