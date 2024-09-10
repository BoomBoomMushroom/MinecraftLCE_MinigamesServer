package me.dillionweaver.battlegamemode.data;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public class BattleConstants {

    public static final String[] idToName = new String[] { "Lobby" };
    public static final int[] votableMapIds = new int[]{

    };
    public static final int[][] allSpawnPoints = new int[][] {
            new int[] { -340, 58, -341 }, // Lobby
    };
    public static final int[][] mapIdToSpawnPointIndexes = new int[][] {
            new int[] { 0 },
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
