package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.apiimpl.network.node.cover.CoverManager;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import com.refinedmods.refinedstorage.util.LevelUtils;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class AdvancedExporterBlockEntity extends
    NetworkNodeBlockEntity<AdvancedExporterNetworkNode> {

  public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> COMPARE =
      IComparable.createParameter(ExtraStorage.rl("advanced_exporter_compare"));
  public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> TYPE =
      IType.createParameter(ExtraStorage.rl("advanced_exporter_type"));

  public static final BlockEntitySynchronizationParameter<CompoundTag, AdvancedExporterBlockEntity> COVER_MANAGER =
      new BlockEntitySynchronizationParameter<>(
          ExtraStorage.rl("advanced_exporter_cover_manager"),
          EntityDataSerializers.COMPOUND_TAG,
          new CompoundTag(),
          t -> t.getNode().getCoverManager().writeToNbt(),
          (t, v) -> t.getNode().getCoverManager().readFromNbt(v),
          (initial, p) -> {
          });

  public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
      .addWatchedParameter(REDSTONE_MODE)
      .addWatchedParameter(COMPARE)
      .addWatchedParameter(TYPE)
      .addWatchedParameter(COVER_MANAGER)
      .build();

  public AdvancedExporterBlockEntity(BlockPos pos, BlockState state) {
    super(ESBlockEntities.ADVANCED_EXPORTER.get(), pos, state, SPEC, AdvancedExporterNetworkNode.class);
  }

  @Override
  public AdvancedExporterNetworkNode createNode(Level level, BlockPos pos) {
    return new AdvancedExporterNetworkNode(level, pos);
  }

  @Override
  public ModelData getModelData() {
    return ModelData.builder().with(CoverManager.PROPERTY, this.getNode().getCoverManager()).build();
  }

  @Override
  public CompoundTag writeUpdate(CompoundTag tag) {
    super.writeUpdate(tag);
    tag.put(CoverManager.NBT_COVER_MANAGER, this.getNode().getCoverManager().writeToNbt());
    return tag;
  }

  @Override
  public void readUpdate(CompoundTag tag) {
    super.readUpdate(tag);
    this.getNode().getCoverManager().readFromNbt(tag.getCompound(CoverManager.NBT_COVER_MANAGER));
    requestModelDataUpdate();
    LevelUtils.updateBlock(level, worldPosition);
  }
}
