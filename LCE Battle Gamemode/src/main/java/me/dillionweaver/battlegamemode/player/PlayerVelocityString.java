package me.dillionweaver.battlegamemode.player;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

public class PlayerVelocityString {
    public double getPlayerSpeed(Player player){
        Vector playerVel = player.getVelocity();
        playerVel.add(new Vector(0, +0.0784000015258789, 0)); // account for gravity
        double speed = playerVel.length();

        return speed;
    }
    public String getMetersPerSecondString(Player player){
        double speed = getPlayerSpeed(player) * 20;
        DecimalFormat df = new DecimalFormat("00.00");
        String roundedSpeed = df.format( speed );

        return roundedSpeed;
    }
}
