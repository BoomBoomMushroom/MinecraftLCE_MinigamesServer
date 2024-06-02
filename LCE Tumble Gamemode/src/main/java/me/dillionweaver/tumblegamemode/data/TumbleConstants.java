package me.dillionweaver.tumblegamemode.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class TumbleConstants {
    public static final int[][] lobbySpawnPoints = new int[][] {
            new int[] { -341, 60, -341 }, // lobby          0
            new int[] { -341, 60, -332 }, // lobby
            new int[] { -332, 60, -332 }, // lobby
            new int[] { -324, 60, -332 }, // lobby
            new int[] { -324, 60, -341 }, // lobby
            new int[] { -324, 60, -350 }, // lobby          5
            new int[] { -332, 60, -350 }, // lobby
            new int[] { -341, 60, -350 }, // lobby
            new int[] { -341, 60, -344 }, // lobby
            new int[] { -341, 60, -338 }, // lobby
            new int[] { -335, 60, -332 }, // lobby          10
            new int[] { -329, 60, -332 }, // lobby
            new int[] { -324, 60, -338 }, // lobby
            new int[] { -324, 60, -344 }, // lobby
            new int[] { -329, 60, -350 }, // lobby
            new int[] { -335, 60, -350 }, // lobby          15
    };

    public static final EntityType spectatingAnimal = EntityType.PARROT;

    public static final int[] gameSpawnSpectating = new int[]{0, 80, 0};

    public static final int maxRadius = 15;
    public static final int minRadius = 5;
    public static final int startY = 70;
    public static final int deathY = 24;
    public static final int numOfLayers = 3;
    public static final int blocksBetweenLayers = 7;

    public static final int[] fillCornerA = new int[]{40, 90, 40};
    public static final int[] fillCornerB = new int[]{-40, 35, -40};

    public static final Material[] cannotDropItems = new Material[]{
            Material.EMERALD_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.GOLD_INGOT,
            Material.SNOWBALL,
            Material.DIAMOND_SHOVEL
    };

    public static final Material adminSelectionMakerItem = Material.WOODEN_AXE;

    // item slots: https://proxy.spigotmc.org/3d5ceb0e4998f49be1771df5d1bb62d6c68ebb41?url=https%3A%2F%2Fbugs.mojang.com%2Fsecure%2Fattachment%2F61101%2FItems_slot_number.jpg

    public static final int viewResultsItemSlot = 7;
    public static final int setReadyItemSlot = 8;

    public static final String pluginMessagePrefix = "[Tumble] ";
    public static final String endResultTitle = "Results";

    public static final boolean stopPlayerFromInteractingWithWhateverTheyWant = true;

    public static final String saveDataFilename = "TumbleSaveData.yml";
    public static final String tumbleWorldKeyName = "TumbleWorld";
}
