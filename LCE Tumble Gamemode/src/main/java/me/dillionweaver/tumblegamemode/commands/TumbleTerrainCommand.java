package me.dillionweaver.tumblegamemode.commands;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class TumbleTerrainCommand implements CommandExecutor {
    private final TumbleGamemode main;
    public TumbleTerrainCommand(TumbleGamemode main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            if(!main.isPlayerInGameWorld((Player)commandSender)){
                String message = "&c"+ TumbleConstants.pluginMessagePrefix +"You must be in the Tumble world to run this command!";
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                commandSender.sendMessage(coloredMessage);
                return false;
            }
        }
        if(!commandSender.isOp()){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        if(args.length != 1){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"You must have one argument to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        if(Objects.equals(args[0], "wipe")){
            main.terrainGeneration.wipeMapAndFillWithBlock(Material.AIR);
        }
        if(Objects.equals(args[0], "layer")){
            main.terrainGeneration.generateLayer(new ArrayList<>(), TumbleConstants.startY, true);
        }


        //String message = "&c"+ TumbleConstants.pluginMessagePrefix +"The game was forcefully stopped by an Admin!";
        //String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        //main.broadcast(coloredMessage);

        return true;
    }
}
