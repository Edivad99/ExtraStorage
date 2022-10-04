package edivad.extrastorage.items.storage;

import com.refinedmods.refinedstorage.RSBlocks;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import com.refinedmods.refinedstorage.render.Styles;
import edivad.extrastorage.blocks.AdvancedStorageBlock;
import edivad.extrastorage.items.storage.item.ExpandedStorageDiskItem;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import edivad.extrastorage.nodes.AdvancedStorageNetworkNode;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.UUID;

public class AdvancedStorageBlockItem extends BaseBlockItem {
    private final ItemStorageType type;

    public AdvancedStorageBlockItem(AdvancedStorageBlock block, Item.Properties builder) {
        super(block, builder);
        this.type = block.getType();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (isValid(stack)) {
            UUID id = getId(stack);

            API.instance().getStorageDiskSync().sendRequest(id);

            StorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);
            if (data != null) {
                if (data.getCapacity() == -1)
                    tooltip.add(Component.translatable("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                else
                    tooltip.add(Component.translatable("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
            }

            if (flag.isAdvanced())
                tooltip.add(Component.literal(id.toString()).setStyle(Styles.GRAY));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack storageStack = player.getItemInHand(hand);

        if (!level.isClientSide && player.isCrouching()) {
            UUID diskId = null;
            IStorageDisk disk = null;

            if (isValid(storageStack)) {
                diskId = getId(storageStack);
                disk = API.instance().getStorageDiskManager((ServerLevel) level).get(diskId);
            }

            // Newly created storages won't have a tag yet, so allow invalid disks as well.
            if (disk == null || disk.getStored() == 0) {
                ItemStack storagePart = new ItemStack(ExpandedStorageDiskItem.getPartById(type));

                if (!player.getInventory().add(storagePart.copy()))
                    Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), storagePart);

                if (disk != null) {
                    API.instance().getStorageDiskManager((ServerLevel) level).remove(diskId);
                    API.instance().getStorageDiskManager((ServerLevel) level).markForSaving();
                }

                return new InteractionResultHolder<>(InteractionResult.SUCCESS, new ItemStack(RSBlocks.MACHINE_CASING.get()));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, storageStack);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    private UUID getId(ItemStack disk) {
        return disk.getTag().getUUID(AdvancedStorageNetworkNode.NBT_ID);
    }

    private boolean isValid(ItemStack disk) {
        return disk.hasTag() && disk.getTag().hasUUID(AdvancedStorageNetworkNode.NBT_ID);
    }
}
