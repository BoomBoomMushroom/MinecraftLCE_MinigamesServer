package me.dillionweaver.battlegamemode.data;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BattleConstants {

    public static final String[] idToName = new String[] { "Lobby", "Crucible (8 Players)", "Crucible (16 Players)" };
    public static final int[] votableMapIds = new int[]{
            1, 2, // Both Crucibles
    };
    public static final int[][] allSpawnPoints = new int[][] {
            new int[] { -340, 58, -341 }, // Lobby              0
            new int[] { -340, 58, -333 }, // Lobby
            new int[] { -332, 58, -333 }, // Lobby
            new int[] { -324, 58, -333 }, // Lobby
            new int[] { -324, 58, -341 }, // Lobby
            new int[] { -324, 58, -350 }, // Lobby              5
            new int[] { -332, 56, -350 }, // Lobby
            new int[] { -341, 56, -350 }, // Lobby
            new int[] { -340, 56, -344 }, // Lobby

            new int[] { -782, 41, 121 }, // crucible (8 plr)
            new int[] { -780, 41, 115 }, // crucible (8 plr)    10
            new int[] { -774, 41, 113 }, // crucible (8 plr)
            new int[] { -768, 41, 115 }, // crucible (8 plr)
            new int[] { -766, 41, 121 }, // crucible (8 plr)
            new int[] { -768, 41, 127 }, // crucible (8 plr)
            new int[] { -774, 41, 129 }, // crucible (8 plr)    15
            new int[] { -780, 41, 127 }, // crucible (8 plr)

            new int[] { -897, 48, -341 }, // crucible (16 plr)
            new int[] { -904, 48, -337 }, // crucible (16 plr)
            new int[] { -911, 48, -341 }, // crucible (16 plr)
            new int[] { -915, 48, -348 }, // crucible (16 plr)  20
            new int[] { -911, 48, -355 }, // crucible (16 plr)
            new int[] { -904, 48, -359 }, // crucible (16 plr)
            new int[] { -897, 48, -355 }, // crucible (16 plr)
            new int[] { -893, 48, -348 }, // crucible (16 plr)
            new int[] { -894, 45, -328 }, // crucible (16 plr)  25
            new int[] { -924, 45, -340 }, // crucible (16 plr)
            new int[] { -925, 38, -359 }, // crucible (16 plr)
            new int[] { -914, 45, -368 }, // crucible (16 plr)
            new int[] { -830, 33, -362 }, // crucible (16 plr)
            new int[] { -883, 45, -358 }, // crucible (16 plr)  30
            new int[] { -884, 39, -337 }, // crucible (16 plr)
            new int[] { -918, 33, -334 }, // crucible (16 plr)
    };
    public static final int[][] mapIdToSpawnPointIndexes = new int[][] {
            new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, // lobby
            new int[] { 9, 10, 11, 12, 13, 14, 15, 16 }, // crucible 8 plr
            new int[] { 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 }, // crucible 16 plr
    };

    public static final Material[] cannotDropItems = new Material[]{
            Material.ELYTRA,
            Material.MAP,
            Material.EMERALD_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.GOLD_INGOT,
            Material.NETHER_STAR,
    };

    public static final Material adminSelectionMakerItem = Material.WOODEN_HOE;

    // item slots: https://proxy.spigotmc.org/3d5ceb0e4998f49be1771df5d1bb62d6c68ebb41?url=https%3A%2F%2Fbugs.mojang.com%2Fsecure%2Fattachment%2F61101%2FItems_slot_number.jpg

    public static final int viewResultsItemSlot = 6;
    public static final int setReadyItemSlot = 7;
    public static final int mapVotingItemSlot = 8;

    public static final String pluginMessagePrefix = "[Battle] ";
    public static final String votingMenuTitle = "Vote for a map";
    public static final String endResultTitle = "Results";

    public static final boolean stopPlayerFromInteractingWithWhateverTheyWant = true;

    public static final String saveDataFilename = "BattleSaveData.yml";
    public static final String battleWorldKeyName = "BattleWorld";
}
