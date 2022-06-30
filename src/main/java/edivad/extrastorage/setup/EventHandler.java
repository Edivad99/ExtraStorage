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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EventHandler {

    @SubscribeEvent
    public void handlePlayerLoggedInEvent(LoggedInEvent event) {
        try {
            IModInfo modInfo = ModList.get().getModFileById(Main.MODID).getMods().get(0);
            String qualifier = modInfo.getVersion().getQualifier();
            if(qualifier != null && qualifier.contains("NONE"))
                return;
            CheckResult versionRAW = VersionChecker.getResult(modInfo);
            if(versionRAW.target() == null)
                return;

            Status result = versionRAW.status();
            LocalPlayer player = event.getPlayer();

            List<String> messages = new ArrayList<>();
            if(result.equals(Status.OUTDATED) && versionRAW.changes().containsKey(versionRAW.target())) {
                String changes = versionRAW.changes().get(versionRAW.target());

                messages.add(ChatFormatting.GREEN + "[" + Main.MODNAME + "] " + ChatFormatting.WHITE + "A new version is available (" + versionRAW.target() + "), please update!");
                messages.add(ChatFormatting.YELLOW + "Changelog:");

                Arrays.stream(changes.split("\n"))
                        .map(change -> ChatFormatting.WHITE + "- " + change)
                        .collect(Collectors.toCollection(() -> messages));
                if(versionRAW.changes().size() > 1) {
                    messages.add(ChatFormatting.WHITE + "- And more...");
                }
            }
            messages.stream()
                    .map(TextComponent::new)
                    .forEach(message -> player.displayClientMessage(message, false));

        }
        catch(Exception e) {
            Main.LOGGER.warn("Unable to check the version", e);
        }
    }
}