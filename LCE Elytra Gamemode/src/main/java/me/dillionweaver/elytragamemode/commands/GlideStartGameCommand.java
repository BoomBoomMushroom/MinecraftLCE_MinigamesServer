package me.dillionweaver.elytragamemode.commands;

import me.dillionweaver.elytragamemode.GlideGamemode;
import me.dillionweaver.elytragamemode.data.GlideConstants;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GlideStartGameCommand implements CommandExecutor {
    private final GlideGamemode main;
    public GlideStartGameCommand(GlideGamemode main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            if(!main.isPlayerInGameWorld((Player)commandSender)){
                String message = "&c"+ GlideConstants.pluginMessagePrefix +"You must be in the Elytra world to run this command!";
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                commandSender.sendMessage(coloredMessage);
                return false;
            }
        }
        if(!commandSender.isOp()){
            String message = "&c"+ GlideConstants.pluginMessagePrefix+"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        float x = 0f;
        float y = 0f;
        float z = 0f;

        float v = 0f;
        float v1 = 0f;

        int mapId = -1;
        if(args.length == 3){
            x = Float.parseFloat(args[0]);
            y = Float.parseFloat(args[1]);
            z = Float.parseFloat(args[2]);
        }
        else if(args.length == 1){
            try{
                mapId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                String lowerName = args[0].toLowerCase();
                mapId = Arrays.asList(GlideConstants.idToName).indexOf(lowerName);
            }
            if(mapId == -1 || mapId >= GlideConstants.idToName.length){
                // No map found
                String message = "&c"+ GlideConstants.pluginMessagePrefix+"No map found with index/name (" + args[0] + ")";
                String outText = ChatColor.translateAlternateColorCodes('&', message);
                if(commandSender instanceof Player){
                    ((Player) commandSender).sendMessage(outText);
                }
                else {
                    System.out.println(outText);
                }
                return false;
            }

            int[] coordsFromId = GlideConstants.idToCoordinates[mapId];
            if(coordsFromId.length == 2){
                coordsFromId = GlideConstants.checkpoints[coordsFromId[0]];
                x = (float) coordsFromId[6];
                y = (float) coordsFromId[7];
                z = (float) coordsFromId[8];

                v = (float) coordsFromId[9];
                v1 = (float) coordsFromId[10];
            }
            else{
                x = (float) coordsFromId[0];
                y = (float) coordsFromId[1];
                z = (float) coordsFromId[2];
            }
        }
        else{
            return false;
        }

        for(Player player : main.playersInWorld){
            Location newLocation = player.getLocation();
            newLocation.setYaw(v);
            newLocation.setPitch(v1);
            newLocation.setX(x);
            newLocation.setY(y);
            newLocation.setZ(z);
            player.teleport(newLocation);
            player.setInvisible(false);
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(6);
            player.getInventory().clear();
            player.closeInventory();

            main.checkPlayerGameStatus.updateGameStatus(player, true);
            player.setGliding(false);

            String UUID = player.getUniqueId().toString();

            main.playerUUID_toCheckpointId.put(UUID, GlideConstants.checkpointsInMapFromId[mapId][0] );
            main.playerUUID_toHasFinished.put(UUID, false);
            main.playerUUID_toVotedMapId.put(UUID, 0);

            main.playerUUID_toDamageInRound.put(UUID, 0);
            main.playerUUID_toDeathsInRound.put(UUID, 0);
            main.playerUUID_toTimeCompleted.put(UUID, 0L);
            main.playerUUID_isBoosting.put(UUID, false);
        }

        main.countdown = 3;
        main.mapId = mapId;
        main.playersWon = 0;

        return true;
    }
}
