package me.dillionweaver.tumblegamemode.data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AreaMath {
    public static boolean locationInArea(Location start, Location a, Location b){
        int px = start.getBlockX();
        int py = start.getBlockY();
        int pz = start.getBlockZ();

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
