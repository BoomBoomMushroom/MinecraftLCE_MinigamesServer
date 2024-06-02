package me.dillionweaver.lobbymanager.commands;

import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Arrays;

public class LobbyManagerRegisterWorldCommand implements CommandExecutor {
    private final LobbyManager main;
    public LobbyManagerRegisterWorldCommand(LobbyManager main){
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
        if(args.length != 3){
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"Too many or too few arguments. Just one is needed. /registerworld folder_name world_name LOGO_ITEM_NAME";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        String worldFolderName = args[0];
        String worldName = args[1];
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

        main.fileSaveData.setDataFileKey(worldName, createdWorld.getName());
        main.fileSaveData.setDataFileKey(worldName + "_icon", args[2]);

        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9"+ LobbyManagerConstants.pluginMessagePrefix+"World registered successfully"));

        return true;
    }
}
