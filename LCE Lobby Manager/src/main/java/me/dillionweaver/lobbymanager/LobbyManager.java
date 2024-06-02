package me.dillionweaver.lobbymanager;

import me.dillionweaver.lobbymanager.commands.*;
import me.dillionweaver.lobbymanager.data.*;
import me.dillionweaver.lobbymanager.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class LobbyManager extends JavaPlugin {
    public Server server;
    public World lobbyWorld;
    public Random random = new Random();
    public boolean isDisabling = false;
    public List<Player> playerQueueToLobby = new ArrayList<>();
    public List<Player> playersInWorld = new ArrayList<>();
    public FileSaveData fileSaveData = new FileSaveData(this);
    public PlayerInteraction playerInteraction = new PlayerInteraction(this);
    public AdminBoundarySetup adminBoundarySetup = new AdminBoundarySetup(this);

    @Override
    public void onEnable() {
        server = getServer();

        server.getPluginManager().registerEvents(new PlayerEvents(this), this);

        server.getPluginCommand("LMLoadWorld").setExecutor(new LobbyManagerLoadWorldCommand(this));
        server.getPluginCommand("LMSetLobbyWorld").setExecutor(new LobbyManagerSetLobbyWorldCommand(this));
        server.getPluginCommand("LMRegisterWorld").setExecutor(new LobbyManagerRegisterWorldCommand(this));
        server.getPluginCommand("LMSendMeToWorld").setExecutor(new LobbyManagerSendMeToWorldCommand(this));

        // load all worlds we're responsible for!
        for(Object objKey : fileSaveData.getDataFileKeys()){
            String key = (String) objKey;
            if(key.endsWith("_icon")){continue;}
            String worldName = (String) fileSaveData.getDataFileKey(key);
            World loadedWorld = loadWorld(worldName);

            loadedWorld.setDifficulty(Difficulty.PEACEFUL);
            loadedWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            loadedWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            loadedWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
            loadedWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            loadedWorld.setGameRule(GameRule.DO_INSOMNIA, false);
            loadedWorld.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            loadedWorld.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            loadedWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            loadedWorld.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, false);
            loadedWorld.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, false);
            loadedWorld.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, false);
            loadedWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
            loadedWorld.setGameRule(GameRule.MOB_GRIEFING, false);
            loadedWorld.setGameRule(GameRule.NATURAL_REGENERATION, false);
            loadedWorld.setTime(0);
            loadedWorld.setStorm(false);
        }

        String lobbyManagerWorldName = (String)fileSaveData.getDataFileKey(LobbyManagerConstants.lobbyWorldKeyName);

        if(lobbyManagerWorldName == null){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"Lobby world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            server.broadcastMessage(coloredMessage);
        }
        WorldCreator creator = new WorldCreator(lobbyManagerWorldName);
        lobbyWorld = server.createWorld(creator);

        lobbyWorld.setDifficulty(Difficulty.PEACEFUL);
        lobbyWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        lobbyWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        lobbyWorld.setTime(0);
        lobbyWorld.setStorm(false);

        if(lobbyWorld == null){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"Lobby world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            server.broadcastMessage(coloredMessage);
        }

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Your per-tick update logic here
                update();
            }
        }, 0L, 20L);

        for(Player player : server.getOnlinePlayers()) {
            playerOnJoin(player);
        }

        System.out.println(LobbyManagerConstants.pluginMessagePrefix + "Lobby plugin started up!");
    }

    public void update(){
        for(Player player : playerQueueToLobby){
            teleportPlayerToLobby(player);
        }
        playerQueueToLobby.clear();


        for(Player player : server.getOnlinePlayers()){
            ItemStack firstStackItem = player.getInventory().getItem(LobbyManagerConstants.setLobbyHopItemSlot);
            boolean needToGive = false;

            if(firstStackItem == null){ needToGive = true; }
            else{
                if(firstStackItem.getType() != Material.NETHER_STAR) {
                    needToGive = true;
                }
            }

            if(needToGive){ givePlayerLobbyHopItem(player); }
        }
    }

    public World loadWorld(String worldName){
        WorldCreator creator = new WorldCreator(worldName);
        return server.createWorld(creator);
    }

    @Override
    public void onDisable() {
        isDisabling = true;
        for(Player player : playersInWorld) {
            playerOnLeft(player);
        }

        System.out.println(LobbyManagerConstants.pluginMessagePrefix + "Lobby plugin stopped!");
    }

    public boolean isPlayerInGameWorld(Player player){
        return player.getWorld().getUID() == lobbyWorld.getUID();
    }

    public void addPlayerToWorldList(Player player){
        int indexOf = playersInWorld.indexOf(player);
        if(indexOf != -1){ return; }
        playersInWorld.add(player);
    }

    public void removePlayerFromWorldList(Player player){
        playersInWorld.remove(player);
    }

    public void playerOnJoin(Player player){
        addPlayerToWorldList(player);

        String UUID = player.getUniqueId().toString();

        player.setGameMode(GameMode.ADVENTURE);

        player.getInventory().clear();

        player.setSaturatedRegenRate(0);
        player.setUnsaturatedRegenRate(0);
        player.setVisualFire(false);
        player.setInvisible(false);

        player.setStarvationRate(0);
        player.setFoodLevel(20);
        player.setSaturatedRegenRate(0);
        player.setSaturation(0);

        teleportPlayerToLobby(player);
    }

    public void playerOnLeft(Player player){
        if(!isDisabling) {
            removePlayerFromWorldList(player);
        }
        String UUID = player.getUniqueId().toString();
    }

    public void givePlayerLobbyHopItem(Player player){
        player.getInventory().clear(0);

        ItemStack resultsItem = new ItemStack(Material.NETHER_STAR, 1);

        ItemMeta itemMeta = resultsItem.getItemMeta();
        String itemName = "&l&6Switch Lobbies";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        resultsItem.setItemMeta(itemMeta);
        player.getInventory().setItem(LobbyManagerConstants.setLobbyHopItemSlot, resultsItem);
    }


    public void teleportPlayerToLobby(Player player){
        int[] lobbySpawnPoints = LobbyManagerConstants.lobbySpawnPoints[0];

        int index = random.nextInt(lobbySpawnPoints.length);
        int[] position = LobbyManagerConstants.lobbySpawnPoints[index];
        Location newLoc = new Location(lobbyWorld, position[0], position[1], position[2]);

        player.teleport(newLoc);
        //player.setRotation(90, 0);
    }
}
