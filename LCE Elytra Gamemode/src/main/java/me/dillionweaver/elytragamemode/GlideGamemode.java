package me.dillionweaver.elytragamemode;

import me.dillionweaver.elytragamemode.commands.GlideEndGameCommand;
import me.dillionweaver.elytragamemode.commands.GlideRegisterWorldCommand;
import me.dillionweaver.elytragamemode.commands.GlideSendMeToWorldCommand;
import me.dillionweaver.elytragamemode.commands.GlideStartGameCommand;
import me.dillionweaver.elytragamemode.data.AdminBoundarySetup;
import me.dillionweaver.elytragamemode.data.GlideConstants;
import me.dillionweaver.elytragamemode.data.FileSaveData;
import me.dillionweaver.elytragamemode.data.TimeStuff;
import me.dillionweaver.elytragamemode.effects.FinishlineFireworks;
import me.dillionweaver.elytragamemode.effects.PlaySoundEffect;
import me.dillionweaver.elytragamemode.player.*;
import me.dillionweaver.elytragamemode.effects.VelocityAdding;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class GlideGamemode extends JavaPlugin {
    public Server server;
    public boolean gameStarted = false;
    public boolean isDisabling = false;
    public boolean canSetHealth = true;
    public float countdown = -1f;
    public float returnToLobbyTimer = -1f;
    public int playersWon = 0;
    String lastCountdownText = "";
    public long startTimeMS = 0;
    public long endTime = 0;
    public int mapId = 0;
    public int lastMapId = 0;
    public Random random = new Random();
    public BossBar timeRemainingDisplay = null;
    public World glideWorld;
    public List<Player> playersInWorld = new ArrayList<>();
    public List<Player> playerQueueTpToLobby = new ArrayList<>();
    public String currentMusicName = "";

    public HashMap<String, Integer> playerUUID_toCheckpointId = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_toHasFinished = new HashMap<>();
    public HashMap<String, Long> playerUUID_toTimeCompleted = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_toIsReady = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toVotedMapId = new HashMap<>();
    public HashMap<String, Float> playerUUID_toDamageCooldown = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toDeathsInRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toDamageInRound = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_toDidPlayLastRound = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_isUpdrafting = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_isBoosting = new HashMap<>();

    public VelocityAdding velocityAdding = new VelocityAdding();
    public PlayerAreaMath playerAreaMath = new PlayerAreaMath();
    public PlayerVelocityString playerVelocityString = new PlayerVelocityString();
    public CheckPlayerGameStatus checkPlayerGameStatus = new CheckPlayerGameStatus(this);
    public PlayerInteraction playerInteraction = new PlayerInteraction(this);
    public EndStatsUI endStatsUI = new EndStatsUI(this);
    public FileSaveData fileSaveData = new FileSaveData(this);
    public PlaySoundEffect playSoundEffect = new PlaySoundEffect(this);

    public AdminBoundarySetup adminBoundarySetup = new AdminBoundarySetup(this);

    @Override
    public void onEnable() {
        server = getServer();

        server.getPluginManager().registerEvents(new PlayerEvents(this), this);

        server.getPluginCommand("GlideStartGame").setExecutor(new GlideStartGameCommand(this));
        server.getPluginCommand("GlideEndGame").setExecutor(new GlideEndGameCommand(this));
        server.getPluginCommand("GlideRegisterWorld").setExecutor(new GlideRegisterWorldCommand(this));
        server.getPluginCommand("GlideSendMeToWorld").setExecutor(new GlideSendMeToWorldCommand(this));

        timeRemainingDisplay = server.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        timeRemainingDisplay.setVisible(false);

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                // Your per-tick update logic here
                update();
            }
        }, 0L, 1L);


        String elytraWorldName = (String)fileSaveData.getDataFileKey(GlideConstants.elytraWorldKeyName);
        if(elytraWorldName == null){
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"Glide world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }
        WorldCreator creator = new WorldCreator(elytraWorldName);
        glideWorld = server.createWorld(creator);

        glideWorld.setDifficulty(Difficulty.PEACEFUL);
        glideWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        glideWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        glideWorld.setTime(0);
        glideWorld.setStorm(false);

        if(glideWorld == null){
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"Glide world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }

        for(Player player : server.getOnlinePlayers()) {
            if(!isPlayerInGameWorld(player)){ continue; }

            playerOnJoin(player);
        }

        System.out.println(GlideConstants.pluginMessagePrefix + "Glide plugin started up!");
    }

    @Override
    public void onDisable() {
        isDisabling = true;
        for(Player player : playersInWorld) {
            playerOnLeft(player);
            playSoundEffect.stopPlayingSoundForPlayer(player, currentMusicName, SoundCategory.MUSIC);
        }

        System.out.println(GlideConstants.pluginMessagePrefix + "Glide plugin stopped!");
    }

    public void broadcast(String message){
        for(Player player : playersInWorld){
            player.sendMessage(message);
        }
    }

    public boolean isPlayerInGameWorld(Player player){
        return player.getWorld().getUID() == glideWorld.getUID();
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

        timeRemainingDisplay.addPlayer(player);

        player.setGameMode(GameMode.ADVENTURE);
        checkPlayerGameStatus.updateGameStatus(player, true);

        player.getInventory().clear();

        canSetHealth = true;
        player.setMaxHealth(6);
        player.setHealthScaled(true);
        player.setHealthScale(6);
        player.setHealth(6);
        canSetHealth = false;

        player.setSaturatedRegenRate(0);
        player.setUnsaturatedRegenRate(0);
        player.setVisualFire(false);
        player.setInvisible(false);

        player.setStarvationRate(0);
        player.setFoodLevel(20);
        player.setSaturatedRegenRate(0);
        player.setSaturation(0);

        playerUUID_toCheckpointId.put(UUID, 0);
        playerUUID_toHasFinished.put(UUID, false);
        playerUUID_toTimeCompleted.put(UUID, 0L);
        playerUUID_toIsReady.put(UUID, false);
        playerUUID_toVotedMapId.put(UUID, 0);
        playerUUID_toDamageCooldown.put(UUID, 0f);
        playerUUID_toDeathsInRound.put(UUID, 0);
        playerUUID_toDamageInRound.put(UUID, 0);
        playerUUID_toDidPlayLastRound.put(UUID, false);
        playerUUID_isBoosting.put(UUID, false);
        playerUUID_isUpdrafting.put(UUID, false);

        playerInteraction.setReady(player, false);
        teleportPlayerToLobby(player);

        if(gameStarted){
            int[] mapCheckpointIndexes = GlideConstants.checkpointsInMapFromId[mapId];
            int checkpointIndex = mapCheckpointIndexes[0];
            int[] checkpoints = GlideConstants.checkpoints[checkpointIndex];
            playerUUID_toCheckpointId.put(UUID, checkpointIndex);
            player.teleport(new Location(glideWorld, checkpoints[6], checkpoints[7], checkpoints[8]));
            player.setRotation(checkpoints[9], checkpoints[10]);

            checkPlayerGameStatus.updateGameStatus(player, true);
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
        timeRemainingDisplay.removePlayer(player);

        playerUUID_toHasFinished.remove(UUID);
        playerUUID_toTimeCompleted.remove(UUID);
        playerUUID_toCheckpointId.remove(UUID);
        playerUUID_toIsReady.remove(UUID);
        playerUUID_toVotedMapId.remove(UUID);
        playerUUID_toDamageCooldown.remove(UUID);
        playerUUID_toDeathsInRound.remove(UUID);
        playerUUID_toDamageInRound.remove(UUID);
        playerUUID_toDidPlayLastRound.remove(UUID);
        playerUUID_isBoosting.remove(UUID);
        playerUUID_isUpdrafting.remove(UUID);
    }

    public void givePlayerVotingMap(Player player){
        ItemStack votingMap = new ItemStack(Material.MAP, 1);

        ItemMeta itemMeta = votingMap.getItemMeta();
        String itemName = "&l&fMap Voting";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        votingMap.setItemMeta(itemMeta);
        player.getInventory().setItem(GlideConstants.mapVotingItemSlot, votingMap);
    }

    public void givePlayerResultsItem(Player player){
        ItemStack resultsItem = new ItemStack(Material.GOLD_INGOT, 1);

        ItemMeta itemMeta = resultsItem.getItemMeta();
        String itemName = "&l&6View Results";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        resultsItem.setItemMeta(itemMeta);
        player.getInventory().setItem(GlideConstants.viewResultsItemSlot, resultsItem);
    }

    public void checkForPlayersReady(){
        HashMap<Integer, Integer> mapIdToVotes = new HashMap<>();

        for(int i = 0; i< GlideConstants.idToName.length; i++){
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

        for(int i = 0; i< GlideConstants.idToName.length; i++){
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
            winningMapId = GlideConstants.votableMapIds[ random.nextInt(GlideConstants.votableMapIds.length) ];
        }

        String mapName = GlideConstants.idToName[winningMapId];
        mapName = mapName.substring(0, 1).toUpperCase() + mapName.substring(1);

        broadcast(ChatColor.translateAlternateColorCodes('&', "&3"+ GlideConstants.pluginMessagePrefix+"The map "+mapName+" has won the vote. Starting game soon."));
        server.dispatchCommand(server.getConsoleSender(), "glidestartgame "+winningMapId );
    }

    public boolean hasGameStarted(){
        if(mapId == 0 || !gameStarted){
            // Game not started! Wait...
            return false;
        }
        return true;
    }

    public void playerHitWall(Player player, boolean instantKill){
        if(!hasGameStarted()){
            // Game not started! Wait...
            return;
        }
        if( !checkPlayerGameStatus.stillPlayingGame(player) ){ return; }
        String UUID = player.getUniqueId().toString();

        if(playerUUID_toDamageCooldown.get(UUID) > 0){ return; }
        playerUUID_toDamageCooldown.put(UUID, 1f);

        double currentHealth = player.getHealth();
        double newHealth = currentHealth - 2;
        if(newHealth <= 0 || instantKill){
            // died
            //player.sendHealthUpdate(6, 20, 20);
            canSetHealth = true;
            player.setHealth(6);
            canSetHealth = false;

            int checkpointIndex = playerUUID_toCheckpointId.get( UUID );
            int[] checkpoint = GlideConstants.checkpoints[checkpointIndex];
            Location newLoc = new Location(glideWorld, checkpoint[6], checkpoint[7], checkpoint[8] );
            newLoc.setYaw(checkpoint[9]);
            newLoc.setPitch(checkpoint[10]);
            player.teleport(newLoc);
            player.setGliding(true);
            player.setVelocity(VelocityAdding.VECTOR_ZERO);

            playSoundEffect.playSoundEffectForPlayer(player, "minecraft:glide.respawn", SoundCategory.MASTER);
            playerUUID_toDeathsInRound.put(UUID, playerUUID_toDeathsInRound.get(UUID)+1);
        }
        else{
            canSetHealth = true;
            player.damage(2);
            //player.setHealth(newHealth);
            canSetHealth = false;

            //playSoundEffect.playSoundEffect(player.getLocation(), "minecraft:glide.fallsbig");
        }

        int amountToAdd = instantKill ? (int)currentHealth/2 : 1;

        playerUUID_toDamageInRound.put(UUID, playerUUID_toDamageInRound.get(UUID)+amountToAdd);

        checkPlayerGameStatus.updateGameStatus(player, true);
    }

    public void teleportPlayerToLobby(Player player){
        if(!playersInWorld.contains(player)){return;}

        int[] lobbySpawnPoints = GlideConstants.checkpointsInMapFromId[0];

        int index = random.nextInt(lobbySpawnPoints.length);
        int[] position = GlideConstants.checkpoints[lobbySpawnPoints[index]];
        Location newLoc = new Location(glideWorld, position[0], position[1], position[2]);

        player.teleport(newLoc);
        //player.setRotation(90, 0);
    }

    public void endGame(){
        countdown = -1;
        gameStarted = false;
        lastMapId = mapId;
        mapId = 0;
        playersWon = 0;

        timeRemainingDisplay.setVisible(false);

        canSetHealth = true;
        for(Player player : playersInWorld){
            player.setHealth(6);
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
            int countdownRounded = (int)Math.ceil(countdown);
            String command = "title @a title ";
            String countdownText = new String[]{
                    "0",
                    "①",
                    "②",
                    "③",
            }[countdownRounded];
            String countdownTextColor = new String[]{
                    "white",
                    "green",
                    "yellow",
                    "red",
            }[countdownRounded];
            command += "{\"color\":\""+ countdownTextColor +"\",\"text\":\""+ countdownText +"\"}";
            if(lastCountdownText == countdownTextColor){ return; }

            int[] sfxPos = GlideConstants.checkpoints[GlideConstants.checkpointsInMapFromId[mapId][0]];
            Location soundEffectLocation = new Location(glideWorld, sfxPos[6], sfxPos[7], sfxPos[8]);

            if(countdownText == "0"){
                countdown = -1;
                gameStarted = true;
                startTimeMS = TimeStuff.getTimeMS();
                endTime = startTimeMS + (60*3) * 1000; // 3 minutes in ms


                for(Player playerForSound : playersInWorld) {
                    playSoundEffect.playSoundEffectForPlayer(playerForSound, "minecraft:glide.cd_zero", SoundCategory.MASTER);

                    playSoundEffect.stopPlayingSoundForPlayerByCategory(playerForSound, SoundCategory.MUSIC);

                    String[] possibleMusicNames = GlideConstants.mapIdToMusicName[mapId];
                    currentMusicName = possibleMusicNames[random.nextInt(possibleMusicNames.length)];
                    playSoundEffect.playSoundEffectForPlayer(playerForSound, currentMusicName, SoundCategory.MUSIC);
                }
                return;
            }

            for(Player playerForSound : playersInWorld) {
                playSoundEffect.playSoundEffectForPlayer(playerForSound, "minecraft:block.lever.click", SoundCategory.MASTER);
            }

            server.dispatchCommand(server.getConsoleSender(), command);
            lastCountdownText = countdownTextColor;
        }


        if(!hasGameStarted()){
            // Game not started! Wait...
            timeRemainingDisplay.setVisible(false);
            return;
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
            String message = "&3" + GlideConstants.pluginMessagePrefix + "Time has run out! Returning to lobby.";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);

            endGame();
            return;
        }
        if(timeUntilEnd <= 60){
            // display that somewhere (preferably at the top)
            String timeLeftText = (int)Math.floor(timeUntilEnd) + " seconds to reach the end!";
            timeRemainingDisplay.setTitle(timeLeftText);
            timeRemainingDisplay.setVisible(true);

            //long currentTimeDiff = endTime - TimeStuff.getTimeMS();
            //long startTimeDiff = endTime - startTimeMS;

            double percent = (double) timeUntilEnd / 60;
            timeRemainingDisplay.setProgress( percent );
        }

        canSetHealth = false;

        if(mapId == 0){return;}
        lastMapId = mapId;

        int[] mapCheckpointIndexes = GlideConstants.checkpointsInMapFromId[mapId];
        int[] mapSpeedBoostIndexes = GlideConstants.speedBoostsInMapFromId[mapId];
        int[] mapUpdraftIndexes = GlideConstants.updraftsInMapFromId[mapId];
        int[] mapFinishLinesIndexes = GlideConstants.finishLinesInMapFromId[mapId];

        int[] lastCheckpoint = GlideConstants.checkpoints[ mapCheckpointIndexes[mapCheckpointIndexes.length-1] ];
        int maxCheckpointNumber = lastCheckpoint[11];

        boolean playerIsPlaying = false;
        for(Player player : playersInWorld) {
            String UUID = player.getUniqueId().toString();

            if( !checkPlayerGameStatus.stillPlayingGame(player) ){ continue; }
            playerUUID_toDidPlayLastRound.put(UUID, true);
            playerIsPlaying = true;

            float newDmgCooldown = playerUUID_toDamageCooldown.get(UUID) - (1/20f);
            playerUUID_toDamageCooldown.put(UUID, newDmgCooldown);

            player.setExp(0);

            // make sure we're always flying
            player.setGliding(true);

            // velocity display
            String speedString = playerVelocityString.getMetersPerSecondString(player);
            String timeElapsedString = TimeStuff.msToFormat( TimeStuff.timeSinceMS(startTimeMS) );

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timeElapsedString + "  -  " + speedString + "m/s"));

            // old checking for boosts & updrafts. now we should iterate over the bounding boxes
            //boolean doSpeedBoost = speedBoost.playerWithinBlockArea(player, elytraConstants.speedBoostBlock, 4);
            //boolean doUpdraftBoost = speedBoost.playerWithinBlockArea(player, elytraConstants.updraftBlock, 50);

            boolean doSpeedBoost = false;
            boolean doUpdraftBoost = false;
            boolean crossedFinishline = false;

            for(int finishlineIndex : mapFinishLinesIndexes){
                int[] finishline = GlideConstants.finishLineBoundingBoxes[finishlineIndex];
                Location a = new Location(glideWorld, finishline[0], finishline[1], finishline[2]);
                Location b = new Location(glideWorld, finishline[3], finishline[4], finishline[5]);
                crossedFinishline = crossedFinishline || playerAreaMath.playerInArea(player, a, b);
            }

            playerUUID_toHasFinished.put(player.getUniqueId().toString(), crossedFinishline);
            if(crossedFinishline){
                FinishlineFireworks.spawnFinishlineFireworks(player.getLocation());
                long timeDiff = TimeStuff.timeSinceMS(startTimeMS);
                playerUUID_toTimeCompleted.put(UUID, timeDiff);

                String message = "&a" + GlideConstants.pluginMessagePrefix + player.getDisplayName() + " has made it to the end with a time of " + TimeStuff.msToFormat(timeDiff);
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                broadcast(coloredMessage);

                long nowPlusOneMin = TimeStuff.getTimeMS() + (60 * 1000);
                if(nowPlusOneMin < endTime ){
                    endTime = nowPlusOneMin;
                }

                playersWon++;
                String soundName = "finish_line";

                switch (playersWon){
                    case 1:
                        soundName = "finish_line_1st";
                        break;
                    case 2:
                        soundName = "finish_line_2nd";
                        break;
                    case 3:
                        soundName = "finish_line_3rd";
                        break;
                    default:
                        soundName = "finish_line";
                }

                String bestTimeKey = UUID + "_bestTime_" + mapId;

                long bestTime = -1;
                Object keyGet = fileSaveData.getDataFileKey(bestTimeKey);
                if(keyGet != null){
                    bestTime = Long.parseLong(keyGet.toString());
                }

                if(playerUUID_toTimeCompleted.get(UUID) < bestTime || bestTime == -1){
                    soundName += "_best_time";
                    fileSaveData.setDataFileKey(bestTimeKey, playerUUID_toTimeCompleted.get(UUID));

                    // first one has issue where if you don't have a record it says "0:00:-01"
                    //message = " &6&l" + player.getDisplayName() + " has beat their personal record of "+ TimeStuff.msToFormat(bestTime) +" with a new time of " + TimeStuff.msToFormat(timeDiff);
                    message = " &6&l" + player.getDisplayName() + " has set a a new personal record with a time of " + TimeStuff.msToFormat(timeDiff);
                    coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                    broadcast(coloredMessage);
                }

                playSoundEffect.playSoundEffectForPlayer(player, "minecraft:glide."+soundName, SoundCategory.MASTER);

                //player.setVelocity(new Vector(0,0,0));
                continue;
            }


            for(int speedBoostIndex : mapSpeedBoostIndexes){
                int[] speedBoost = GlideConstants.speedBoostBoundingBoxes[speedBoostIndex];
                Location a = new Location(glideWorld, speedBoost[0], speedBoost[1], speedBoost[2]);
                Location b = new Location(glideWorld, speedBoost[3], speedBoost[4], speedBoost[5]);
                doSpeedBoost = doSpeedBoost || playerAreaMath.playerInArea(player, a, b);
            }

            for(int updraftIndex : mapUpdraftIndexes){
                int[] updraft = GlideConstants.updraftBoundingBoxes[updraftIndex];
                Location a = new Location(glideWorld, updraft[0], updraft[1], updraft[2]);
                Location b = new Location(glideWorld, updraft[3], updraft[4], updraft[5]);
                doUpdraftBoost = doUpdraftBoost || playerAreaMath.playerInArea(player, a, b);
            }

            if(doSpeedBoost){
                velocityAdding.addVelocity(player, VelocityAdding.VECTOR_ZERO, 0.25, false, true);
                if(!playerUUID_isBoosting.get(UUID)) {
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:glide.boost", SoundCategory.MASTER);
                }
                playerUUID_isBoosting.put(UUID, true);
            }
            else{
                playerUUID_isBoosting.put(UUID, false);
            }

            if(doUpdraftBoost){
                velocityAdding.addVelocity(player, VelocityAdding.VECTOR_UP, .8 , true, false);
                if(!playerUUID_isUpdrafting.get(UUID)) {
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:glide.humid_boost", SoundCategory.MASTER);
                }
                playerUUID_isUpdrafting.put(UUID, true);
            }
            else{
                playerUUID_isUpdrafting.put(UUID, false);
            }

            for(int checkpointIndex : mapCheckpointIndexes){
                int[] checkpoint = GlideConstants.checkpoints[checkpointIndex];
                Location a = new Location(glideWorld, checkpoint[0], 0, checkpoint[2]);
                Location b = new Location(glideWorld, checkpoint[3], 255, checkpoint[5]);
                boolean isInside = playerAreaMath.playerInArea(player, a, b);
                if(!isInside){ continue; }

                int checkpointNum = checkpoint[11];

                if(playerUUID_toCheckpointId.get(UUID) >= checkpointIndex){continue;}

                playerUUID_toCheckpointId.put(UUID, checkpointIndex);
                if(checkpointNum != 0) {
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:glide.checkpoint", SoundCategory.MASTER);

                    String message = "&a" + GlideConstants.pluginMessagePrefix + player.getDisplayName() + " has made it to checkpoint #" + String.valueOf(checkpointNum) + "/" + String.valueOf(maxCheckpointNumber);
                    String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                    broadcast(coloredMessage);
                }
            }
        }

        if(!playerIsPlaying && returnToLobbyTimer == -1){
            returnToLobbyTimer = 3f;
            String message = "&3" + GlideConstants.pluginMessagePrefix + "Everyone has made it to the end! Returning to the lobby soon.";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }

    }
}
