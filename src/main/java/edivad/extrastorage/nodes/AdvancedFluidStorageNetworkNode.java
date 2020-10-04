package edivad.extrastorage.nodes;

import com.refinedmods.refinedstorage.api.storage.IStorage;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.FluidStorageNetworkNode;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.FluidStorageWrapperStorageDisk;
import edivad.extrastorage.Main;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.tiles.AdvancedFluidStorageBlockTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedFluidStorageNetworkNode extends FluidStorageNetworkNode
{
    public static final ResourceLocation BLOCK_FLUID_16384K_ID = new ResourceLocation(Main.MODID, "block_16384k_fluid");
    public static final ResourceLocation BLOCK_FLUID_65536K_ID = new ResourceLocation(Main.MODID, "block_65536k_fluid");
    public static final ResourceLocation BLOCK_FLUID_262144K_ID = new ResourceLocation(Main.MODID, "block_262144k_fluid");
    public static final ResourceLocation BLOCK_FLUID_1048576K_ID = new ResourceLocation(Main.MODID, "block_1048576k_fluid");

    private final FluidStorageType type;
    private IStorageDisk<FluidStack> storage;

    public AdvancedFluidStorageNetworkNode(World world, BlockPos pos, FluidStorageType type)
    {
        super(world, pos, null);
        this.type = type;
    }

    @Override
    public int getEnergyUsage()
    {
        return 10 + (type.ordinal() * 2);
    }

    @Override
    public ResourceLocation getId()
    {
        switch (type)
        {
            case TIER_5: return BLOCK_FLUID_16384K_ID;
            case TIER_6: return BLOCK_FLUID_65536K_ID;
            case TIER_7: return BLOCK_FLUID_262144K_ID;
            case TIER_8: return BLOCK_FLUID_1048576K_ID;
            default: return null;
        }
    }

    @Override
    public void addFluidStorages(List<IStorage<FluidStack>> storages)
    {
        if (storage == null) {
            loadStorage(null);
        }

        storages.add(storage);
    }

    @Override
    public void loadStorage(@Nullable PlayerEntity owner)
    {
        IStorageDisk disk = API.instance().getStorageDiskManager((ServerWorld) world).get(getStorageId());

        if (disk == null)
        {
            disk = API.instance().createDefaultFluidDisk((ServerWorld) world, type.getCapacity(), owner);
            API.instance().getStorageDiskManager((ServerWorld) world).set(getStorageId(), disk);
            API.instance().getStorageDiskManager((ServerWorld) world).markForSaving();
        }

        this.storage = new FluidStorageWrapperStorageDisk(this, disk);
    }

    public IStorageDisk<FluidStack> getStorage()
    {
        return storage;
    }

    @Override
    public ITextComponent getTitle()
    {
        return new TranslationTextComponent("block." + Main.MODID + ".block_" + type.getName() + "_fluid");
    }

    @Override
    public long getStored()
    {
        return AdvancedFluidStorageBlockTile.STORED.getValue();
    }

    @Override
    public long getCapacity()
    {
        return type.getCapacity();
    }
}
