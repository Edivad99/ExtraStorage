package edivad.extrastorage.autocrafting.advancedautocrafter;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.common.content.BlockProperties;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.AbstractDirectionalBlock;
import com.refinedmods.refinedstorage.common.support.BaseBlockItem;
import com.refinedmods.refinedstorage.common.support.BlockItemProvider;
import com.refinedmods.refinedstorage.common.support.NetworkNodeBlockItem;
import com.refinedmods.refinedstorage.common.support.direction.DefaultDirectionType;
import com.refinedmods.refinedstorage.common.support.direction.DirectionType;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;
import edivad.extrastorage.setup.ESBlockEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
  @Getter
  private final CrafterTier tier;
  private final Identifier identifier;

  public AdvancedAutocrafterBlock(Identifier identifier, CrafterTier tier) {
    super(BlockProperties.stone(identifier));
    this.identifier = identifier;
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
    return new NetworkNodeBlockItem(identifier, this, HELP);
  }
}
