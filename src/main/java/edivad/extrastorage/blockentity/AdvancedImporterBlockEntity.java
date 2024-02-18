package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.apiimpl.network.node.cover.CoverManager;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import com.refinedmods.refinedstorage.util.LevelUtils;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.nodes.AdvancedImporterNetworkNode;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class AdvancedImporterBlockEntity extends
    NetworkNodeBlockEntity<AdvancedImporterNetworkNode> {

  public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> COMPARE =
      IComparable.createParameter(ExtraStorage.rl("advanced_importer_compare"));
  public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> WHITELIST_BLACKLIST =
      IWhitelistBlacklist.createParameter(ExtraStorage.rl("advanced_importer_whitelist_blacklist"));
  public static final BlockEntitySynchronizationParameter<Integer, AdvancedImporterBlockEntity> TYPE =
      IType.createParameter(ExtraStorage.rl("advanced_importer_type"));

  public static final BlockEntitySynchronizationParameter<CompoundTag, AdvancedImporterBlockEntity> COVER_MANAGER =
      new BlockEntitySynchronizationParameter<>(
          ExtraStorage.rl("advanced_importer_cover_manager"),
          EntityDataSerializers.COMPOUND_TAG,
          new CompoundTag(),
          t -> t.getNode().getCoverManager().writeToNbt(),
          (t, v) -> t.getNode().getCoverManager().readFromNbt(v),
          (initial, p) -> {
          }
      );

  public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
      .addWatchedParameter(REDSTONE_MODE)
      .addWatchedParameter(COMPARE)
      .addWatchedParameter(WHITELIST_BLACKLIST)
      .addWatchedParameter(TYPE)
      .addWatchedParameter(COVER_MANAGER)
      .build();

  public AdvancedImporterBlockEntity(BlockPos pos, BlockState state) {
    super(ESBlockEntities.ADVANCED_IMPORTER.get(), pos, state, SPEC, AdvancedImporterNetworkNode.class);
  }

  @Override
  public AdvancedImporterNetworkNode createNode(Level level, BlockPos blockPos) {
    return new AdvancedImporterNetworkNode(level, blockPos);
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
    LevelUtils.updateBlock(level, worldPosition);  }
}
