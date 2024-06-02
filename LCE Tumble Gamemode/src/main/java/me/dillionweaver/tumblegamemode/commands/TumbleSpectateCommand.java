package me.dillionweaver.tumblegamemode.commands;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;

public class TumbleSpectateCommand implements CommandExecutor {
    private final TumbleGamemode main;
    public TumbleSpectateCommand(TumbleGamemode main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.isOp()){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        if(commandSender instanceof Player){}
        else{
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"Must be a player to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }
        Player player = (Player) commandSender;

        Entity spectatingEntity = main.playerSpectating.playerToSpecAnimal.get(player);
        if(spectatingEntity != null){
            main.playerSpectating.removePlayerSpectatingAnimal(player);
        }
        else{
            main.playerSpectating.spawnPlayerSpectatingAnimal(player, EntityType.PARROT);
        }

        return true;
    }
}
