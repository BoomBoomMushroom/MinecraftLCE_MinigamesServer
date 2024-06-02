package me.dillionweaver.elytragamemode.player;

import me.dillionweaver.elytragamemode.GlideGamemode;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CheckPlayerGameStatus {
    private final GlideGamemode main;
    public CheckPlayerGameStatus(GlideGamemode main){
        this.main = main;
    }

    public boolean stillPlayingGame(Player player){
        if(!main.isPlayerInGameWorld(player)){ return false; }
        //return !player.isInvisible();
        if(main.playerUUID_toHasFinished.get(player.getUniqueId().toString())){ return false; };
        return !(player.getGameMode() != GameMode.ADVENTURE && player.isOp());
    }

    public void updateGameStatus(Player player, boolean stillInGame){
        player.setGameMode(GameMode.ADVENTURE);
        //player.setInvisible(!stillInGame);
        //player.setAllowFlight(!stillInGame);
        //player.setFlying(!stillInGame);

        if(stillInGame){
            ItemStack elytraToSet = new ItemStack(Material.ELYTRA, 1);
            player.getInventory().setChestplate(elytraToSet);
            player.setGliding(true);
        }
        else{
            player.getInventory().clear();
        }
    }
}
