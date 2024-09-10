package me.dillionweaver.battlegamemode.player;

import me.dillionweaver.battlegamemode.BattleGamemode;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import me.dillionweaver.battlegamemode.data.TimeStuff;
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
    private final BattleGamemode main;
    public PlayerInteraction(BattleGamemode main){
        this.main = main;
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

        player.getInventory().setItem(BattleConstants.setReadyItemSlot, itemStack);

        main.checkForPlayersReady();
    }


    public HashMap<Integer, Integer> countVotes(){
        HashMap<Integer, Integer> mapIdToVotes = new HashMap<>();

        for(int i = 0; i< BattleConstants.idToName.length; i++){
            mapIdToVotes.put(i, 0);
        }

        for(Player player : main.playersInWorld){
            int votedMapId = main.playerUUID_toVotedMapId.get(player.getUniqueId().toString());
            mapIdToVotes.put(votedMapId, mapIdToVotes.get(votedMapId)+1);
        }
        return mapIdToVotes;
    }

    public void updateAllVotingGUIs(){
        for(Player player : main.playersInWorld){
            if(player.getOpenInventory().getTitle() == BattleConstants.votingMenuTitle){
                player.closeInventory();
                openVotingGUI(player);
            }
        }
    }

    public void openVotingGUI(Player player){
        if(!main.isPlayerInGameWorld(player)){return;}
        String UUID = player.getUniqueId().toString();

        if(main.hasGameStarted()){ return; }

        HashMap<Integer, Integer> mapIdToVotes = countVotes();

        int slotCount = 54;
        int cols = 9;
        int rows = 6;

        int availableMapIndex = 0;
        int votedMapId = main.playerUUID_toVotedMapId.get(UUID);

        Inventory votingGUI = main.server.createInventory(player, slotCount, BattleConstants.votingMenuTitle);
        for(int i=0;i<slotCount;i++){
            int row = i / cols;
            int col = i % cols;

            ItemStack itemToSet = null;

            itemToSet = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, i);
            ItemMeta itemMeta = itemToSet.getItemMeta();
            itemMeta.setItemName(row + ", " + col);
            itemToSet.setItemMeta(itemMeta);

            boolean noItem = false;
            String[] idToName = BattleConstants.idToName;

            if(availableMapIndex >= BattleConstants.votableMapIds.length){
                noItem = true;
            }
            else {
                if (row == 0) {
                    itemToSet = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                    itemMeta = itemToSet.getItemMeta();
                    String coloredItemName = ChatColor.translateAlternateColorCodes('&', "&cRemove Vote");
                    itemMeta.setItemName(coloredItemName);
                    itemMeta.setEnchantmentGlintOverride(votedMapId == 0);
                    itemToSet.setItemMeta(itemMeta);
                }
                else{
                    if (col != 0 && col != 8) {
                        int currentMapId = BattleConstants.votableMapIds[availableMapIndex];
                        int itemAmount = mapIdToVotes.get(currentMapId);
                        if(itemAmount == 0){itemAmount = 1;}

                        itemToSet = new ItemStack(Material.FILLED_MAP, itemAmount);
                        itemMeta = itemToSet.getItemMeta();
                        String str = idToName[currentMapId];
                        String capitalizedStr = str.substring(0, 1).toUpperCase() + str.substring(1);
                        String coloredItemName = ChatColor.translateAlternateColorCodes('&', "&l" + capitalizedStr);
                        itemMeta.setItemName(coloredItemName);
                        ArrayList<String> lores = new ArrayList<>();
                        lores.add("Votes: " + mapIdToVotes.get(currentMapId));

                        long bestTime = -1;
                        Object keyGet = main.fileSaveData.getDataFileKey(UUID+"_bestTime_"+currentMapId);
                        if(keyGet != null){
                            bestTime = Long.parseLong(keyGet.toString());
                        }
                        String bestTimeStr = bestTime==-1 ? "None" : TimeStuff.msToFormat(bestTime);
                        lores.add("Best Time: " + bestTimeStr);

                        itemMeta.setLore(lores);
                        itemMeta.setEnchantmentGlintOverride(votedMapId == currentMapId);
                        itemToSet.setItemMeta(itemMeta);
                        availableMapIndex++;
                    }
                    else{
                        noItem = true;
                    }
                }
            }

            if(noItem){
                itemToSet = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                itemMeta = itemToSet.getItemMeta();
                itemMeta.setItemName(" ");
                itemToSet.setItemMeta(itemMeta);
            }

            votingGUI.setItem(i, itemToSet);
        }

        player.openInventory(votingGUI);
    }
}
