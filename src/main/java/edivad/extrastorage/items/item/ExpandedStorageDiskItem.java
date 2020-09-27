package edivad.extrastorage.items.item;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import edivad.extrastorage.items.ExpandedStorageDisk;
import edivad.extrastorage.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ExpandedStorageDiskItem extends ExpandedStorageDisk
{
    private final ItemStorageType type;

    public ExpandedStorageDiskItem(ItemStorageType type)
    {
        super();
        this.type = type;
    }

    @Override
    protected Item getPart()
    {
        return getPartById(this.type);
    }

    @Override
    public int getCapacity(ItemStack itemStack)
    {
        return this.type.getCapacity();
    }

    @Override
    public StorageType getType()
    {
        return StorageType.ITEM;
    }

    public static Item getPartById(ItemStorageType type)
    {
        return Registration.ITEM_STORAGE_PART.get(type).get();
    }
}
