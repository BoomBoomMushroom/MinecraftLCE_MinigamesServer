package me.dillionweaver.elytragamemode.commands;

import me.dillionweaver.elytragamemode.GlideGamemode;
import me.dillionweaver.elytragamemode.data.GlideConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlideEndGameCommand implements CommandExecutor {
    private final GlideGamemode main;
    public GlideEndGameCommand(GlideGamemode main){
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
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        main.endGame();

        String message = "&c"+ GlideConstants.pluginMessagePrefix +"The game was forcefully stopped by an Admin!";
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        main.broadcast(coloredMessage);

        return true;
    }
}
