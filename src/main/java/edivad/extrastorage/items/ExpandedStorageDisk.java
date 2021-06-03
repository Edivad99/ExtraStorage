package edivad.extrastorage.items;

import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.api.storage.StorageType;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.Styles;
import edivad.extrastorage.setup.ModSetup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public abstract class ExpandedStorageDisk extends Item implements IStorageDiskProvider
{
    private static final String ID = "ID";

    public ExpandedStorageDisk()
    {
        super(new Item.Properties().group(ModSetup.extraStorageTab).maxStackSize(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!worldIn.isRemote && !stack.hasTag() && entityIn instanceof PlayerEntity)
        {
            UUID id = UUID.randomUUID();
            ServerWorld serverWorld = (ServerWorld) worldIn;
            if(getType() == StorageType.ITEM)
                API.instance().getStorageDiskManager(serverWorld).set(id, API.instance().createDefaultItemDisk(serverWorld, this.getCapacity(stack), (PlayerEntity) entityIn));
            else if (getType() == StorageType.FLUID)
                API.instance().getStorageDiskManager(serverWorld).set(id, API.instance().createDefaultFluidDisk(serverWorld, this.getCapacity(stack), (PlayerEntity) entityIn));
            API.instance().getStorageDiskManager(serverWorld).markForSaving();
            setId(stack, id);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack diskStack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote && playerIn.isSneaking())
        {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            IStorageDisk disk = API.instance().getStorageDiskManager(serverWorld).getByStack(diskStack);
            if(disk != null && disk.getStored() == 0)
            {
                ItemStack part = new ItemStack(getPart(), diskStack.getCount());

                if(!playerIn.inventory.addItemStackToInventory(part.copy()))
                {
                    InventoryHelper.spawnItemStack(worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ(), part);
                }

                API.instance().getStorageDiskManager(serverWorld).remove(this.getId(diskStack));
                API.instance().getStorageDiskManager(serverWorld).markForSaving();

                return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(RSItems.STORAGE_HOUSING.get()));
            }

        }
        return new ActionResult<>(ActionResultType.PASS, diskStack);
    }

    @Override
    public void addInformation(ItemStack disk, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if(isValid(disk))
        {
            UUID id = this.getId(disk);
            API.instance().getStorageDiskSync().sendRequest(id);
            StorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);
            if(data != null)
            {
                if(data.getCapacity() == -1)
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                else
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
            }
            if(flagIn.isAdvanced())
                tooltip.add(new StringTextComponent(id.toString()).setStyle(Styles.GRAY));
        }
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public UUID getId(ItemStack disk)
    {
        return disk.getTag().getUniqueId(ID);
    }

    @Override
    public void setId(ItemStack disk, UUID id)
    {
        disk.getOrCreateTag().putUniqueId(ID, id);
    }

    @Override
    public boolean isValid(ItemStack disk)
    {
        return disk.hasTag() && disk.getTag().hasUniqueId(ID);
    }

    protected abstract Item getPart();
}
