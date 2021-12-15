package edivad.extrastorage.nodes;

import com.refinedmods.refinedstorage.api.storage.IStorage;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.ItemStorageWrapperStorageDisk;
import com.refinedmods.refinedstorage.apiimpl.network.node.storage.StorageNetworkNode;
import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.AdvancedStorageBlockEntity;
import edivad.extrastorage.items.item.ItemStorageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

    public AdvancedStorageNetworkNode(Level level, BlockPos pos, ItemStorageType type)
    {
        super(level, pos, null);
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
        return switch(type) {
            case TIER_5 -> BLOCK_256K_ID;
            case TIER_6 -> BLOCK_1024K_ID;
            case TIER_7 -> BLOCK_4096K_ID;
            case TIER_8 -> BLOCK_16384K_ID;
            default -> null;
        };
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
    public void loadStorage(@Nullable Player owner)
    {
        IStorageDisk disk = API.instance().getStorageDiskManager((ServerLevel) level).get(getStorageId());

        if (disk == null)
        {
            disk = API.instance().createDefaultItemDisk((ServerLevel) level, type.getCapacity(), owner);
            API.instance().getStorageDiskManager((ServerLevel) level).set(getStorageId(), disk);
            API.instance().getStorageDiskManager((ServerLevel) level).markForSaving();
        }

        this.storage = new ItemStorageWrapperStorageDisk(this, disk);
    }

    public IStorageDisk<ItemStack> getStorage()
    {
        return storage;
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("block." + Main.MODID + ".block_" + type.getName());
    }

    @Override
    public long getStored()
    {
        return AdvancedStorageBlockEntity.STORED.getValue();
    }

    @Override
    public long getCapacity()
    {
        return type.getCapacity();
    }
}
