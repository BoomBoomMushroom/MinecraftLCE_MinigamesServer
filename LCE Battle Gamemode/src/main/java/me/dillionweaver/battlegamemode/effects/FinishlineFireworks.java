package me.dillionweaver.battlegamemode.effects;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class FinishlineFireworks {
    public static void spawnFinishlineFireworks(Location location){
        World locWorld = location.getWorld();
        Vector locVec = location.toVector();
        float offsetInDir = 2f;

        Vector[] adjacentSpawnVectors = new Vector[]{
                new Vector(0,offsetInDir,0),
                new Vector(0,-offsetInDir,0),

                new Vector(offsetInDir,0,0),
                new Vector(-offsetInDir,0,0),

                new Vector(0,0,offsetInDir),
                new Vector(0,0,-offsetInDir),
        };

        for(Vector addingVector : adjacentSpawnVectors){
            Vector newVec = locVec.add(addingVector);
            Location newLocation = new Location(locWorld, newVec.getX(), newVec.getY(), newVec.getZ() );
            Firework firework = createFirework(newLocation);
            firework.detonate();
        }
    }

    public static Firework createFirework(Location location){
        Firework newFirework = location.getWorld().spawn(location, Firework.class);

        Color[] fireworkColors = new Color[]{
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.LIME,
                Color.OLIVE,
                Color.BLUE,
                Color.AQUA,
                Color.PURPLE,
                Color.SILVER,
                Color.MAROON,
                Color.NAVY,
                Color.TEAL
        };

        FireworkEffect colors = FireworkEffect.builder()
                .withColor(fireworkColors)
                .build();

        FireworkMeta fwMeta = newFirework.getFireworkMeta();
        fwMeta.addEffects(colors);
        fwMeta.setPower(4);
        newFirework.setFireworkMeta(fwMeta);

        newFirework.setTicksLived(2); // so it can blow up near instantly

        return newFirework;
    }
}
