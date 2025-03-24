package edivad.extrastorage.advancedexporter;

import java.util.Map;
import java.util.Objects;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

class CompositeExporterTransferStrategy implements ExporterTransferStrategy {
  private final Map<Class<? extends ResourceKey>, ExporterTransferStrategy> strategies;
  private final ExporterTransferStrategy fastItemStrategy;
  private final ExporterTransferStrategy fastFluidStrategy;

  CompositeExporterTransferStrategy(final Map<Class<? extends ResourceKey>,
      ExporterTransferStrategy> strategies) {
    this.strategies = strategies;
    this.fastItemStrategy = Objects.requireNonNull(strategies.get(ItemResource.class));
    this.fastFluidStrategy = Objects.requireNonNull(strategies.get(FluidResource.class));
  }

  @Override
  public Result transfer(final ResourceKey resource, final Actor actor, final Network network) {
    final Class<? extends ResourceKey> resourceClass = resource.getClass();
    if (resourceClass == ItemResource.class) {
      return fastItemStrategy.transfer(resource, actor, network);
    }
    if (resourceClass == FluidResource.class) {
      return fastFluidStrategy.transfer(resource, actor, network);
    }
    final ExporterTransferStrategy strategy = strategies.get(resourceClass);
    return strategy == null ? Result.DESTINATION_DOES_NOT_ACCEPT : strategy.transfer(resource, actor, network);
  }
}
