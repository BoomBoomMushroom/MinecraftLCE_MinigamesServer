package me.dillionweaver.battlegamemode.player;

import me.dillionweaver.battlegamemode.BattleGamemode;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import me.dillionweaver.battlegamemode.data.TimeStuff;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class EndStatsUI {
    private final BattleGamemode main;
    public EndStatsUI(BattleGamemode main){
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

        Inventory endStatsGUI = main.server.createInventory(player, slotCount, BattleConstants.endResultTitle);

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
            long timeCompleted = main.playerUUID_toTimeCompleted.get(currentPlayerUUID);
            int deaths = main.playerUUID_toDeathsInRound.get(currentPlayerUUID);
            int damage = main.playerUUID_toDamageInRound.get(currentPlayerUUID);

            long bestTimeThisMap = -1L;

            Object bestFromLastMap = main.fileSaveData.getDataFileKey(currentPlayerUUID + "_bestTime_" + main.lastMapId);
            if(bestFromLastMap == null){
                bestFromLastMap = -1;
            } else{
                bestTimeThisMap = Long.parseLong(bestFromLastMap.toString());
            }

            boolean wasBestTime = main.playerUUID_toTimeCompleted.get(currentPlayerUUID) <= bestTimeThisMap;

            String rank = "F";

            String finishedTime = "DNF";
            if (timeCompleted != 0L) {
                finishedTime = TimeStuff.msToFormat(timeCompleted);

                long AVERAGE_TIME = BattleConstants.mapIdToAverageTimeToCompleteMS[main.lastMapId];
                rank = CalculateRank.getRank(damage, timeCompleted, AVERAGE_TIME);
            }

            lore.add(ChatColor.translateAlternateColorCodes('&', "&fRank: &l" + BattleConstants.rankToColorCode.get(rank) + rank));
            if(wasBestTime){
                lore.add(ChatColor.translateAlternateColorCodes('&', "&6&lPersonal Best Time!"));
            }
            lore.add(ChatColor.translateAlternateColorCodes('&', "Time: " + finishedTime));
            lore.add(ChatColor.translateAlternateColorCodes('&', "Deaths: " + deaths));
            lore.add(ChatColor.translateAlternateColorCodes('&', "Damage: " + damage));
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
