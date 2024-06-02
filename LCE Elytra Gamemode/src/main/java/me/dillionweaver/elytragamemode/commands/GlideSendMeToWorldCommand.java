package me.dillionweaver.elytragamemode.commands;

import me.dillionweaver.elytragamemode.GlideGamemode;
import me.dillionweaver.elytragamemode.data.GlideConstants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GlideSendMeToWorldCommand implements CommandExecutor {
    private final GlideGamemode main;
    public GlideSendMeToWorldCommand(GlideGamemode main){
        this.main = main;
    }

    public int tryParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            // String is not a valid integer, return the original string
            return -1; // Or any other default value you prefer for non-integers
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){}
        else{
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"You must be a player to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        Player player = (Player) commandSender;

        if(!player.isOp()){
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }
        if(args.length != 1){
            String message = "&c"+ GlideConstants.pluginMessagePrefix +"Too many or too few arguments. Just one is needed. /registerworld folder_name";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        Location newLoc = player.getLocation();

        String worldNameOrIndex = args[0];
        int worldIndex = tryParseInt(worldNameOrIndex);

        List<World> worlds = main.server.getWorlds();
        World toWorld = null;

        if(worldIndex != -1){
            if(worldIndex > worlds.size()){
                toWorld = worlds.get(worldIndex);
            }
            else{
                String message = "&c"+ GlideConstants.pluginMessagePrefix +"Index out of bounds!";
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                commandSender.sendMessage(coloredMessage);
                return false;
            }
        }
        else{
            toWorld = main.server.getWorld(worldNameOrIndex);
        }


        if(toWorld == null){ toWorld = main.glideWorld; }

        newLoc.setWorld(toWorld);
        player.teleport(newLoc);

        return true;
    }
}
