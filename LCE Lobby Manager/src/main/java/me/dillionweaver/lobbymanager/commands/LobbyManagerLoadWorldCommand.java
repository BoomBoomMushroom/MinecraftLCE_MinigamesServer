package me.dillionweaver.lobbymanager.commands;

import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class LobbyManagerLoadWorldCommand implements CommandExecutor {
    private final LobbyManager main;
    public LobbyManagerLoadWorldCommand(LobbyManager main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!commandSender.isOp()){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }
        if(args.length != 1){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"Too many or too few arguments. Just one is needed. /registerworld folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        String worldFolderName = args[0];
        File worldContainer = main.server.getWorldContainer();
        File worldFolder = new File(worldContainer, worldFolderName);

        if( !worldFolder.exists() ){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"World folder does not exists! Make sure you typed it right and it's all lowercase!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        };

        WorldCreator creator = new WorldCreator(worldFolderName);
        World createdWorld = main.server.createWorld(creator);

        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9"+ LobbyManagerConstants.pluginMessagePrefix+"World registered successfully"));

        return true;
    }
}
