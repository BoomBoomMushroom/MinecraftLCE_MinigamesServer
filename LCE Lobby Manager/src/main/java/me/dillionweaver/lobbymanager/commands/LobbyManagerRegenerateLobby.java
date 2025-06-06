package me.dillionweaver.lobbymanager.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import me.dillionweaver.lobbymanager.LobbyManager;
import me.dillionweaver.lobbymanager.data.LobbyManagerConstants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;

public class LobbyManagerRegenerateLobby implements CommandExecutor {
    private final LobbyManager main;
    public LobbyManagerRegenerateLobby(LobbyManager main){
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
            String message = "&c"+ LobbyManagerConstants.pluginMessagePrefix +"Too many or little arguments. Only one is needed! /LMRegenerateLobby WORLD_NAME_KEY";
            String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
            commandSender.sendMessage(coloredMessage);
            return false;
        }

        String worldNameKey = args[0];
        World worldToPlace = main.worldNameToWorld.get(worldNameKey);

        // world edit: kill @e[type=!player]; schematic load lobby_new; paste AT_POSITION LobbyManagerConstants.lobbyCornerToPaste,

        // load the schematic
        try {
            File schematicFile = new File(main.getDataFolder(), LobbyManagerConstants.lobbySchematicFilename);
            Location toLocation = new Location(worldToPlace, LobbyManagerConstants.lobbyCornerToPaste[0], LobbyManagerConstants.lobbyCornerToPaste[1], LobbyManagerConstants.lobbyCornerToPaste[2]);

            EditSession session = createEditSession(toLocation.getWorld());
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));
            Clipboard schematic = reader.read();

            // start paste
            Operation pasteOperation = new ClipboardHolder(schematic)
                    .createPaste(session)
                    .to(BukkitAdapter.asBlockVector(toLocation))
                    .copyEntities(true) // make sure we include entities so things like the armor stands and the item frames come along
                    .build();

            // remove existing entities inside the paste area
            Region pasteRegion = schematic.getRegion();
            for(Entity entity : toLocation.getWorld().getEntities()){
                if(entity.getType() == EntityType.PLAYER){ continue; }
                Location entityLoc = entity.getLocation();
                BlockVector3 blockPos = BlockVector3.at(entityLoc.getBlockX(), entityLoc.getBlockY(), entityLoc.getBlockZ());
                if(pasteRegion.contains(blockPos)){
                    entity.remove();
                }
            }
            // remove end

            // finish the paste
            Operations.complete(pasteOperation);

            // "Update" all the blocks to tell the client an update has happened!
            int minChunkX = pasteRegion.getMinimumPoint().getBlockX() >> 4;
            int minChunkZ = pasteRegion.getMinimumPoint().getBlockZ() >> 4;
            int maxChunkX = pasteRegion.getMaximumPoint().getBlockX() >> 4;
            int maxChunkZ = pasteRegion.getMaximumPoint().getBlockZ() >> 4;

            for (int x = minChunkX; x <= maxChunkX; x++) {
                for (int z = minChunkZ; z <= maxChunkZ; z++) {
                    toLocation.getWorld().refreshChunk(x, z);
                }
            }

            commandSender.sendMessage(LobbyManagerConstants.pluginMessagePrefix + " Regenerated the Lobby in " + worldNameKey);
            // end
        } catch (final Throwable t){
            t.printStackTrace();
        }

        return true;
    }

    private static EditSession createEditSession(World bukkitWorld){
        final EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(bukkitWorld));
        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }
}
