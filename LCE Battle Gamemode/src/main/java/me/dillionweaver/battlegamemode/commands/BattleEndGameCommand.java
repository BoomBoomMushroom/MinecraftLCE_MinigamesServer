package me.dillionweaver.battlegamemode.commands;

import me.dillionweaver.battlegamemode.BattleGamemode;
import me.dillionweaver.battlegamemode.data.BattleConstants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BattleEndGameCommand implements CommandExecutor {
    private final BattleGamemode main;
    public BattleEndGameCommand(BattleGamemode main){
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
            String message = "&c"+ BattleConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        main.endGame();

        String message = "&c"+ BattleConstants.pluginMessagePrefix +"The game was forcefully stopped by an Admin!";
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        main.broadcast(coloredMessage);

        return true;
    }
}
