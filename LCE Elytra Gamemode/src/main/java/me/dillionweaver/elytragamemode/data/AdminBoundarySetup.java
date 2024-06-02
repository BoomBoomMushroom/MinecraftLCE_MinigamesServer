package me.dillionweaver.elytragamemode.data;

import me.dillionweaver.elytragamemode.GlideGamemode;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminBoundarySetup {
    private final GlideGamemode main;
    public AdminBoundarySetup(GlideGamemode main){
        this.main = main;
    }

    public Location locationA = null;
    public Location locationB = null;

    public void updateLocationsAndPrint(Player player){
        if(locationA == null){ return; }
        if(locationB == null){ return; }


        String locA = locationA.getBlockX() + ", " + locationA.getBlockY() + ", " + locationA.getBlockZ();
        String locB = locationB.getBlockX() + ", " + locationB.getBlockY() + ", " + locationB.getBlockZ();
        String msg = locA + ",  " + locB;
        msg = "new int[] { "+ msg +" },";

        TextComponent message = new TextComponent(msg);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg));

        player.spigot().sendMessage(ChatMessageType.CHAT, message);

        locationA = null;
        locationB = null;
    }
}