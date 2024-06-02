package me.dillionweaver.lobbymanager.player;

import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import me.dillionweaver.lobbymanager.data.LobbyManagerHelperLists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import static me.dillionweaver.lobbymanager.data.LobbyManagerConstants.cannotDropItems;

public class PlayerEvents implements Listener {

    private final LobbyManager main;
    public PlayerEvents(LobbyManager main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        main.teleportPlayerToLobby(player);
        //if(!main.isPlayerInGameWorld(player)){return;}

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
        if(event.getEntityType() == EntityType.PLAYER) {
            if (!main.isPlayerInGameWorld((Player)event.getEntity())) {
                return;
            }
        }

        event.setCancelled(true);
        event.setDamage(0);
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event){
        if(event.getEntityType() == EntityType.PLAYER){
            if(!main.isPlayerInGameWorld((Player)event.getEntity())){
                return;
            }
        }
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
    public void blockInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();

        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        Material itemType = holdingItem.getType();

        if(itemType == Material.NETHER_STAR){
            main.playerInteraction.openLobbySwitchGUI(player);
            event.setCancelled(true);
            return;
        }

        if(!main.isPlayerInGameWorld(player)){return;}


        if(!player.isOp()){
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock==null){return;}
            Material blockType = clickedBlock.getType();
            if(blockType == Material.NOTE_BLOCK || blockType == Material.CHEST){}
            else{
                event.setCancelled(LobbyManagerConstants.stopPlayerFromInteractingWithWhateverTheyWant);
            }
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event){
        if(event.getBlock().getWorld().getUID() == main.lobbyWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        if(event.getBlock().getWorld().getUID() == main.lobbyWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        Block block = event.getBlock();
        if(block.getWorld().getUID() == main.lobbyWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(!player.isOp()){
            event.setCancelled(LobbyManagerConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }
        if(!main.isPlayerInGameWorld(player)){return;}

        Material itemType = event.getItemDrop().getItemStack().getType();
        if(LobbyManagerHelperLists.bracketListIncludes(cannotDropItems, itemType)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        if(event.getView().getTitle().equals(LobbyManagerConstants.lobbySwitchGuiName)){
            int slotId = event.getSlot();
            ItemStack itemBeforeClick = event.getClickedInventory().getItem(slotId);
            if(itemBeforeClick != null) {
                String itemName = itemBeforeClick.getItemMeta().getItemName();
                String worldName = (String) main.fileSaveData.getDataFileKey(itemName);
                if(worldName != null) {
                    WorldCreator creator = new WorldCreator(worldName);
                    World toWorld = main.server.createWorld(creator);
                    Location playerLoc = player.getLocation();
                    playerLoc.setY(60);
                    playerLoc.setWorld(toWorld);
                    player.teleport(playerLoc);
                    event.setCancelled(true);
                }
            }

            event.setCancelled(true);
            return;
        }
        else {
            if (!main.isPlayerInGameWorld(player)) { return; }
        }



        if(!player.isOp()){
            event.setCancelled(LobbyManagerConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }

        Object[] cannotSwitchSlots = new Object[]{
                LobbyManagerConstants.setLobbyHopItemSlot,
        };

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            event.setCancelled(true);
        }

        if(LobbyManagerHelperLists.bracketListIncludes(cannotSwitchSlots, event.getSlot())){
            event.setCancelled(true);
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
        Location toLoc = event.getTo();
        World toWorld = toLoc.getWorld();

        boolean doWeOwnPlayer = main.playersInWorld.contains(player);

        if(!main.isPlayerInGameWorld(player)){
            if(toWorld.getUID() == main.lobbyWorld.getUID() && !doWeOwnPlayer){
                main.playerOnJoin(player);
                main.playerQueueToLobby.add(player);
            }
            else{
                return;
            }
        }
        else{
            if(toWorld.getUID() != main.lobbyWorld.getUID()){
                main.playerOnLeft(player);
            }
            else{
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!main.isPlayerInGameWorld(event.getPlayer())){ return; }
    }
}
