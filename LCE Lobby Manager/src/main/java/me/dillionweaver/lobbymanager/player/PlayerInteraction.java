package me.dillionweaver.lobbymanager.player;

import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class PlayerInteraction {
    private final LobbyManager main;
    public PlayerInteraction(LobbyManager main){
        this.main = main;
    }

    public void openLobbySwitchGUI(Player player){
        String UUID = player.getUniqueId().toString();

        int slotCount = 54;
        int cols = 9;
        int rows = 6;

        Object[] keys = main.fileSaveData.getDataFileKeys();
        int keyIndex = 0;

        Inventory lobbySwitchGUI = main.server.createInventory(player, slotCount, LobbyManagerConstants.lobbySwitchGuiName);
        for(int i=0;i<slotCount;i++){
            int row = i / cols;
            int col = i % cols;

            ItemStack itemToSet = null;

            itemToSet = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i);
            ItemMeta itemMeta = itemToSet.getItemMeta();
            itemMeta.setItemName(row + ", " + col);
            itemToSet.setItemMeta(itemMeta);

            boolean noItem = false;

            if(keyIndex >= keys.length){
                noItem = true;
            }
            else {

                String currentKey = (String) keys[keyIndex];
                if (Objects.equals(currentKey, LobbyManagerConstants.lobbyWorldKeyName) || currentKey.endsWith("_icon")) {
                    i--;
                    keyIndex++;
                    continue;
                }

                String materialName = (String) main.fileSaveData.getDataFileKey(currentKey+"_icon");
                Material material = Material.getMaterial(materialName);

                itemToSet = new ItemStack(material, 1);
                itemMeta = itemToSet.getItemMeta();
                itemMeta.setItemName(currentKey);
                itemToSet.setItemMeta(itemMeta);
                keyIndex++;
            }

            if (noItem) {
                itemToSet = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                itemMeta = itemToSet.getItemMeta();
                itemMeta.setItemName(" ");
                itemToSet.setItemMeta(itemMeta);
            }

            lobbySwitchGUI.setItem(i, itemToSet);
        }

        player.openInventory(lobbySwitchGUI);
    }
}
