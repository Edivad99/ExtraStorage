package edivad.extrastorage.nodes;

import com.refinedmods.refinedstorage.api.storage.IStorage;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.ItemStorageWrapperStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.StorageNetworkNode;
import edivad.extrastorage.Main;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.tiles.AdvancedStorageBlockTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedStorageNetworkNode extends StorageNetworkNode
{
    public static final ResourceLocation BLOCK_256K_ID = new ResourceLocation(Main.MODID, "block_256k");
    public static final ResourceLocation BLOCK_1024K_ID = new ResourceLocation(Main.MODID, "block_1024k");
    public static final ResourceLocation BLOCK_4096K_ID = new ResourceLocation(Main.MODID, "block_4096k");
    public static final ResourceLocation BLOCK_16384K_ID = new ResourceLocation(Main.MODID, "block_16384k");

    private final ItemStorageType type;
    private IStorageDisk<ItemStack> storage;

    public AdvancedStorageNetworkNode(World world, BlockPos pos, ItemStorageType type)
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
            case TIER_5: return BLOCK_256K_ID;
            case TIER_6: return BLOCK_1024K_ID;
            case TIER_7: return BLOCK_4096K_ID;
            case TIER_8: return BLOCK_16384K_ID;
            default: return null;
        }
    }

    @Override
    public void addItemStorages(List<IStorage<ItemStack>> storages)
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
            disk = API.instance().createDefaultItemDisk((ServerWorld) world, type.getCapacity(), owner);
            API.instance().getStorageDiskManager((ServerWorld) world).set(getStorageId(), disk);
            API.instance().getStorageDiskManager((ServerWorld) world).markForSaving();
        }

        this.storage = new ItemStorageWrapperStorageDisk(this, disk);
    }

    public IStorageDisk<ItemStack> getStorage()
    {
        return storage;
    }

    @Override
    public ITextComponent getTitle()
    {
        return new TranslationTextComponent("block." + Main.MODID + ".block_" + type.getName());
    }

    @Override
    public long getStored()
    {
        return AdvancedStorageBlockTile.STORED.getValue();
    }

    @Override
    public long getCapacity()
    {
        return type.getCapacity();
    }
}
