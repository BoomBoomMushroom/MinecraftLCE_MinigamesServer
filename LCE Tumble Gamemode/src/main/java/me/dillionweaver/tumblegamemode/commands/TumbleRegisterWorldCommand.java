package me.dillionweaver.tumblegamemode.commands;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class TumbleRegisterWorldCommand implements CommandExecutor {
    private final TumbleGamemode main;
    public TumbleRegisterWorldCommand(TumbleGamemode main){
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
        if(args.length != 1){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"Too many or too few arguments. Just one is needed. /registerworld folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        String worldFolderName = args[0];
        File worldContainer = main.server.getWorldContainer();
        File worldFolder = new File(worldContainer, worldFolderName);

        if( !worldFolder.exists() ){
            String message = "&c"+ TumbleConstants.pluginMessagePrefix +"World folder does not exists! Make sure you typed it right and it's all lowercase!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        };

        World preWorldQuery = main.server.getWorld(worldFolderName);

        if(preWorldQuery != null){
            main.fileSaveData.setDataFileKey(TumbleConstants.tumbleWorldKeyName, preWorldQuery.getName());
        }
        else{
            WorldCreator creator = new WorldCreator(worldFolderName);
            World createdWorld = main.server.createWorld(creator);

            main.fileSaveData.setDataFileKey(TumbleConstants.tumbleWorldKeyName, createdWorld.getName());
        }

        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9"+ TumbleConstants.pluginMessagePrefix+"World registered successfully"));

        return true;
    }
}
