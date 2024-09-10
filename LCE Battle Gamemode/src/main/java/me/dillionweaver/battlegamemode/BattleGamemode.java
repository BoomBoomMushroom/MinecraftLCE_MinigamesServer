package me.dillionweaver.battlegamemode;

import me.dillionweaver.battlegamemode.commands.BattleEndGameCommand;
import me.dillionweaver.battlegamemode.commands.BattleRegisterWorldCommand;
import me.dillionweaver.battlegamemode.commands.BattleSendMeToWorldCommand;
import me.dillionweaver.battlegamemode.commands.BattleStartGameCommand;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import me.dillionweaver.battlegamemode.data.FileSaveData;
import me.dillionweaver.battlegamemode.data.TimeStuff;
import me.dillionweaver.battlegamemode.effects.PlaySoundEffect;
import me.dillionweaver.battlegamemode.player.*;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class BattleGamemode extends JavaPlugin {
    public Server server;
    public boolean gameStarted = false;
    public boolean isDisabling = false;
    public boolean canSetHealth = true;
    public float countdown = -1f;
    public float invulnerabilityTimer = -1f;
    public float returnToLobbyTimer = -1f;
    public long startTimeMS = 0;
    public long endTime = 0;
    public int mapId = 0;
    public int lastMapId = 0;
    public Random random = new Random();
    public World battleWorld;
    public List<Player> playersInWorld = new ArrayList<>();
    public List<Player> playerQueueTpToLobby = new ArrayList<>();
    public String currentMusicName = "";

    public HashMap<String, Boolean> playerUUID_toIsReady = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toVotedMapId = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_toDidPlayLastRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toDeathsInRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toDamageInRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toKillsInRound = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_isAlive = new HashMap<>();

    public PlayerInteraction playerInteraction = new PlayerInteraction(this);
    public EndStatsUI endStatsUI = new EndStatsUI(this);
    public FileSaveData fileSaveData = new FileSaveData(this);
    public PlaySoundEffect playSoundEffect = new PlaySoundEffect(this);

    @Override
    public void onEnable() {
        server = getServer();

        server.getPluginManager().registerEvents(new PlayerEvents(this), this);

        server.getPluginCommand("BattleStartGame").setExecutor(new BattleStartGameCommand(this));
        server.getPluginCommand("BattleEndGame").setExecutor(new BattleEndGameCommand(this));
        server.getPluginCommand("BattleRegisterWorld").setExecutor(new BattleRegisterWorldCommand(this));
        server.getPluginCommand("BattleSendMeToWorld").setExecutor(new BattleSendMeToWorldCommand(this));

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Your per-tick update logic here
                update();
            }
        }, 0L, 1L);


        String battleWorldName = (String)fileSaveData.getDataFileKey(BattleConstants.battleWorldKeyName);
        if(battleWorldName == null){
            String message = "&c"+ BattleConstants.pluginMessagePrefix +"Battle world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }
        WorldCreator creator = new WorldCreator(battleWorldName);
        battleWorld = server.createWorld(creator);

        battleWorld.setDifficulty(Difficulty.PEACEFUL);
        battleWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        battleWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        battleWorld.setTime(0);
        battleWorld.setStorm(false);

        if(battleWorld == null){
            String message = "&c"+ BattleConstants.pluginMessagePrefix +"Battle world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }

        for(Player player : server.getOnlinePlayers()) {
            if(!isPlayerInGameWorld(player)){ continue; }

            playerOnJoin(player);
        }

        System.out.println(BattleConstants.pluginMessagePrefix + "Battle plugin started up!");
    }

    @Override
    public void onDisable() {
        isDisabling = true;
        for(Player player : playersInWorld) {
            playerOnLeft(player);
            playSoundEffect.stopPlayingSoundForPlayer(player, currentMusicName, SoundCategory.MUSIC);
        }

        System.out.println(BattleConstants.pluginMessagePrefix + "Battle plugin stopped!");
    }

    public void broadcast(String message){
        for(Player player : playersInWorld){
            player.sendMessage(message);
        }
    }

    public boolean isPlayerInGameWorld(Player player){
        return player.getWorld().getUID() == battleWorld.getUID();
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
        playerQueueTpToLobby.add(player);

        String UUID = player.getUniqueId().toString();

        player.setGameMode(GameMode.ADVENTURE);

        player.getInventory().clear();

        canSetHealth = true;
        player.setMaxHealth(20);
        player.setHealthScaled(true);
        player.setHealthScale(20);
        player.setHealth(20);
        canSetHealth = false;

        player.setSaturatedRegenRate(0);
        player.setUnsaturatedRegenRate(0);
        player.setVisualFire(false);
        player.setInvisible(false);

        player.setStarvationRate(0);
        player.setFoodLevel(20);
        player.setSaturatedRegenRate(0);
        player.setSaturation(0);

        playerUUID_toIsReady.put(UUID, false);
        playerUUID_toVotedMapId.put(UUID, 0);
        playerUUID_toDidPlayLastRound.put(UUID, false);
        playerUUID_toDeathsInRound.put(UUID, 0);
        playerUUID_toDamageInRound.put(UUID, 0);
        playerUUID_toKillsInRound.put(UUID, 0);
        playerUUID_isAlive.put(UUID, false);

        playerInteraction.setReady(player, false);
        teleportPlayerToLobby(player);

        if(gameStarted){
            int[] mapSpawnpointIndexes = BattleConstants.mapIdToSpawnPointIndexes[mapId];
            int spawnpointIndex = mapSpawnpointIndexes[0];
            int[] spawnpoint = BattleConstants.allSpawnPoints[spawnpointIndex];
            player.teleport(new Location(battleWorld, spawnpoint[0], spawnpoint[1], spawnpoint[2]));
            playerUUID_isAlive.put(UUID, false);
        }
        else{
            givePlayerResultsItem(player);
            givePlayerVotingMap(player);
        }
    }

    public void playerOnLeft(Player player){
        if(!isDisabling) {
            removePlayerFromWorldList(player);
        }
        String UUID = player.getUniqueId().toString();

        playerUUID_toIsReady.remove(UUID);
        playerUUID_toVotedMapId.remove(UUID);
        playerUUID_toDidPlayLastRound.remove(UUID);
    }

    public void givePlayerVotingMap(Player player){
        ItemStack votingMap = new ItemStack(Material.MAP, 1);

        ItemMeta itemMeta = votingMap.getItemMeta();
        String itemName = "&l&fMap Voting";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        votingMap.setItemMeta(itemMeta);
        player.getInventory().setItem(BattleConstants.mapVotingItemSlot, votingMap);
    }

    public void givePlayerResultsItem(Player player){
        ItemStack resultsItem = new ItemStack(Material.GOLD_INGOT, 1);

        ItemMeta itemMeta = resultsItem.getItemMeta();
        String itemName = "&l&6View Results";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        resultsItem.setItemMeta(itemMeta);
        player.getInventory().setItem(BattleConstants.viewResultsItemSlot, resultsItem);
    }

    public void checkForPlayersReady(){
        HashMap<Integer, Integer> mapIdToVotes = new HashMap<>();

        for(int i = 0; i< BattleConstants.idToName.length; i++){
            mapIdToVotes.put(i, 0);
        }

        int numOfreadyPlayers = 0;

        for(Player player : playersInWorld){
            String UUID = player.getUniqueId().toString();
            if(playerUUID_toIsReady.get(UUID) == false){
                return;
            }
            numOfreadyPlayers++;

            int votedMapId = playerUUID_toVotedMapId.get(UUID);
            mapIdToVotes.put(votedMapId, mapIdToVotes.get(votedMapId)+1);
        }

        float readiedPlayersPercent = numOfreadyPlayers / playersInWorld.size();
        for(Player player : playersInWorld) {
            player.setExp(readiedPlayersPercent);
        }

        // everyone is ready! Now we check for a winner map or, if it's a tie, a random map
        ArrayList<Integer> mostVotesId = new ArrayList<Integer>();
        int mostVotes = 0;

        for(int i = 0; i< BattleConstants.idToName.length; i++){
            int mapVotes = mapIdToVotes.get(i);

            if(mapVotes == mostVotes){
                mostVotesId.add(i);
                continue;
            }
            if(mapVotes > mostVotes){
                mostVotesId.clear();
                mostVotesId.add(i);
                mostVotes = mapVotes;
            }
        }

        int winningMapId = 0;
        int winningVotesArray = mostVotesId.size();

        // 1 winner, now do that map
        if(winningVotesArray == 1){
            winningMapId = mostVotesId.get(0);
        }
        else{
            int randomIndex = random.nextInt(winningVotesArray);
            winningMapId = mostVotesId.get(randomIndex);
        }

        if(winningMapId == 0) {
            winningMapId = BattleConstants.votableMapIds[ random.nextInt(BattleConstants.votableMapIds.length) ];
        }

        String mapName = BattleConstants.idToName[winningMapId];
        mapName = mapName.substring(0, 1).toUpperCase() + mapName.substring(1);

        broadcast(ChatColor.translateAlternateColorCodes('&', "&3"+ BattleConstants.pluginMessagePrefix+"The map "+mapName+" has won the vote. Starting game soon."));
        server.dispatchCommand(server.getConsoleSender(), "battlestartgame "+winningMapId );
    }

    public boolean hasGameStarted(){
        if(mapId == 0 || !gameStarted){
            // Game not started! Wait...
            return false;
        }
        return true;
    }

    public void teleportPlayerToLobby(Player player){
        if(!playersInWorld.contains(player)){return;}

        int[] lobbySpawnPoints = BattleConstants.mapIdToSpawnPointIndexes[0];

        int index = random.nextInt(lobbySpawnPoints.length);
        int[] position = BattleConstants.allSpawnPoints[lobbySpawnPoints[index]];
        Location newLoc = new Location(battleWorld, position[0], position[1], position[2]);

        player.teleport(newLoc);
    }

    public void endGame(){
        countdown = -1;
        gameStarted = false;
        lastMapId = mapId;
        mapId = 0;

        canSetHealth = true;
        for(Player player : playersInWorld){
            player.setHealth(20);
            playSoundEffect.stopPlayingSoundForPlayer(player, currentMusicName, SoundCategory.MUSIC);

            teleportPlayerToLobby(player);

            player.getInventory().clear();
            player.closeInventory();

            playerInteraction.setReady(player, false);
            givePlayerResultsItem(player);
            givePlayerVotingMap(player);

            endStatsUI.openEndStatsGUI(player);
        }

        canSetHealth = false;
        currentMusicName = "";
    }

    public void update(){
        for(Player player : playerQueueTpToLobby){
            teleportPlayerToLobby(player);
            player.getInventory().clear();
            player.closeInventory();

            playerInteraction.setReady(player, false);
            givePlayerResultsItem(player);
            givePlayerVotingMap(player);
        }
        playerQueueTpToLobby.clear();

        // countdown the start of a game
        if(countdown != -1){
            countdown -= 1/20f;

            if(countdown <= 0){
                countdown = -1;
                gameStarted = true;
                startTimeMS = TimeStuff.getTimeMS();
                endTime = startTimeMS + (60*10) * 1000; // 10 minutes in ms


                for(Player playerForSound : playersInWorld) {
                    playSoundEffect.playSoundEffectForPlayer(playerForSound, "minecraft:glide.cd_zero", SoundCategory.MASTER);

                    playSoundEffect.stopPlayingSoundForPlayerByCategory(playerForSound, SoundCategory.MUSIC);

                    /*
                    String[] possibleMusicNames = BattleConstants.mapIdToMusicName[mapId];
                    currentMusicName = possibleMusicNames[random.nextInt(possibleMusicNames.length)];
                    playSoundEffect.playSoundEffectForPlayer(playerForSound, currentMusicName, SoundCategory.MUSIC);
                    */
                }
                return;
            }
        }

        if(returnToLobbyTimer != -1f) {
            if (returnToLobbyTimer > 0) {
                returnToLobbyTimer -= 1 / 20f;
                return;
            }
            if (returnToLobbyTimer <= 0) {
                returnToLobbyTimer = -1f;
                endGame();
            }
        }

        long timeUntilEnd = (endTime - TimeStuff.getTimeMS()) / 1000;
        if(timeUntilEnd <= 0 && returnToLobbyTimer == -1){
            // time ran out!
            String message = "&3" + BattleConstants.pluginMessagePrefix + "Round end!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);

            endGame();
            return;
        }
        if(timeUntilEnd <= 60){
            // https://www.youtube.com/watch?v=wOOyl_TQjEk (video of Battle gameplay)
            // display that somewhere (preferably at the top)
            String timeLeftText = "Round ends in " + (int)Math.floor(timeUntilEnd) + " seconds(s)...";
        }

        canSetHealth = false;

        if(mapId == 0){return;}
        lastMapId = mapId;

        boolean playerIsPlaying = false;
        for(Player player : playersInWorld) {
            String UUID = player.getUniqueId().toString();

            if( !playerUUID_isAlive.get(UUID) ){ continue; }

            playerUUID_toDidPlayLastRound.put(UUID, true);
            playerIsPlaying = true;

            player.setExp(0);
        }
    }
}
