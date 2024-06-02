package me.dillionweaver.tumblegamemode.data;

import org.bukkit.entity.Player;

public class FunnySuperlatives {
    /*
    public static String getSuperlative(long timeSurvived, int blocksMined, int snowballsThrown) {
        String superlative = "You somehow managed to survive for ";

        // Time Survived
        if (timeSurvived <= 60000) {
            superlative += "a blink and ";
        } else if (timeSurvived <= 180000) {
            superlative += "a short while and ";
        } else if (timeSurvived <= 300000) {
            superlative += "the entire game and ";
        } else {
            superlative += "some suspicious amount of time and "; // For potential cheaters
        }

        // Blocks Mined
        switch (blocksMined) {
            case 0:
                superlative += "barely touched a pickaxe. ";
                break;
            case 1:
            case 2:
                superlative += "mined a whopping " + blocksMined + " block. ";
                break;
            case int i when i <= 10:
                superlative += "mined a measly " + blocksMined + " blocks. ";
                break;
            case int i when i <= 25:
                superlative += "were a pickaxe enthusiast, mining " + blocksMined + " blocks. ";
                break;
            default:
                superlative += "went on a block-breaking rampage, mining a staggering " + blocksMined + " blocks! ";
        }

        // Snowballs Thrown
        switch (snowballsThrown) {
            case 0:
                superlative += "threw zero snowballs. ";
                break;
            case 1:
            case 2:
                superlative += "threw a measly " + snowballsThrown + " snowball. ";
                break;
            case int i when i <= 5:
                superlative += "threw a few " + snowballsThrown + " snowballs. ";
                break;
            case int i when i <= 10:
                superlative += "were a snowball slinger, throwing " + snowballsThrown + " snowballs! ";
                break;
            default:
                superlative += "unleashed a blizzard of snowballs, throwing a whopping " + snowballsThrown + "! ";
        }

        return superlative;
    }
    */

    /*
    // Define funny superlative tiers for each stat
    private static final String[][] tiers = {
            {"Eternal", "Timelord", "Time Traveler", "Time Tourist", "Clock Watcher"},  // Time survived (ms)
            {"Blockbuster", "Quarry King", "Shovel Shah", "Clicky Clicky", "Stonecutter"},  // Blocks mined
            {"Snownado", "Blizzard Brawller", "Snowballer", "Snowfighter", "Snow Novice"}, // Snowballs thrown
    };

    public static String getSuperlative(int statValue, int statType) {
        // Check if statType is within valid range
        if (statType < 0 || statType >= tiers.length) {
            return "Invalid Stat Type";
        }

        // Find the appropriate tier based on the stat value
        for (int i = 0; i < tiers[statType].length; i++) {
            if (statValue > getTierThreshold(tiers[statType], i)) {
                return tiers[statType][i];
            }
        }
        return tiers[statType][tiers[statType].length - 1];  // Return the last tier if no threshold is met
    }

    private static int getTierThreshold(String[] tierList, int tierIndex) {
        // Base case: threshold for first tier is 0
        if (tierIndex == 0) {
            return 0;
        }
        // Recursive case: get threshold for previous tier
        return getTierThreshold(tierList, tierIndex - 1);
    }
    */
}
