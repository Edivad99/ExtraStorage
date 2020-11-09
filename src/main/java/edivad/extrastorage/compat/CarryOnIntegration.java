package edivad.extrastorage.compat;

import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.items.item.ItemStorageType;
import edivad.extrastorage.setup.Registration;
import net.minecraft.block.Block;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class CarryOnIntegration
{
    /**
     * Added this method to avoid unpleasant surprises using the Carry On.
     * To be removed when the Carry On gives the possibility to move these blocks without
     * causing problems.
     */
    public static void registerCarryOn()
    {
        if(ModList.get().isLoaded("carryon"))
        {
            for(CrafterTier tier : CrafterTier.values())
                denyBlockCarryOn(Registration.CRAFTER_BLOCK.get(tier).get());
            for(ItemStorageType type : ItemStorageType.values())
                denyBlockCarryOn(Registration.ITEM_STORAGE_BLOCK.get(type).get());
            for(FluidStorageType type : FluidStorageType.values())
                denyBlockCarryOn(Registration.FLUID_STORAGE_BLOCK.get(type).get());
            denyBlockCarryOn(Registration.ADVANCED_IMPORTER.get());
            denyBlockCarryOn(Registration.ADVANCED_EXPORTER.get());
        }
    }
    private static void denyBlockCarryOn(Block block)
    {
        String registryName = block.getRegistryName().toString();
        InterModComms.sendTo("carryon", "blacklistBlock", () -> registryName);
        Main.logger.debug(Main.MODNAME + " made it impossible to use Carry On on this block -> " + registryName);
    }
}
