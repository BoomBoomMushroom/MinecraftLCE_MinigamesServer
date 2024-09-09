package me.dillionweaver.battlegamemode.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerAreaMath {
    public boolean playerInArea(Player player, Location a, Location b){
        Location playerLoc = player.getLocation();
        int px = playerLoc.getBlockX();
        int py = playerLoc.getBlockY();
        int pz = playerLoc.getBlockZ();

        int ax = a.getBlockX();
        int ay = a.getBlockY();
        int az = a.getBlockZ();

        int bx = b.getBlockX();
        int by = b.getBlockY();
        int bz = b.getBlockZ();

        // biggest is A | sort
        if(bx < ax){
            int tmp = ax;
            ax = bx;
            bx = tmp;
        }
        if(by < ay){
            int tmp = ay;
            ay = by;
            by = tmp;
        }
        if(bz < az){
            int tmp = az;
            az = bz;
            bz = tmp;
        }

        return (px >= ax && px <= bx && py >= ay && py <= by && pz >= az && pz <= bz);
    }
}
