package me.dillionweaver.tumblegamemode.commands;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import me.dillionweaver.tumblegamemode.data.TumbleConstants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TumbleStartGameCommand implements CommandExecutor {
    private final TumbleGamemode main;
    public TumbleStartGameCommand(TumbleGamemode main){
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
            String message = "&c"+ TumbleConstants.pluginMessagePrefix+"You must be an Admin to run this command!";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        int y = TumbleConstants.startY;
        main.terrainGeneration.wipeMapAndFillWithBlock(Material.AIR);
        List<Block> firstLayer = null;
        for(int i=0;i<TumbleConstants.numOfLayers;i++){
            List<Block> layer = main.terrainGeneration.generateLayer(new ArrayList<>(), y - (i*TumbleConstants.blocksBetweenLayers), true);
            if(firstLayer == null){
                firstLayer = layer;
            }
        }

        Location center = new Location(main.tumbleWorld, 0, 70, 0);

        for(Player player : main.playersInWorld){
            Location newLocation = player.getLocation();
            newLocation.setYaw(0);
            newLocation.setPitch(0);
            newLocation.setX( TumbleConstants.gameSpawnSpectating[0] );
            newLocation.setY( TumbleConstants.gameSpawnSpectating[1] );
            newLocation.setZ( TumbleConstants.gameSpawnSpectating[2] );
            player.teleport(newLocation);
            player.setInvisible(false);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.closeInventory();


            Block toSpawnOnBlock = firstLayer.get( main.random.nextInt(firstLayer.size()) );
            Location spawnLocation = new Location(main.tumbleWorld, toSpawnOnBlock.getX()+0.5, toSpawnOnBlock.getY()+1, toSpawnOnBlock.getZ()+0.5);
            player.teleport(spawnLocation);


            Location playerLocation = player.getLocation();
            double dx = center.getX() - playerLocation.getX();
            double dz = center.getZ() - playerLocation.getZ();
            double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90;
            playerLocation.setYaw((float) yaw);
            player.teleport(playerLocation);


            main.checkPlayerGameStatus.updateGameStatus(player, true);
            main.playerDeathOrderList.clear();

            String UUID = player.getUniqueId().toString();

            main.playerUUID_toBlocksMinedInRound.put(UUID, 0);
            main.playerUUID_toSnowballsThrownInRound.put(UUID, 0);
            main.playerUUID_toBreakCooldownTicks.put(UUID, 0);
            main.playerUUID_toDidPlayLastRound.put(UUID, true);
            main.playerUUID_toTimedSurvivedInRoundMS.put(UUID, 0L);

            player.setGameMode(GameMode.SURVIVAL);
        }

        main.countdown = 3;

        return true;
    }
}
