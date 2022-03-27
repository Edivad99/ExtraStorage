package edivad.extrastorage;

import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.ModSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main
{
    public static final String MODID = "extrastorage";
    public static final String MODNAME = "Extra Storage";

    public static final Logger logger = LogManager.getLogger();


    public Main()
    {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
        Registration.init();
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ModSetup::init);
        eventBus.addGenericListener(GlobalLootModifierSerializer.class, (RegistryEvent.Register event) -> ESLootFunctions.register());
    }
}
