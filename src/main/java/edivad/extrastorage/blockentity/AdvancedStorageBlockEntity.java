package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.api.storage.AccessType;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IAccessType;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IPrioritizable;
import com.refinedmods.refinedstorage.blockentity.config.IWhitelistBlacklist;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import com.refinedmods.refinedstorage.blockentity.data.RSSerializers;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import edivad.extrastorage.setup.ESBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedStorageBlockEntity extends NetworkNodeBlockEntity<AdvancedStorageNetworkNode> {

  public static final BlockEntitySynchronizationParameter<Integer, AdvancedStorageBlockEntity> PRIORITY =
      IPrioritizable.createParameter(ExtraStorage.rl("advanced_storage_priority"));
  public static final BlockEntitySynchronizationParameter<Integer, AdvancedStorageBlockEntity> COMPARE =
      IComparable.createParameter(ExtraStorage.rl("advanced_storage_compare"));
  public static final BlockEntitySynchronizationParameter<Integer, AdvancedStorageBlockEntity> WHITELIST_BLACKLIST =
      IWhitelistBlacklist.createParameter(ExtraStorage.rl("advanced_storage_whitelist_blacklist"));
  public static final BlockEntitySynchronizationParameter<AccessType, AdvancedStorageBlockEntity> ACCESS_TYPE =
      IAccessType.createParameter(ExtraStorage.rl("advanced_storage_access_type"));
  public static final BlockEntitySynchronizationParameter<Long, AdvancedStorageBlockEntity> STORED =
      new BlockEntitySynchronizationParameter<>(
          ExtraStorage.rl("advanced_storage_stored"),
          RSSerializers.LONG_SERIALIZER, 0L,
          t -> t.getNode().getStorage() != null ? (long) t.getNode().getStorage().getStored() : 0);
  public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
      .addWatchedParameter(REDSTONE_MODE)
      .addWatchedParameter(PRIORITY)
      .addWatchedParameter(COMPARE)
      .addWatchedParameter(WHITELIST_BLACKLIST)
      .addWatchedParameter(STORED)
      .addWatchedParameter(ACCESS_TYPE)
      .build();
  private final ItemStorageType type;

  public AdvancedStorageBlockEntity(ItemStorageType type, BlockPos pos, BlockState state) {
    super(ESBlockEntities.ITEM_STORAGE.get(type).get(), pos, state, SPEC, AdvancedStorageNetworkNode.class);
    this.type = type;
  }

  public ItemStorageType getItemStorageType() {
    return type;
  }

  @Override
  public AdvancedStorageNetworkNode createNode(Level level, BlockPos pos) {
    return new AdvancedStorageNetworkNode(level, pos, type);
  }
}
