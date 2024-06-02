package me.dillionweaver.tumblegamemode.commands;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TumbleEndGameCommand implements CommandExecutor {
    private final TumbleGamemode main;
    public TumbleEndGameCommand(TumbleGamemode main){
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

        main.endGame();

        String message = "&c"+ TumbleConstants.pluginMessagePrefix +"The game was forcefully stopped by an Admin!";
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        main.broadcast(coloredMessage);

        return true;
    }
}
