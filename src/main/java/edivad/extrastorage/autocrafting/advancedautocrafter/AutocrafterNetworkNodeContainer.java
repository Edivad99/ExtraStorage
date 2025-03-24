package edivad.extrastorage.autocrafting.advancedautocrafter;

import com.refinedmods.refinedstorage.api.network.node.NetworkNode;
import com.refinedmods.refinedstorage.common.api.autocrafting.Autocrafter;
import com.refinedmods.refinedstorage.common.api.support.network.ConnectionStrategy;
import com.refinedmods.refinedstorage.common.support.network.InWorldNetworkNodeContainerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;

class AutocrafterNetworkNodeContainer extends InWorldNetworkNodeContainerImpl implements Autocrafter {
  private final AdvancedAutocrafterBlockEntity blockEntity;

  AutocrafterNetworkNodeContainer(final AdvancedAutocrafterBlockEntity blockEntity,
      final NetworkNode node,
      final String name,
      final ConnectionStrategy connectionStrategy) {
    super(blockEntity, node, name, 0, connectionStrategy, null);
    this.blockEntity = blockEntity;
  }

  @Override
  public Component getAutocrafterName() {
    return blockEntity.getName();
  }

  @Override
  public Container getPatternContainer() {
    return blockEntity.getPatternContainer();
  }

  @Override
  public boolean isVisibleToTheAutocrafterManager() {
    return blockEntity.isVisibleToTheAutocrafterManager();
  }
}
