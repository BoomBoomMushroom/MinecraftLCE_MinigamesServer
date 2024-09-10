package me.dillionweaver.battlegamemode.player;

import me.dillionweaver.battlegamemode.BattleGamemode;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import me.dillionweaver.battlegamemode.data.BattleHelperLists;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;

import static me.dillionweaver.battlegamemode.data.BattleConstants.cannotDropItems;

public class PlayerEvents implements Listener {

    private final BattleGamemode main;
    public PlayerEvents(BattleGamemode main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}

        main.playerOnJoin(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}

        main.playerOnLeft(player);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(!main.isPlayerInGameWorld((Player)event.getEntity())){return;}

        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event){
        if(event.getEntityType() == EntityType.PLAYER) { if (!main.isPlayerInGameWorld((Player)event.getEntity())) { return; } }

        if(event.getEntityType() != EntityType.PLAYER){ return; }

        if(main.canSetHealth){return;}

        event.setCancelled(true);
        event.setDamage(0);
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event){
        if(event.getEntityType() == EntityType.PLAYER){ if(!main.isPlayerInGameWorld((Player)event.getEntity())){return;} }

        event.setCancelled(!main.canSetHealth);
    }

    @EventHandler
    public void playerBreaksBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}

        if(!player.isOp()){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void openInventory(InventoryOpenEvent event){
        if(main.countdown != -1 || main.hasGameStarted()){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void blockInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}

        ItemStack holdingItem = player.getInventory().getItemInMainHand();

        Material itemType = holdingItem.getType();

        if (itemType == BattleConstants.adminSelectionMakerItem && player.isOp()) {
            event.setCancelled(true);
        }
        if (itemType == Material.MAP) {
            // open voting menu
            main.playerInteraction.openVotingGUI(player);
            event.setCancelled(true);
        }
        if (itemType == Material.EMERALD_BLOCK) {
            // ready
            main.playerInteraction.setReady(player, true);
            event.setCancelled(true);
        }
        if (itemType == Material.REDSTONE_BLOCK) {
            // unready
            main.playerInteraction.setReady(player, false);
            event.setCancelled(true);
        }
        if (itemType == Material.GOLD_INGOT) {
            // view results
            main.endStatsUI.openEndStatsGUI(player);
            event.setCancelled(true);
        }


        if(!player.isOp()){
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock==null){return;}
            Material blockType = clickedBlock.getType();
            if(blockType == Material.NOTE_BLOCK || blockType == Material.CHEST){}
            else{
                event.setCancelled(BattleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
            }
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(!player.isOp()){
            event.setCancelled(BattleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }
        if(!main.isPlayerInGameWorld(player)){return;}

        Material itemType = event.getItemDrop().getItemStack().getType();
        if(BattleHelperLists.bracketListIncludes(cannotDropItems, itemType)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!main.isPlayerInGameWorld((Player)event.getWhoClicked())){return;}

        if(event.getClickedInventory() == null){
            event.setCancelled(true);
            return;
        }

        if(!event.getWhoClicked().isOp()){
            event.setCancelled(BattleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }

        if(event.getView().getTitle() == BattleConstants.endResultTitle){
            event.setCancelled(true);
            return;
        }

        if(event.getView().getTitle() == BattleConstants.votingMenuTitle){
            event.setCancelled(true);

            int slot = event.getSlot();
            String itemName = event.getInventory().getItem(slot).getItemMeta().getItemName();
            if(itemName.length() <= 2){ return; }
            itemName = itemName.substring(2);

            Player player = (Player) event.getWhoClicked();
            String UUID = player.getUniqueId().toString();

            String prefix = BattleConstants.pluginMessagePrefix;

            if(itemName.equals("Remove Vote")){
                if (main.playerUUID_toVotedMapId.get(UUID) == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c"+prefix+"You don't have anything voted for!"));
                    return;
                }
                main.playerUUID_toVotedMapId.put(UUID, 0);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3"+prefix+"Successfully removed vote!"));
                main.playSoundEffect.playSoundEffectForPlayer(player, "minecraft:block.lever.click", SoundCategory.MASTER);
            }
            else {
                int mapId = Arrays.asList(BattleConstants.idToName).indexOf(itemName.toLowerCase());
                if (main.playerUUID_toVotedMapId.get(UUID) == mapId) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c"+prefix+"You already voted for "+ itemName +"!"));
                    return;
                }
                main.playerUUID_toVotedMapId.put(UUID, mapId);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3"+prefix+"Successfully voted for "+ itemName +"!"));
                main.playSoundEffect.playSoundEffectForPlayer(player, "minecraft:block.lever.click", SoundCategory.MASTER);
            }
            main.playerInteraction.updateAllVotingGUIs();

            return;
        }


        Object[] cannotSwitchSlots = new Object[]{
                102, // chest
                BattleConstants.mapVotingItemSlot,
                BattleConstants.setReadyItemSlot,
        };

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            event.setCancelled(true);
        }

        if(BattleHelperLists.bracketListIncludes(cannotSwitchSlots, event.getSlot())){
            if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        if(!main.isPlayerInGameWorld((Player)event.getWhoClicked())){return;}
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        Location fromLoc = event.getFrom();
        World fromWorld = fromLoc.getWorld();
        Location toLoc = event.getTo();
        World toWorld = toLoc.getWorld();

        boolean doWeOwnPlayer = main.playersInWorld.contains(player);
        boolean goingToOurWorld = toWorld.getUID() == main.battleWorld.getUID();
        boolean fromOurWorld = fromWorld.getUID() == main.battleWorld.getUID();

        //System.out.println(doWeOwnPlayer + ", " + goingToOurWorld + ", " + fromOurWorld);

        if(!fromOurWorld){
            if(goingToOurWorld && !doWeOwnPlayer){
                if(!main.hasGameStarted()) {
                    main.playerOnJoin(player);
                }
                else{
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3"+ BattleConstants.pluginMessagePrefix+"&cGame is already in session! Please wait until it is finished"));
                    event.setCancelled(true);
                    return;
                }
            }
            else{
                return;
            }
        }
        else{
            if(!goingToOurWorld){
                if(main.hasGameStarted()){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3"+ BattleConstants.pluginMessagePrefix+"&cPlease wait until the game is finished!"));
                    event.setCancelled(true);
                    return;
                }
                else {
                    main.playerOnLeft(player);
                }
            }
            else{
                return;
            }
        }
    }

    @EventHandler
    public void onItemSwapped(PlayerSwapHandItemsEvent event){
        if(!main.isPlayerInGameWorld(event.getPlayer())){return;}

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!main.isPlayerInGameWorld(event.getPlayer())){return;}

        if(main.countdown != -1){
            Location from = event.getFrom();
            from.setYaw(event.getTo().getYaw());
            from.setPitch(event.getTo().getPitch());
            event.setTo(from);
            //event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        String UUID = player.getUniqueId().toString();
        
    }
}
