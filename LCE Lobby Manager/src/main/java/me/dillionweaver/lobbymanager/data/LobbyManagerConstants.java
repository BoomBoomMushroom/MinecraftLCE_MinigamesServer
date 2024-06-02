package me.dillionweaver.lobbymanager.data;

import org.bukkit.Material;

public class LobbyManagerConstants {
    public static final int[][] lobbySpawnPoints = new int[][] {
            new int[] { -341, 57, -341 }, // lobby          0
            new int[] { -341, 57, -332 }, // lobby
            new int[] { -332, 57, -332 }, // lobby
            new int[] { -324, 57, -332 }, // lobby
            new int[] { -324, 57, -341 }, // lobby
            new int[] { -324, 57, -350 }, // lobby          5
            new int[] { -332, 57, -350 }, // lobby
            new int[] { -341, 57, -350 }, // lobby
            new int[] { -341, 57, -344 }, // lobby
            new int[] { -341, 57, -338 }, // lobby
            new int[] { -335, 57, -332 }, // lobby          10
            new int[] { -329, 57, -332 }, // lobby
            new int[] { -324, 57, -338 }, // lobby
            new int[] { -324, 57, -344 }, // lobby
            new int[] { -329, 57, -350 }, // lobby
            new int[] { -335, 57, -350 }, // lobby          15
    };

    public static final Material[] cannotDropItems = new Material[]{
            Material.NETHER_STAR,
    };

    public static final Material adminSelectionMakerItem = Material.WOODEN_AXE;

    // item slots: https://proxy.spigotmc.org/3d5ceb0e4998f49be1771df5d1bb62d6c68ebb41?url=https%3A%2F%2Fbugs.mojang.com%2Fsecure%2Fattachment%2F61101%2FItems_slot_number.jpg
    public static final int setLobbyHopItemSlot = 0;

    public static final String pluginMessagePrefix = "[Lobby Manager] ";
    public static final String lobbySwitchGuiName = "Choose Lobby";

    public static final boolean stopPlayerFromInteractingWithWhateverTheyWant = true;

    public static final String saveDataFilename = "LobbyManagerSaveData.yml";
    public static final String lobbyWorldKeyName = "LobbyWorld";
}
