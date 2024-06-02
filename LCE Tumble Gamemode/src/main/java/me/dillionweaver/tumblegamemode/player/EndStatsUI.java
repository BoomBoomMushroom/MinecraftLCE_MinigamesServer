package me.dillionweaver.tumblegamemode.player;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.FunnySuperlatives;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import me.dillionweaver.tumblegamemode.data.TimeStuff;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class EndStatsUI {
    private final TumbleGamemode main;
    public EndStatsUI(TumbleGamemode main){
        this.main = main;
    }

    public void openEndStatsGUI(Player player){
        if(!main.isPlayerInGameWorld(player)){return;}
        String UUID = player.getUniqueId().toString();

        int slotCount = 54;
        int cols = 9;
        int rows = 6;

        ArrayList<Player> players = new ArrayList<>();
        for(Player plr : main.playersInWorld){
            players.add(plr);
        }

        int currentPlayerIndex = 0;

        Inventory endStatsGUI = main.server.createInventory(player, slotCount, TumbleConstants.endResultTitle);

        endStatsGUI.setItem(0, generateItemFromPlayer(player));

        for(int i=1;i<slotCount;i++) {
            int row = i / cols;
            int col = i % cols;

            ItemStack itemToSet = null;

            itemToSet = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i);
            ItemMeta itemMeta = itemToSet.getItemMeta();
            itemMeta.setItemName(row + ", " + col);
            itemToSet.setItemMeta(itemMeta);

            if(currentPlayerIndex >= players.size() || i == 1){
                // put with empty
                itemToSet = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                itemMeta = itemToSet.getItemMeta();
                itemMeta.setItemName(" ");
                itemToSet.setItemMeta(itemMeta);
            }
            else{
                // player head w/ info
                Player currentPlayer = players.get(currentPlayerIndex);
                if(currentPlayer.getUniqueId() == player.getUniqueId()){
                    i--;
                    currentPlayerIndex++;
                    continue;
                }

                itemToSet = generateItemFromPlayer(currentPlayer);
                currentPlayerIndex++;
            }

            endStatsGUI.setItem(i, itemToSet);
        }

        player.openInventory(endStatsGUI);
    }

    public ItemStack generateItemFromPlayer(Player currentPlayer){
        String currentPlayerUUID = currentPlayer.getUniqueId().toString();

        ItemStack itemToSet = getPlayerHead(currentPlayer);

        ItemMeta itemMeta = itemToSet.getItemMeta();
        itemMeta.setItemName(ChatColor.translateAlternateColorCodes('&', "&f&l" + currentPlayer.getDisplayName()));
        ArrayList<String> lore = new ArrayList<>();

        if(main.playerUUID_toDidPlayLastRound.get(currentPlayerUUID)) {
            long timeSurvivedMS = main.playerUUID_toTimedSurvivedInRoundMS.get(currentPlayerUUID);
            String timeSurvivedFormatted = TimeStuff.msToFormat(timeSurvivedMS);
            int blocksMined = main.playerUUID_toBlocksMinedInRound.get(currentPlayerUUID);
            int snowballsThrown = main.playerUUID_toSnowballsThrownInRound.get(currentPlayerUUID);

            lore.add("Time Survived: " + timeSurvivedFormatted);
            lore.add("Blocks Mined: " + blocksMined);
            lore.add("Snowballs Thrown: " + snowballsThrown);
        }
        else{
            lore.add(ChatColor.translateAlternateColorCodes('&', "&5Didn't play Last round "));
        }

        itemMeta.setLore(lore);
        itemToSet.setItemMeta(itemMeta);

        return itemToSet;
    }

    public ItemStack getPlayerHead(Player player){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }
}
