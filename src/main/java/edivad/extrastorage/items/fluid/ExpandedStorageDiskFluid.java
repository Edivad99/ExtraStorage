package edivad.extrastorage.items.fluid;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import edivad.extrastorage.items.ExpandedStorageDisk;
import edivad.extrastorage.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ExpandedStorageDiskFluid extends ExpandedStorageDisk
{
    private final FluidStorageType type;

    public ExpandedStorageDiskFluid(FluidStorageType type)
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
        return StorageType.FLUID;
    }

    public static Item getPartById(FluidStorageType type)
    {
        return Registration.FLUID_STORAGE_PART.get(type).get();
    }
}
