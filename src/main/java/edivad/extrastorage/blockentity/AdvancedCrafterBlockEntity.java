package edivad.extrastorage.blockentity;

import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.dataparameter.AdvancedCrafterTileDataParameterClientListener;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class AdvancedCrafterBlockEntity extends NetworkNodeBlockEntity<AdvancedCrafterNetworkNode> {

  public static final BlockEntitySynchronizationParameter<Integer, AdvancedCrafterBlockEntity> MODE =
      new BlockEntitySynchronizationParameter<>(
          ExtraStorage.rl("advanced_crafter_mode"),
          EntityDataSerializers.INT, AdvancedCrafterNetworkNode.CrafterMode.IGNORE.ordinal(),
          t -> t.getNode().getMode().ordinal(),
          (t, v) -> t.getNode().setMode(AdvancedCrafterNetworkNode.CrafterMode.getById(v)));
  private static final BlockEntitySynchronizationParameter<Boolean, AdvancedCrafterBlockEntity> HAS_ROOT =
      new BlockEntitySynchronizationParameter<>(
          ExtraStorage.rl("advanced_crafter_has_root"),
          EntityDataSerializers.BOOLEAN, false,
          t -> t.getNode().getRootContainerNotSelf().isPresent(),
          null, (t, v) -> new AdvancedCrafterTileDataParameterClientListener().onChanged(t, v));
  public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
      .addWatchedParameter(REDSTONE_MODE)
      .addWatchedParameter(MODE)
      .addParameter(HAS_ROOT)
      .build();
  private final CrafterTier tier;

  public AdvancedCrafterBlockEntity(CrafterTier tier, BlockPos pos, BlockState state) {
    super(ESBlockEntities.CRAFTER.get(tier).get(), pos, state, SPEC, AdvancedCrafterNetworkNode.class);
    this.tier = tier;
  }

  @Override
  public AdvancedCrafterNetworkNode createNode(Level level, BlockPos pos) {
    return new AdvancedCrafterNetworkNode(level, pos, tier);
  }

  public IItemHandler getPatterns(@Nullable Direction direction) {
    if (direction != null && !direction.equals(this.getNode().getDirection())) {
      return getNode().getPatternInventory();
    }
    return null;
  }

  public CrafterTier getTier() {
    return tier;
  }
}
