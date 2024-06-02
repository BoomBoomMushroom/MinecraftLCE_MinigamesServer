package me.dillionweaver.tumblegamemode.player;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.AreaMath;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import me.dillionweaver.tumblegamemode.data.TumbleHelperLists;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static me.dillionweaver.tumblegamemode.data.TumbleConstants.cannotDropItems;

public class PlayerEvents implements Listener {

    private final TumbleGamemode main;
    public PlayerEvents(TumbleGamemode main){
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
        if(event.getEntityType() == EntityType.PLAYER) {
            if (!main.isPlayerInGameWorld((Player)event.getEntity())) {
                return;
            }
        }

        Entity entity = event.getEntity();
        if(event.getEntityType() == EntityType.ITEM_FRAME){
            event.setCancelled(true);
            return;
        }
        if(event.getEntityType() == EntityType.PLAYER){
            event.setCancelled(true);
            return;
        }
        if(event.getCause() == EntityDamageEvent.DamageCause.KILL){
            event.setCancelled(false);
            event.getEntity().remove();
            return;
        }
        if(entity.isInvulnerable()){
            event.setCancelled(true);
            return;
        }


        event.setCancelled(false);
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
    public void onBlockDamaged(BlockDamageEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}
    }

    @EventHandler
    public void blockInteraction(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){return;}

        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        Material itemType = holdingItem.getType();

        if(itemType == TumbleConstants.adminSelectionMakerItem && player.isOp()){
            main.playerInteraction.adminMarkerInteraction(event);
            event.setCancelled(true);
        }
        if(itemType == Material.EMERALD_BLOCK){
            // ready
            main.playerInteraction.setReady(player, true);
            event.setCancelled(true);
        }
        if(itemType == Material.REDSTONE_BLOCK){
            // unready
            main.playerInteraction.setReady(player, false);
            event.setCancelled(true);
        }
        if(itemType == Material.GOLD_INGOT){
            // view results
            main.endStatsUI.openEndStatsGUI(player);
            event.setCancelled(true);
        }

