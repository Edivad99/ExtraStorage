//package edivad.expandedstorage.container;
//
//import com.refinedmods.refinedstorage.container.BaseContainer;
//import edivad.expandedstorage.setup.Registration;
//import edivad.expandedstorage.tiles.TagExporterTile;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraftforge.items.SlotItemHandler;
//
//public class TagExporterContainer extends BaseContainer
//{
//    private final TagExporterTile tile;
//
//    public TagExporterContainer(int windowId, PlayerEntity player, TagExporterTile tile)
//    {
//        super(Registration.TAG_EXPORTER_CONTAINER.get(), tile, player, windowId);
//        this.tile = tile;
//        initSlots();
//    }
//
//    public void initSlots()
//    {
//        this.inventorySlots.clear();
//
//        this.transferManager.clearTransfers();
//
//        for (int i = 0; i < 4; ++i) {
//            addSlot(new SlotItemHandler(tile.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
//        }
//
//        addPlayerInventory(8, 55);
//
//        transferManager.addBiTransfer(getPlayer().inventory, tile.getNode().getUpgrades());;
//    }
//}
