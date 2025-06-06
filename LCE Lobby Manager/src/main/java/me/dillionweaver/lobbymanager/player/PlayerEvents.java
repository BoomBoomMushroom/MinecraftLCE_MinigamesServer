package me.dillionweaver.lobbymanager.player;

import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import me.dillionweaver.lobbymanager.data.LobbyManagerHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

import static me.dillionweaver.lobbymanager.data.LobbyManagerConstants.cannotDropItems;

public class PlayerEvents implements Listener {
    private Map<String, Boolean> lastRegenerated = new HashMap();
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
        if(!main.isPlayerInLobbyBounds(player)){return;}

        main.playerOnLeft(player);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(!main.isPlayerInLobbyBounds((Player)event.getEntity())){return;}

        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event){
        if(event.getEntityType() == EntityType.PLAYER) {
            if (!main.isPlayerInLobbyBounds((Player)event.getEntity())) {
                return;
            }
        }

        event.setCancelled(true);
        event.setDamage(0);
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event){
        if(event.getEntityType() == EntityType.PLAYER){
            if(!main.isPlayerInLobbyBounds((Player)event.getEntity())){
                return;
            }
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event){
        if(main.isLocationInLobbyBounds(event.getEntity().getLocation())){ return; } // Not our world so idc
        if(event.getCause() == HangingBreakEvent.RemoveCause.ENTITY){
            // im assuming the entity breaking this is a player, only other possible one could be a ranged mob hitting it with their projectile but i don't see that happening.
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onManipulateArmorStand(PlayerArmorStandManipulateEvent event){
        Player player = event.getPlayer();
        if(main.isPlayerInLobbyBounds(player) == false){ return; } // idc they're not in our world

        if(event.getPlayer().isOp() == false){ // not admin
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerBreaksBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInLobbyBounds(player)){return;}

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

        // is the action is physical (like walking over something I presume) then its most likely a pressure plate or something so do not open the nether star gui
        Action playerAction = event.getAction();
        if(itemType == Material.NETHER_STAR && playerAction != Action.PHYSICAL){
            main.playerInteraction.openLobbySwitchGUI(player);
            event.setCancelled(true);
            return;
        }

        if(!main.isPlayerInLobbyBounds(player)){return;}
        if(playerAction == Action.RIGHT_CLICK_BLOCK){
            Material clickedBlockMaterial = event.getClickedBlock().getType();
            if(clickedBlockMaterial == Material.JUKEBOX && player.getGameMode() == GameMode.ADVENTURE){
                GameMode previousGameMode = player.getGameMode();
                player.setGameMode(GameMode.SURVIVAL);
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(this.main, new Runnable() {
                    @Override
                    public void run() {
                        // Your per-tick update logic here
                        Bukkit.getConsoleSender().sendMessage(player.getGameMode().toString());
                        Bukkit.getConsoleSender().sendMessage(previousGameMode.toString());
                        player.setGameMode( previousGameMode );
                        Bukkit.getConsoleSender().sendMessage(player.getGameMode().toString());
                    }
                }, 1L);

                event.setCancelled(false);
                return;
            }


            Material[] notAllowedToOpen = new Material[]{ Material.HOPPER, Material.DISPENSER };
            if(LobbyManagerHelper.bracketListIncludes(notAllowedToOpen, clickedBlockMaterial) && player.isOp()==false){
                event.setCancelled(true);
                return;
            }
        }

        if(player.isOp()){ return; }

        Block clickedBlock = event.getClickedBlock();
        if(clickedBlock==null){return;}
        Material blockType = clickedBlock.getType();
        Material[] allowedToInteract = new Material[]{
                Material.NOTE_BLOCK,
                Material.CHEST,
                Material.JUKEBOX,
        };
        if(LobbyManagerHelper.bracketListIncludes(allowedToInteract, blockType) == false){
            event.setCancelled(LobbyManagerConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event){
        if(main.isLocationInLobbyBounds(event.getBlock().getLocation())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        if(main.isLocationInLobbyBounds(event.getBlock().getLocation())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        Block block = event.getBlock();
        if(main.isLocationInLobbyBounds(block.getLocation())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(!main.isPlayerInLobbyBounds(player)){return;}
        if(!player.isOp()){
            event.setCancelled(LobbyManagerConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }

        Material itemType = event.getItemDrop().getItemStack().getType();
        if(LobbyManagerHelper.bracketListIncludes(cannotDropItems, itemType)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        boolean isPlayerInventory = clickedInventory.getType() == InventoryType.PLAYER;

        if(event.getView().getTitle().equals(LobbyManagerConstants.lobbySwitchGuiName)){
            int slotId = event.getSlot();
            ItemStack itemBeforeClick = clickedInventory.getItem(slotId);
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
            if (!main.isPlayerInLobbyBounds(player)) { return; }
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

        if(LobbyManagerHelper.bracketListIncludes(cannotSwitchSlots, event.getSlot()) && isPlayerInventory){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event){
        if(!main.isPlayerInLobbyBounds((Player)event.getWhoClicked())){return;}
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        Location toLoc = event.getTo();
        World toWorld = toLoc.getWorld();

        boolean isPlayerInOurWorld = event.getFrom().getWorld().getUID() == main.lobbyWorld.getUID();
        int preTeleportAmountOfPlayers = toWorld.getPlayers().size();

        // for some reason it calls the teleport twice so we need to flip flop between generating and not generating
        if(preTeleportAmountOfPlayers == 0){
            String worldName = toWorld.getName();
            boolean wasLastGenerated = false;
            if(lastRegenerated.get(worldName) != null) { wasLastGenerated = lastRegenerated.get(worldName); }
            lastRegenerated.put(worldName, !wasLastGenerated);
            if(wasLastGenerated == false){
                // we are the first people going to world so let's regenerate lobby
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "LMRegenerateLobby " + worldName);
            }
        }

        if(isPlayerInOurWorld == false){
            if(toWorld.getUID() == main.lobbyWorld.getUID()){
                main.playerOnJoin(player);
                main.playerQueueToLobby.add(player);
            }
            else{ return; }
        }
        else{
            if(toWorld.getUID() != main.lobbyWorld.getUID()){ main.playerOnLeft(player); }
            else{ return; }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(!main.isPlayerInLobbyBounds(event.getPlayer())){ return; }
    }
}
