package edivad.extrastorage.nodes;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.refinedmods.refinedstorage.api.storage.IStorage;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.FluidStorageNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.FluidStorageWrapperStorageDisk;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blockentity.AdvancedFluidStorageBlockEntity;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class AdvancedFluidStorageNetworkNode extends FluidStorageNetworkNode {

  public static final ResourceLocation BLOCK_FLUID_16384K_ID =
      ExtraStorage.rl("block_16384k_fluid");
  public static final ResourceLocation BLOCK_FLUID_65536K_ID =
      ExtraStorage.rl("block_65536k_fluid");
  public static final ResourceLocation BLOCK_FLUID_262144K_ID =
      ExtraStorage.rl("block_262144k_fluid");
  public static final ResourceLocation BLOCK_FLUID_1048576K_ID =
      ExtraStorage.rl("block_1048576k_fluid");

  private final FluidStorageType type;
  private IStorageDisk<FluidStack> storage;

  public AdvancedFluidStorageNetworkNode(Level level, BlockPos pos, FluidStorageType type) {
    super(level, pos, null);
    this.type = type;
  }

  @Override
  public int getEnergyUsage() {
    return 10 + (type.ordinal() * 2);
  }

  @Override
  public ResourceLocation getId() {
    return switch (type) {
      case TIER_5 -> BLOCK_FLUID_16384K_ID;
      case TIER_6 -> BLOCK_FLUID_65536K_ID;
      case TIER_7 -> BLOCK_FLUID_262144K_ID;
      case TIER_8 -> BLOCK_FLUID_1048576K_ID;
    };
  }

  @Override
  public void addFluidStorages(List<IStorage<FluidStack>> storages) {
    if (storage == null) {
      loadStorage(null);
    }

    storages.add(storage);
  }

  @Override
  public void loadStorage(@Nullable Player owner) {
    var disk = API.instance().getStorageDiskManager((ServerLevel) level).get(getStorageId());

    if (disk == null) {
      disk = API.instance().createDefaultFluidDisk((ServerLevel) level, type.getCapacity(), owner);
      API.instance().getStorageDiskManager((ServerLevel) level).set(getStorageId(), disk);
      API.instance().getStorageDiskManager((ServerLevel) level).markForSaving();
    }

    this.storage = new FluidStorageWrapperStorageDisk(this, disk);
  }

  public IStorageDisk<FluidStack> getStorage() {
    return storage;
  }

  @Override
  public Component getTitle() {
    var title = String.format("block.%s.block_%s_fluid", ExtraStorage.ID, type.getName());
    return Component.translatable(title);
  }

  @Override
  public long getStored() {
    return AdvancedFluidStorageBlockEntity.STORED.getValue();
  }

  @Override
  public long getCapacity() {
    return type.getCapacity();
  }
}
