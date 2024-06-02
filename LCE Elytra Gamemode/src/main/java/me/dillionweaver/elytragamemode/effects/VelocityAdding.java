package me.dillionweaver.elytragamemode.effects;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VelocityAdding {
    public static final Vector VECTOR_ZERO = new Vector(0, 0, 0);
    public static final Vector VECTOR_UP = new Vector(0, 1, 0);
    public void addVelocity(Player player, Vector direction, double forceMultiplier, boolean doUpwardsForce, boolean usePlayerFacing){
        if(player == null){ return; }
        Vector playerFacing = player.getLocation().getDirection();
        playerFacing.setY(0);
        direction = direction.add(playerFacing);

        Vector velocity = player.getVelocity();

        Vector combinedForce = VECTOR_ZERO;

        direction = direction.multiply(forceMultiplier);
        if(doUpwardsForce){
            combinedForce = velocity.clone().add( new Vector(0, 0.25, 0).multiply(forceMultiplier) );
        }
        else {
            combinedForce = velocity.clone().add(direction);
        }

        player.setVelocity(combinedForce);
    }
}
