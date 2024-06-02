package me.dillionweaver.tumblegamemode.player;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckPlayerGameStatus {
    private final TumbleGamemode main;
    public CheckPlayerGameStatus(TumbleGamemode main){
        this.main = main;
    }

    public boolean stillPlayingGame(Player player){
        if(!main.isPlayerInGameWorld(player)){
            return false;
        }

        if(main.playerDeathOrderList.contains(player)){
            return false;
        }
        if(player.isInvisible()) {
            return false;
        }

        return player.getGameMode() == GameMode.SURVIVAL;
    }

    public void updateGameStatus(Player player, boolean stillInGame){
        player.setGameMode(stillInGame ? GameMode.SURVIVAL : GameMode.ADVENTURE);
        player.setInvisible(!stillInGame);
        player.setAllowFlight(!stillInGame);
        player.setFlying(!stillInGame);

        if(stillInGame){
            main.playerSpectating.removePlayerSpectatingAnimal(player);
        }
        else{
            main.playerSpectating.spawnPlayerSpectatingAnimal(player, EntityType.PARROT);
            player.getInventory().clear();
        }
    }
}
