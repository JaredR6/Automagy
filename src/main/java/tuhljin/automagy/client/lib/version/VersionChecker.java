package tuhljin.automagy.client.lib.version;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import tuhljin.automagy.common.lib.TjUtil;

public class VersionChecker {
    public static boolean checkComplete = false;
    public static String latestVersion = "";
    public static ArrayList<String> latestDesc = new ArrayList<>();

    public VersionChecker() {
        new VersionCheckerThread();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (checkComplete && event.phase == Phase.END && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.world != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;

            MinecraftForge.EVENT_BUS.unregister(this);
            if (latestVersion != null && !latestVersion.isEmpty() && !latestVersion.equals("2.0.3")) {
                String message = I18n.format("automagy.chat.newVersionAvailable", latestVersion, "2.0.3");
                TjUtil.sendRawChatToPlayer(player, message, null);
                if (latestDesc.size() > 0) {
                    int failsafe = 1;

                    for (String s : latestDesc) {
                        TjUtil.sendRawChatToPlayer(player, TextFormatting.ITALIC + s, TextFormatting.GRAY);
                        ++failsafe;
                        if (failsafe >= 10) {
                            break;
                        }
                    }
                }

                message = I18n.format("automagy.chat.newVersionLink");
                ITextComponent component = ITextComponent.Serializer.jsonToComponent(message);
                player.sendMessage(component);
            }
        }

    }
}
