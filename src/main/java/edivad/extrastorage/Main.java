package edivad.extrastorage;

import edivad.extrastorage.setup.ESLootFunctions;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.setup.proxy.IProxy;
import edivad.extrastorage.setup.proxy.Proxy;
import edivad.extrastorage.setup.proxy.ProxyClient;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.ModSetup;
import net.minecraftforge.api.distmarker.Dist;
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

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ProxyClient::new, () -> Proxy::new);
    public static final Logger logger = LogManager.getLogger();


    public Main()
    {
        Registration.init();
        ESLootFunctions.register();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        });
    }
}
