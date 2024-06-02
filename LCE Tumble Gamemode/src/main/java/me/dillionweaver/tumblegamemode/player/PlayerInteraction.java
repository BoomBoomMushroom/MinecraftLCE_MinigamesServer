package me.dillionweaver.tumblegamemode.player;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInteraction {
    private final TumbleGamemode main;
    public PlayerInteraction(TumbleGamemode main){
        this.main = main;
    }

    public void adminMarkerInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Location blockLocation = event.getClickedBlock().getLocation();

        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            main.adminBoundarySetup.locationA = blockLocation;
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            main.adminBoundarySetup.locationB = blockLocation;
        }

        main.adminBoundarySetup.updateLocationsAndPrint(player);
    }

    public void setReady(Player player, boolean isReady){
        if(!main.isPlayerInGameWorld(player)){return;}
        main.playerUUID_toIsReady.put(player.getUniqueId().toString(), isReady);

        Material newItemMat = isReady ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK;
        ItemStack itemStack = new ItemStack(newItemMat, 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = "&l" + (isReady ? "&cUnready" : "&aReady");
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(TumbleConstants.setReadyItemSlot, itemStack);

        main.checkForPlayersReady();
    }
}