        if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(itemType == Material.DIAMOND_SHOVEL){
                if(main.playerUUID_toBreakCooldownTicks.get(player.getUniqueId().toString()) <= 0){
                    int maxRadius = TumbleConstants.maxRadius + 2; // add a margin to catch any stray blocks!
                    Location start = new Location(main.tumbleWorld, -maxRadius, TumbleConstants.fillCornerB[1], -maxRadius);
                    Location end = new Location(main.tumbleWorld, maxRadius, TumbleConstants.fillCornerA[1], maxRadius);

                    Block block = event.getClickedBlock();

                    if(!AreaMath.locationInArea(block.getLocation(), start, end)){
                        event.setCancelled(true);
                        return;
                    }
                    main.destroyBlockInWorld(block);
                    String UUID = player.getUniqueId().toString();
                    int newBlocksMinedCount = main.playerUUID_toBlocksMinedInRound.get(UUID) + 1;
                    main.playerUUID_toBlocksMinedInRound.put(UUID, newBlocksMinedCount);

                    //main.playerUUID_toBreakCooldownTicks.put(player.getUniqueId().toString(), 6); // creative speed, feels slow
                    main.playerUUID_toBreakCooldownTicks.put(UUID, 2);
                }
            }
        }

        LivingEntity playerSpecEntity = (LivingEntity) main.playerSpectating.playerToSpecAnimal.get(player);
        boolean didRightClick = event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR;
        if(playerSpecEntity != null && didRightClick){
            Sound entitySound = main.playerSpectating.entityToSound.get(playerSpecEntity.getType());
            main.tumbleWorld.playSound(playerSpecEntity.getLocation(), entitySound, 3, 1);
        }

        if(!player.isOp()){
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock==null){return;}
            Material blockType = clickedBlock.getType();
            if(blockType == Material.NOTE_BLOCK || blockType == Material.CHEST){}
            else{
                event.setCancelled(TumbleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        Player player = (Player) event.getPlayer();
        if(main.isPlayerInGameWorld(player)){return;}

        if(main.countdown != -1 || main.hasGameStarted()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event){
        if(event.getBlock().getWorld().getUID() == main.tumbleWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        if(event.getBlock().getWorld().getUID() == main.tumbleWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        Block block = event.getBlock();
        if(block.getWorld().getUID() == main.tumbleWorld.getUID()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        if(!player.isOp()){
            event.setCancelled(TumbleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }
        if(!main.isPlayerInGameWorld(player)){return;}

        Material itemType = event.getItemDrop().getItemStack().getType();
        if(TumbleHelperLists.bracketListIncludes(cannotDropItems, itemType)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!main.isPlayerInGameWorld((Player)event.getWhoClicked())){return;}

        if(!event.getWhoClicked().isOp()){
            event.setCancelled(TumbleConstants.stopPlayerFromInteractingWithWhateverTheyWant);
        }

        if(event.getView().getTitle() == TumbleConstants.endResultTitle){
            event.setCancelled(true);
            return;
        }

        Object[] cannotSwitchSlots = new Object[]{
                102, // chest
                TumbleConstants.setReadyItemSlot,
        };

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            event.setCancelled(true);
        }

        if(TumbleHelperLists.bracketListIncludes(cannotSwitchSlots, event.getSlot())){
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
        Location fromLoc = event.getFrom();
        World fromWorld = fromLoc.getWorld();

        Location toLoc = event.getTo();
        World toWorld = toLoc.getWorld();

        boolean doWeOwnPlayer = main.playersInWorld.contains(player);
        boolean goingToOurWorld = toWorld.getUID() == main.tumbleWorld.getUID();
        boolean fromOurWorld = fromWorld.getUID() == main.tumbleWorld.getUID();

        if(!fromOurWorld){
            if(goingToOurWorld && !doWeOwnPlayer){
                main.playerOnJoin(player);
            }
            else{
                return;
            }
        }
        else{
            if(!goingToOurWorld){
                main.playerOnLeft(player);
            }
            else{
                return;
            }
        }
    }

    @EventHandler
    public void offhand(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();
        if(!main.isPlayerInGameWorld(player)){ return; }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(!main.isPlayerInGameWorld(player)){ return; }

        Entity spectatingEntity = main.playerSpectating.playerToSpecAnimal.get(player);
        if(spectatingEntity != null){
            spectatingEntity.teleport(player.getLocation());
            spectatingEntity.setVisualFire(false);
            player.setVisualFire(false);
        }

        if(main.countdown != -1){
            Location from = event.getFrom();
            from.setYaw(event.getTo().getYaw());
            from.setPitch(event.getTo().getPitch());
            event.setTo(from);
            //event.setCancelled(true);
            return;
        }

        if(event.getTo().getBlockY() <= TumbleConstants.deathY){
            main.killPlayer(player);
        }

        boolean isShortFall = player.getFallDistance() > 4;
        if(isShortFall && !main.playerToIsShortfalling.get(player)){
            main.playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.shortfall", SoundCategory.MASTER);
        }
        main.playerToIsShortfalling.put(player, isShortFall);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        if(event.getLocation().getWorld().getUID() != main.tumbleWorld.getUID()){ return; }

        if(event.getEntityType() == EntityType.SNOWBALL){
            Snowball snowball = (Snowball) event.getEntity();
            if(snowball.getShooter() instanceof Player){
                Player player = (Player) snowball.getShooter();
                String UUID = player.getUniqueId().toString();
                int newSnowballsThrownCount = main.playerUUID_toSnowballsThrownInRound.get(UUID) + 1;

                main.playerUUID_toSnowballsThrownInRound.put(UUID, newSnowballsThrownCount);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(player.getWorld().getUID() != main.tumbleWorld.getUID()){ return; }

        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event){
        // is it our world's projectile?
        if(event.getEntity().getWorld().getUID() != main.tumbleWorld.getUID()){ return; }

        // is it a snowball?
        if(event.getEntityType() != EntityType.SNOWBALL){ return; }

        Snowball snowball = (Snowball) event.getEntity();
        snowball.setShooter(null);

        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();

        if(hitBlock != null){
            // it hit a block. let's break it if within bounds
            int maxRadius = TumbleConstants.maxRadius + 2; // add a margin to catch any stray blocks!
            Location start = new Location(main.tumbleWorld, -maxRadius, TumbleConstants.fillCornerB[1], -maxRadius);
            Location end = new Location(main.tumbleWorld, maxRadius, TumbleConstants.fillCornerA[1], maxRadius);

            if(!AreaMath.locationInArea(snowball.getLocation(), start, end)){return;}
            main.destroyBlockInWorld(hitBlock);
        }
        if(hitEntity != null) {
            if (hitEntity.getType() == EntityType.PLAYER) {
                Player player = (Player) hitEntity;
                Vector vel = snowball.getLocation().getDirection().multiply(5);
                vel.setY(.4);
                //player.setVelocity(vel);

                main.tumbleWorld.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 3, 1);
            }
        }
    }
}
