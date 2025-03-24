package edivad.extrastorage.autocrafting.advancedautocrafter;

import static com.refinedmods.refinedstorage.common.support.AbstractDirectionalBlock.tryExtractDirection;

import java.util.function.Supplier;
import com.refinedmods.refinedstorage.common.api.support.network.ConnectionSink;
import com.refinedmods.refinedstorage.common.support.network.ColoredConnectionStrategy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

class AutocrafterConnectionStrategy extends ColoredConnectionStrategy {
  AutocrafterConnectionStrategy(final Supplier<BlockState> blockStateProvider,
      final BlockPos origin) {
    super(blockStateProvider, origin);
  }

  @Override
  public void addOutgoingConnections(final ConnectionSink sink) {
    final Direction myDirection = tryExtractDirection(blockStateProvider.get());
    if (myDirection == null) {
      super.addOutgoingConnections(sink);
      return;
    }
    for (final Direction direction : Direction.values()) {
      if (direction == myDirection) {
        sink.tryConnectInSameDimension(origin.relative(direction), direction.getOpposite(),
            AdvancedAutocrafterBlock.class);
      } else {
        sink.tryConnectInSameDimension(origin.relative(direction), direction.getOpposite());
      }
    }
  }

  @Override
  public boolean canAcceptIncomingConnection(final Direction incomingDirection, final BlockState connectingState) {
    if (!colorsAllowConnecting(connectingState)) {
      return false;
    }
    final Direction myDirection = tryExtractDirection(blockStateProvider.get());
    if (myDirection != null) {
      return myDirection != incomingDirection
          || connectingState.getBlock() instanceof AdvancedAutocrafterBlock;
    }
    return true;
  }
}
