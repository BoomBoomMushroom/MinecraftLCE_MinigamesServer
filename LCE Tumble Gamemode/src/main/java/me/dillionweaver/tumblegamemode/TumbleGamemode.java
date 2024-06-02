package me.dillionweaver.tumblegamemode;

import me.dillionweaver.tumblegamemode.commands.*;
import me.dillionweaver.tumblegamemode.data.*;
import me.dillionweaver.tumblegamemode.player.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class TumbleGamemode extends JavaPlugin {
    public Server server;
    public boolean gameStarted = false;
    public boolean isDisabling = false;
    public float countdown = -1f;
    public float returnToLobbyTimer = -1f;
    String lastCountdownText = "";
    public long startTimeMS = 0;
    public long endTime = 0;
    public Random random = new Random();
    public World tumbleWorld;
    public List<Player> playersInWorld = new ArrayList<>();
    public List<Player> playerQueueToLobby = new ArrayList<>();
    public List<Player> playerDeathOrderList = new ArrayList<>();
    public HashMap<Player, Boolean> playerToIsShortfalling = new HashMap<>();

    public boolean useSnowballs = true;
    public boolean useShovels = true;
    public boolean dontEndBecauseDeveloping = false;

    public BossBar timeRemainingDisplay = null;

    public HashMap<String, Boolean> playerUUID_toIsReady = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toBlocksMinedInRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toSnowballsThrownInRound = new HashMap<>();
    public HashMap<String, Long> playerUUID_toTimedSurvivedInRoundMS = new HashMap<>();
    public HashMap<String, Boolean> playerUUID_toDidPlayLastRound = new HashMap<>();
    public HashMap<String, Integer> playerUUID_toBreakCooldownTicks = new HashMap<>();

    public PlayerSpectating playerSpectating = new PlayerSpectating(this);
    public CheckPlayerGameStatus checkPlayerGameStatus = new CheckPlayerGameStatus(this);
    public PlayerInteraction playerInteraction = new PlayerInteraction(this);
    public EndStatsUI endStatsUI = new EndStatsUI(this);
    public FileSaveData fileSaveData = new FileSaveData(this);
    public Layers layers = new Layers();
    public TerrainGeneration terrainGeneration = new TerrainGeneration(this);
    public AdminBoundarySetup adminBoundarySetup = new AdminBoundarySetup(this);
    public PlaySoundEffect playSoundEffect = new PlaySoundEffect(this);

    @Override
    public void onEnable() {
        server = getServer();

        server.getPluginManager().registerEvents(new PlayerEvents(this), this);

        server.getPluginCommand("TumbleStartGame").setExecutor(new TumbleStartGameCommand(this));
        server.getPluginCommand("TumbleEndGame").setExecutor(new TumbleEndGameCommand(this));
        server.getPluginCommand("TumbleRegisterWorld").setExecutor(new TumbleRegisterWorldCommand(this));
        server.getPluginCommand("TumbleSendMeToWorld").setExecutor(new TumbleSendMeToWorldCommand(this));
        server.getPluginCommand("TumbleTerrain").setExecutor(new TumbleTerrainCommand(this));

        server.getPluginCommand("TumbleSpectate").setExecutor(new TumbleSpectateCommand(this));

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

        String tumbleWorldName = (String)fileSaveData.getDataFileKey(TumbleConstants.tumbleWorldKeyName);

        if(tumbleWorldName == null){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"Glide world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            server.broadcastMessage(coloredMessage);
        }
        WorldCreator creator = new WorldCreator(tumbleWorldName);
        tumbleWorld = server.createWorld(creator);

        tumbleWorld.setDifficulty(Difficulty.PEACEFUL);
        tumbleWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        tumbleWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        tumbleWorld.setTime(0);
        tumbleWorld.setStorm(false);

        //tumbleWorld = server.getWorld(tumbleWorldName); // offline world by default. use WorldCreator to load it on startup
        if(tumbleWorld == null){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"Tumble world not set! Plugin will not work without a world set! Set it using /sendmetoworld world_folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
        }

        for(Player player : server.getOnlinePlayers()) {
            if(!isPlayerInGameWorld(player)){ continue; }

            playerOnJoin(player);
        }

        System.out.println(TumbleConstants.pluginMessagePrefix + "Tumble plugin started up!");
    }

    @Override
    public void onDisable() {
        isDisabling = true;
        for(Player player : playersInWorld) {
            playerOnLeft(player);
        }

        System.out.println(TumbleConstants.pluginMessagePrefix + "Tumble plugin stopped!");
    }

    public void broadcast(String message){
        for(Player player : playersInWorld){
            player.sendMessage(message);
        }
    }

    public void destroyBlockInWorld(Block block){
        tumbleWorld.spawnParticle(Particle.BLOCK, block.getLocation(), 200, 0.55, 0.55, 0.55, 1.75, block.getBlockData());
        block.setType(Material.AIR);
        tumbleWorld.playSound(block.getLocation(), block.getBlockData().getSoundGroup().getBreakSound(), 3, 1);
    }

    public boolean isPlayerInGameWorld(Player player){
        if(playersInWorld.contains(player)){return true;}
        return player.getWorld().getUID() == tumbleWorld.getUID();
    }

    public void addPlayerToWorldList(Player player){
        if(playersInWorld.contains(player)){ return; }
        playersInWorld.add(player);
    }

    public void removePlayerFromWorldList(Player player){
        if(playersInWorld.contains(player)) {
            playersInWorld.remove(player);
        }
    }

    public void playerOnJoin(Player player){
        addPlayerToWorldList(player);

        String UUID = player.getUniqueId().toString();

        timeRemainingDisplay.addPlayer(player);

        player.setGameMode(GameMode.ADVENTURE);
        checkPlayerGameStatus.updateGameStatus(player, true);

        player.getInventory().clear();

        player.setMaxHealth(20);
        player.setHealth(20);

        player.setSaturatedRegenRate(0);
        player.setUnsaturatedRegenRate(0);
        player.setVisualFire(false);
        player.setInvisible(false);

        player.setStarvationRate(0);
        player.setFoodLevel(20);
        player.setSaturatedRegenRate(0);
        player.setSaturation(0);

        playerUUID_toIsReady.put(UUID, false);
        playerUUID_toDidPlayLastRound.put(UUID, false);
        playerUUID_toBreakCooldownTicks.put(UUID, 0);
        playerUUID_toTimedSurvivedInRoundMS.put(UUID, 0L);
        playerToIsShortfalling.put(player, false);

        playerInteraction.setReady(player, false);
        givePlayerResultsItem(player);

        if(gameStarted){
            player.getInventory().clear();
            int[] spawnLoc = TumbleConstants.gameSpawnSpectating;
            player.teleport(new Location(tumbleWorld, spawnLoc[0], spawnLoc[1], spawnLoc[2]));
            player.setRotation(0, 0);

            checkPlayerGameStatus.updateGameStatus(player, false);
        }
        else{
            playerQueueToLobby.add(player);
            playerInteraction.setReady(player, false);
            givePlayerResultsItem(player);
        }

        endGame();
    }

    public void playerOnLeft(Player player){
        if(!isDisabling) {
            removePlayerFromWorldList(player);
        }
        String UUID = player.getUniqueId().toString();
        checkPlayerGameStatus.updateGameStatus(player, true);

        playerUUID_toIsReady.remove(UUID);
        playerUUID_toDidPlayLastRound.remove(UUID);
        playerUUID_toSnowballsThrownInRound.remove(UUID);
        playerUUID_toBlocksMinedInRound.remove(UUID);
        playerUUID_toBreakCooldownTicks.remove(UUID);
        playerUUID_toTimedSurvivedInRoundMS.remove(UUID);
        playerToIsShortfalling.remove(player);
        player.getInventory().clear();

        timeRemainingDisplay.removePlayer(player);
    }

    public void givePlayerResultsItem(Player player){
        ItemStack resultsItem = new ItemStack(Material.GOLD_INGOT, 1);

        ItemMeta itemMeta = resultsItem.getItemMeta();
        String itemName = "&l&6View Results";
        String coloredItemName = ChatColor.translateAlternateColorCodes('&', itemName);
        itemMeta.setItemName(coloredItemName);

        resultsItem.setItemMeta(itemMeta);
        player.getInventory().setItem(TumbleConstants.viewResultsItemSlot, resultsItem);
    }

    public void checkForPlayersReady(){
        int numOfreadyPlayers = 0;

        for(Player player : playersInWorld){
            String UUID = player.getUniqueId().toString();
            if(playerUUID_toIsReady.get(UUID) == false){
                return;
            }
            numOfreadyPlayers++;
        }

        float readiedPlayersPercent = numOfreadyPlayers / playersInWorld.size();
        for(Player player : playersInWorld) {
            player.setExp(readiedPlayersPercent);
        }

        broadcast(ChatColor.translateAlternateColorCodes('&', "&3"+ TumbleConstants.pluginMessagePrefix+"Everyone is ready. Starting game soon."));
        server.dispatchCommand(server.getConsoleSender(), "tumblestartgame");
    }

    public boolean hasGameStarted(){
        return gameStarted;
    }

    public void teleportPlayerToLobby(Player player){
        int[][] lobbySpawnPoints = TumbleConstants.lobbySpawnPoints;

        int index = random.nextInt(lobbySpawnPoints.length);
        int[] position = lobbySpawnPoints[index];
        Location newLoc = new Location(tumbleWorld, position[0], position[1], position[2]);

        player.teleport(newLoc);
        //player.setRotation(90, 0);
    }

    public void killPlayer(Player player){
        if(playerDeathOrderList.contains(player)){ return; }
        playerDeathOrderList.add(player);
        checkPlayerGameStatus.updateGameStatus(player, false);
        int[] deathLoc = TumbleConstants.gameSpawnSpectating;
        player.teleport(new Location(tumbleWorld, deathLoc[0], deathLoc[1], deathLoc[2]));

        playerUUID_toBreakCooldownTicks.put(player.getUniqueId().toString(), 9999999);
        playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.lavadeath", SoundCategory.MASTER);
    }

    public void endGame(){
        countdown = -1;
        gameStarted = false;
        terrainGeneration.wipeMapAndFillWithBlock(Material.AIR);

        for(Player player : playersInWorld){
            timeRemainingDisplay.setVisible(false);

            checkPlayerGameStatus.updateGameStatus(player, true);

            teleportPlayerToLobby(player);
            player.setGameMode(GameMode.ADVENTURE);

            player.getInventory().clear();
            player.closeInventory();

            playerInteraction.setReady(player, false);
            givePlayerResultsItem(player);

            endStatsUI.openEndStatsGUI(player);
            playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.gameend", SoundCategory.MASTER);
            playSoundEffect.stopPlayingSoundForPlayerByCategory(player, SoundCategory.MUSIC);
        }
    }

    public void update(){
        for(Player player : playerQueueToLobby){
            teleportPlayerToLobby(player);
        }
        playerQueueToLobby.clear();

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

            if(Objects.equals(countdownText, "0")){
                countdown = -1;
                gameStarted = true;
                startTimeMS = TimeStuff.getTimeMS();
                endTime = startTimeMS + (60*5) * 1000; // 5 minutes in ms

                for(Player player : playersInWorld){
                    player.setGameMode(GameMode.SURVIVAL);
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.gamestart", SoundCategory.MASTER);
                    playSoundEffect.stopPlayingSoundForPlayerByCategory(player, SoundCategory.MUSIC);
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.music", SoundCategory.MUSIC);
                }

                return;
            }

            for(Player playerToHear : playersInWorld){
                playSoundEffect.playSoundEffectForPlayer(playerToHear, "minecraft:tumble.cdtick", SoundCategory.MASTER);
                playerToHear.setGameMode(GameMode.SURVIVAL);
            }

            server.dispatchCommand(server.getConsoleSender(), command);
            lastCountdownText = countdownTextColor;
        }


        if(!hasGameStarted()){
            // Game not started! Wait...
            timeRemainingDisplay.setVisible(false);
            for(Player playerToHear : playersInWorld){
                playSoundEffect.stopPlayingSoundForPlayerByCategory(playerToHear, SoundCategory.MUSIC);
            }
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
            String message = "&3" + TumbleConstants.pluginMessagePrefix + "Time has run out! Returning to lobby.";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);

            endGame();
            return;
        }
        if(timeUntilEnd <= 60){
            if(!timeRemainingDisplay.isVisible()){
                for(Player player : playersInWorld) {
                    playSoundEffect.playSoundEffectForPlayer(player, "minecraft:tumble.showdown", SoundCategory.MASTER);
                }
            }

            // display that somewhere (preferably at the top)
            String timeLeftText = (int)Math.floor(timeUntilEnd) + " seconds to reach the end!";
            timeRemainingDisplay.setTitle(timeLeftText);
            timeRemainingDisplay.setVisible(true);

            //long currentTimeDiff = endTime - TimeStuff.getTimeMS();
            //long startTimeDiff = endTime - startTimeMS;

            double percent = (double) timeUntilEnd / 60;
            timeRemainingDisplay.setProgress( percent );
        }

        int playersAlive = 0;
        Player lastPlayerCheckedAlive = null;
        for(Player player : playersInWorld) {
            String UUID = player.getUniqueId().toString();

            if( !checkPlayerGameStatus.stillPlayingGame(player) ){ continue; }
            playerUUID_toDidPlayLastRound.put(UUID, true);
            playersAlive++;
            lastPlayerCheckedAlive = player;
            int newCooldown = playerUUID_toBreakCooldownTicks.get(UUID) - 1;
            playerUUID_toBreakCooldownTicks.put(UUID, newCooldown);

            playerUUID_toTimedSurvivedInRoundMS.put(UUID, TimeStuff.timeSinceMS(startTimeMS));

            int itemSlot = 1;

            if(useSnowballs) {
                ItemStack snowballs = new ItemStack(Material.SNOWBALL, 16);
                player.getInventory().setItem(itemSlot, snowballs);
                itemSlot++;
            }
            if(useShovels){
                ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
                player.getInventory().setItem(itemSlot, shovel);
                itemSlot++;
            }

            player.setExp(0);
            player.setLevel(0);
        }

        if(dontEndBecauseDeveloping){return;}
        if(playersAlive == 0 && returnToLobbyTimer == -1){
            //returnToLobbyTimer = 3f;
            String message = "&3" + TumbleConstants.pluginMessagePrefix + "Everyone has died! Returning to the lobby soon.";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
            endGame();
        }
        if(playersAlive == 1 && returnToLobbyTimer == -1){
            //returnToLobbyTimer = 3f;
            String message = "&3" + TumbleConstants.pluginMessagePrefix + lastPlayerCheckedAlive.getDisplayName() + " has won the game! Returning to the lobby soon.";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            broadcast(coloredMessage);
            endGame();
        }

    }
}
