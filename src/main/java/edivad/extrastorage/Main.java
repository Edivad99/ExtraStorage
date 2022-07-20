package edivad.extrastorage;

import com.mojang.logging.LogUtils;
import edivad.extrastorage.setup.ClientSetup;
import edivad.extrastorage.setup.Config;
import edivad.extrastorage.setup.ModSetup;
import edivad.extrastorage.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main
{
    public static final String MODID = "extrastorage";
    public static final String MODNAME = "Extra Storage";

    public static final Logger LOGGER = LogUtils.getLogger();


    public Main()
    {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::onModelBake);
        });
        Registration.init();
        Config.init();

        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(ModSetup::init);
    }
}
