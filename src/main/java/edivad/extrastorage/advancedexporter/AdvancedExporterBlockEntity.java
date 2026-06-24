package edivad.extrastorage.advancedexporter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.refinedmods.refinedstorage.api.network.impl.node.exporter.ExporterNetworkNode;
import com.refinedmods.refinedstorage.api.network.node.exporter.ExporterTransferStrategy;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.exporter.ExporterTransferStrategyFactory;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.content.ContentNames;
import com.refinedmods.refinedstorage.common.exporter.ExporterData;
import com.refinedmods.refinedstorage.common.support.AbstractCableLikeBlockEntity;
import com.refinedmods.refinedstorage.common.support.AbstractDirectionalBlock;
import com.refinedmods.refinedstorage.common.support.FilterWithFuzzyMode;
import com.refinedmods.refinedstorage.common.support.SchedulingModeContainer;
import com.refinedmods.refinedstorage.common.support.SchedulingModeType;
import com.refinedmods.refinedstorage.common.support.containermenu.NetworkNodeExtendedMenuProvider;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicator;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicators;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerImpl;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeDestinations;
import com.refinedmods.refinedstorage.neoforge.support.render.ModelProperties;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;

public class AdvancedExporterBlockEntity extends AbstractCableLikeBlockEntity<ExporterNetworkNode>
    implements NetworkNodeExtendedMenuProvider<ExporterData> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdvancedExporterBlockEntity.class);
  private static final String TAG_UPGRADES = "upgr";

  private final UpgradeContainer upgradeContainer;
  private final FilterWithFuzzyMode filter;
  private final SchedulingModeContainer schedulingModeContainer;

  public AdvancedExporterBlockEntity(BlockPos pos, BlockState state) {
    super(ESBlockEntities.ADVANCED_EXPORTER.get(), pos, state,
        new ExporterNetworkNode(Platform.INSTANCE.getConfig().getExporter().getEnergyUsage()));
    this.upgradeContainer = new UpgradeContainer(UpgradeDestinations.EXPORTER, (c, upgradeEnergyUsage) -> {
      final long baseEnergyUsage = Platform.INSTANCE.getConfig().getExporter().getEnergyUsage();
      mainNetworkNode.setEnergyUsage(baseEnergyUsage + upgradeEnergyUsage);
      if (level instanceof ServerLevel serverLevel) {
        initialize(serverLevel);
      }
    }, this::setChanged);
    this.ticker = upgradeContainer.getTicker();
    this.schedulingModeContainer = new SchedulingModeContainer(
        mainNetworkNode::setSchedulingMode,
        this::setChanged
    );
    this.filter = FilterWithFuzzyMode.createAndListenForFilters(
        ResourceContainerImpl.createForFilter(18),
        this::setChanged,
        this::setFilters
    );
  }

  @Override
  public List<ItemStack> getUpgrades() {
    return upgradeContainer.getUpgrades();
  }

  @Override
  public boolean addUpgrade(final ItemStack upgradeStack) {
    return upgradeContainer.addUpgrade(upgradeStack);
  }

  @Override
  protected void initialize(final ServerLevel level, final Direction direction) {
    super.initialize(level, direction);
    final ExporterTransferStrategy strategy = createStrategy(level, direction);
    LOGGER.debug("Initialized exporter at {} with strategy {}", worldPosition, strategy);
    mainNetworkNode.setTransferStrategy(strategy);
  }

  private ExporterTransferStrategy createStrategy(final ServerLevel serverLevel, final Direction direction) {
    final Direction incomingDirection = direction.getOpposite();
    final BlockPos sourcePosition = worldPosition.relative(direction);
    final List<ExporterTransferStrategyFactory> factories =
        RefinedStorageApi.INSTANCE.getExporterTransferStrategyRegistry().getAll();
    final Map<Class<? extends ResourceKey>, ExporterTransferStrategy> strategies =
        factories.stream().collect(Collectors.toMap(
            ExporterTransferStrategyFactory::getResourceType,
            factory -> factory.create(
                serverLevel,
                sourcePosition,
                incomingDirection,
                upgradeContainer,
                filter.isFuzzyMode()
            )
        ));
    return new CompositeExporterTransferStrategy(strategies);
  }

  @Override
  public void saveAdditional(final ValueOutput output) {
    super.saveAdditional(output);
    output.store(TAG_UPGRADES, ItemContainerContents.CODEC,
        ItemContainerContents.fromItems(upgradeContainer.getItems()));
  }

  @Override
  public void loadAdditional(final ValueInput input) {
    input.read(TAG_UPGRADES, ItemContainerContents.CODEC).ifPresent(upgradeContainer::load);
    super.loadAdditional(input);
  }

  @Override
  public void writeConfiguration(final ValueOutput output) {
    super.writeConfiguration(output);
    schedulingModeContainer.store(output);
    filter.store(output);
  }

  @Override
  public void readConfiguration(final ValueInput input) {
    super.readConfiguration(input);
    schedulingModeContainer.read(input);
    filter.read(input);
  }

  public void setSchedulingModeType(final SchedulingModeType type) {
    schedulingModeContainer.setType(type);
  }

  public SchedulingModeType getSchedulingModeType() {
    return schedulingModeContainer.getType();
  }

  public boolean isFuzzyMode() {
    return filter.isFuzzyMode();
  }

  public void setFuzzyMode(final boolean fuzzyMode) {
    filter.setFuzzyMode(fuzzyMode);
    if (level instanceof ServerLevel serverLevel) {
      initialize(serverLevel);
    }
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    if (level != null) {
      Containers.dropContents(level, pos, upgradeContainer.getDrops());
    }
  }

  @Override
  public Component getName() {
    return overrideName(ContentNames.EXPORTER);
  }

  @Override
  public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
    return new AdvancedExporterContainerMenu(windowId, player, this, filter.getFilterContainer(), upgradeContainer, getExportingIndicators());
  }

  private ExportingIndicators getExportingIndicators() {
    return new ExportingIndicators(
        filter.getFilterContainer(),
        i -> toExportingIndicator(mainNetworkNode.getLastResult(i)),
        false
    );
  }

  private ExportingIndicator toExportingIndicator(@Nullable final ExporterTransferStrategy.Result result) {
    return switch (result) {
      case DESTINATION_DOES_NOT_ACCEPT -> ExportingIndicator.DESTINATION_DOES_NOT_ACCEPT_RESOURCE;
      case RESOURCE_MISSING -> ExportingIndicator.RESOURCE_MISSING;
      case AUTOCRAFTING_STARTED -> ExportingIndicator.AUTOCRAFTING_WAS_STARTED;
      case AUTOCRAFTING_MISSING_RESOURCES -> ExportingIndicator.AUTOCRAFTING_MISSING_RESOURCES;
      case null, default -> ExportingIndicator.NONE;
    };
  }

  @Override
  public ExporterData getMenuData() {
    final ResourceContainer filterContainer = filter.getFilterContainer();
    final ResourceContainerData resourceContainerData = ResourceContainerData.of(filterContainer);
    return new ExporterData(resourceContainerData, getExportingIndicators().getAll());
  }

  @Override
  public StreamEncoder<RegistryFriendlyByteBuf, ExporterData> getMenuCodec() {
    return ExporterData.STREAM_CODEC;
  }

  void setFilters(final List<ResourceKey> filters) {
    mainNetworkNode.setFilters(filters);
  }

  @Override
  protected boolean doesBlockStateChangeWarrantNetworkNodeUpdate(final BlockState oldBlockState,
      final BlockState newBlockState) {
    return AbstractDirectionalBlock.didDirectionChange(oldBlockState, newBlockState);
  }

  @Override
  public ModelData getModelData() {
    return ModelData.builder().with(ModelProperties.CABLE_CONNECTIONS, connections).build();
  }
}
