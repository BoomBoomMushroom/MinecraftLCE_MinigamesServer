package me.dillionweaver.battlegamemode.commands;

import me.dillionweaver.battlegamemode.BattleGamemode;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BattleStartGameCommand implements CommandExecutor {
    private final BattleGamemode main;
    public BattleStartGameCommand(BattleGamemode main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            if(!main.isPlayerInGameWorld((Player)commandSender)){
                String message = "&c"+ BattleConstants.pluginMessagePrefix +"You must be in the Battle world to run this command!";
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                commandSender.sendMessage(coloredMessage);
                return false;
            }
        }
        if(!commandSender.isOp()){
            String message = "&c"+ BattleConstants.pluginMessagePrefix+"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        int[] allCheckpoints = new int[] {};

        float v = 0f;
        float v1 = 0f;

        int mapId = -1;
        if(args.length == 1){
            try{
                mapId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                String lowerName = args[0].toLowerCase();
                mapId = Arrays.asList(BattleConstants.idToName).indexOf(lowerName);
            }
            if(mapId == -1 || mapId >= BattleConstants.idToName.length){
                // No map found
                String message = "&c"+ BattleConstants.pluginMessagePrefix+"No map found with index/name (" + args[0] + ")";
                String outText = ChatColor.translateAlternateColorCodes('&', message);
                if(commandSender instanceof Player){
                    ((Player) commandSender).sendMessage(outText);
                }
                else {
                    System.out.println(outText);
                }
                return false;
            }

            int[] allCheckpointsId = BattleConstants.mapIdToSpawnPointIndexes[mapId];
            if(allCheckpointsId.length > 0){
                allCheckpoints = allCheckpointsId;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }

        int checkpointIndex = 0;
        for(Player player : main.playersInWorld){
            if(checkpointIndex >= allCheckpoints.length){
                checkpointIndex = 0;
            }

            int[] checkpoint = BattleConstants.allSpawnPoints[allCheckpoints[checkpointIndex]];
            float x = checkpoint[0];
            float y = checkpoint[1];
            float z = checkpoint[2];

            checkpointIndex++;

            Location newLocation = player.getLocation();
            newLocation.setYaw(v);
            newLocation.setPitch(v1);
            newLocation.setX(x);
            newLocation.setY(y);
            newLocation.setZ(z);
            player.teleport(newLocation);
            player.setInvisible(false);
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20);
            player.getInventory().clear();
            player.closeInventory();
            player.setGliding(false);

            String UUID = player.getUniqueId().toString();

            main.playerUUID_toVotedMapId.put(UUID, 0);
            main.playerUUID_isAlive.put(UUID, true);
        }

        main.countdown = 10;
        main.invulnerabilityTimer = 15;
        main.mapId = mapId;

        return true;
    }
}
