package edivad.extrastorage.setup;

import edivad.extrastorage.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.VersionChecker.CheckResult;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.forgespi.language.IModInfo;

public class EventHandler {

    public static final EventHandler INSTANCE = new EventHandler();

    @SubscribeEvent
    public void handlePlayerLoggedInEvent(LoggedInEvent event) {
        try
        {
            IModInfo modInfo = ModList.get().getModFileById(Main.MODID).getMods().get(0);
            if(modInfo.getVersion().getQualifier().contains("NONE"))
                return;
            CheckResult versionRAW = VersionChecker.getResult(modInfo);
            if(versionRAW.target() == null)
                return;

            Status result = versionRAW.status();
            LocalPlayer player = event.getPlayer();

            if(result.equals(Status.OUTDATED)) {
                player.displayClientMessage(new TextComponent(ChatFormatting.GREEN + "[" + Main.MODNAME + "] " + ChatFormatting.WHITE + "A new version is available (" + versionRAW.target() + "), please update!"), false);
                player.displayClientMessage(new TextComponent(ChatFormatting.YELLOW + "Changelog:"), false);

                String changes = versionRAW.changes().get(versionRAW.target());
                if(changes != null) {
                    String[] changesFormat = changes.split("\n");

                    for(var change : changesFormat) {
                        player.displayClientMessage(new TextComponent(ChatFormatting.WHITE + "- " + change), false);
                    }
                    if(versionRAW.changes().size() > 1) {
                        player.displayClientMessage(new TextComponent(ChatFormatting.WHITE + "- And more..."), false);
                    }
                }
            }
        } catch(Exception e) {
            Main.logger.warn("Unable to check the version", e);
        }
    }
}