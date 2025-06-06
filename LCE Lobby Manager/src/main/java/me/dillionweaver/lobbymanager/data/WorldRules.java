package me.dillionweaver.lobbymanager.data;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;

public class WorldRules {
    /*
    You get the rules and stuff from the "GamesRulesFromDump" Folder
    MG01 -> Battle
    MG02 -> Tumble
    MG03 -> Glide
    */

    // Lobby Rules
    public static final Material[] LobbyDestroyPermissionWhitelist = new Material[]{};
    public static final Material[] LobbyPlacePermissionWhitelist = new Material[]{};
    public static final Material[] LobbyUsePermissionWhitelist = new Material[]{
            Material.BOW,
            Material.ARROW,
            Material.FISHING_ROD,
            Material.SNOWBALL,
            Material.ELYTRA,
            Material.MUSIC_DISC_WARD,
            Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_STRAD,
            Material.MUSIC_DISC_11,
            Material.MUSIC_DISC_WAIT, // "Where are we now" was the old name
            Material.MUSIC_DISC_CAT,
            Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_CHIRP,
            Material.MUSIC_DISC_FAR,
            Material.MUSIC_DISC_STAL,
            Material.MUSIC_DISC_MALL,
            Material.MUSIC_DISC_MELLOHI,
    };
    public static final Material[] LobbyBlockUsePermissionWhitelist = new Material[]{
            Material.OAK_DOOR,
            Material.IRON_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,

            Material.IRON_TRAPDOOR,
            Material.OAK_TRAPDOOR, // it says "Wooden Trapdoor" but we need to be specific in plugins
            Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.ACACIA_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,

            Material.STONE_BUTTON,
            Material.OAK_BUTTON, // it says "Woodean Button" I assume its suppost to say wooden and I again need to type them all out
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,

            Material.LEVER,

            Material.CHEST,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,
            Material.NOTE_BLOCK,
            Material.JUKEBOX,

            Material.OAK_FENCE_GATE, // this time it actually listed them all out!
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
    };
        // Timers
    public static final int LobbyGameInitTimerDuration = 15;
    public static final int LobbyGameInitTimerAnnounceEvery = 1;
    public static final int LobbyIntervalTimerDuration = 60;
    public static final int LobbyIntervalTimerAnnounceEvery = 1;
        // Sounds
    public static final int LobbyTimerSound = 5; // "ForLast" meaning play it for the last 5 seconds
    public static final boolean LobbyShowNameTags = true;


    // MG01 - Battle

        // Host Options
    public static final int BattleRoundLength = 300; // Short = 180, Normal = 300, Long = 600
    public static final int BattleSpectateMode = 1; // Either 0, 1, 2, 3, 4
    public static final int BattleMaxPlayers = 16; // Either 8 or 16; default is 16
    /*  Start Conditions
    <PlayersCondition op="GreaterThanOrEquals" value="2" activeOnly="false" readyOnly="true"/>
    Start if 2 or more people are ready
        End Conditions
    <RoundTimeCondition value="$RoundLength" /> <!-- in seconds -->
    <PlayersCondition op="LessThanOrEquals" value="1" activeOnly="true" readyOnly="false"/>
    End if the time runs out or only 1 player is alive
        Showdown Conditions
    <!-- If its been 1 minute since we reached 1 vs 1 -->
	<TimeSincePlayersCondition value="60" playerCount="3" />
	<!-- If the round is almost over -->
	<RoundTimeCondition value="$RoundLength" offset="-70" />
	if we've been in a 1v1 for 1 minute or 70 seconds before the game is over
    */
    public static final Difficulty BattleDifficultyOverride = Difficulty.HARD;
    public static final GameMode BattlePlayerGamemode = GameMode.ADVENTURE;
    public static final int BattleLivesPerRound = 1;
    public static final int BattleRoundCount = 1;
    public static final int BattleTeamCount = 16;
    public static final int BattleTeamMaxSize = 2;
    public static final int BattleMaxPlayersForSmallMaps = 6;
	// BattleSpectateMode = $SpectateMode
    public static final boolean BattleShowInvulnerableEffect = true;
    public static final Material[] BattleDestroyPermissionWhitelist = new Material[]{};
    public static final Material[] BattlePlacePermissionWhitelist = new Material[]{
            Material.TNT
    };
    public static final Material[] BattleUsePermissionBlacklist = new Material[]{
            Material.WOODEN_HOE,
            Material.STONE_HOE,
            Material.IRON_HOE,
            Material.DIAMOND_HOE,
            Material.GOLDEN_HOE,
            Material.SKELETON_SKULL, // Skulls in general (id: 397); but why are they here?!

            Material.IRON_SHOVEL,
            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.GOLDEN_SHOVEL,
    }; // Dillion: this blacklist is to prevent players from hoeing the ground and doing something with a skull?
    public static final Material[] BattleBlockUsePermissionWhitelist = new Material[]{
            Material.OAK_DOOR,
            Material.IRON_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,

            Material.IRON_TRAPDOOR,
            Material.OAK_TRAPDOOR, // it says "Wooden Trapdoor" but we need to be specific in plugins
            Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.ACACIA_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,

            Material.STONE_BUTTON,
            Material.OAK_BUTTON, // it says "Woodean Button" I assume its suppost to say wooden and I again need to type them all out
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,

            Material.LEVER,

            Material.CHEST,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,
    };
    public static final boolean BattleBlockTickingOverride = true;
    public static final Material[] BattleBlockTickingSpecificOverride = new Material[]{
        Material.BROWN_MUSHROOM, // again not sure if the mushrooms should be blocks or the plant ones
        Material.RED_MUSHROOM,
        Material.ICE,
    };
        // Timers
    public static final int BattleRoundPreparingTimer = 5; // Time spent preparing before we start checking if the start conditions are satisfied.
    public static final int BattleRoundStartDelayTimer = 10;
    public static final int BattleRoundStartDelayTimerAnnounceEvery = 1; // Start conditions satisfied, warning countdown before players are set loose.
    public static final int BattleGracePeriodTimer = 15; // Timer that ends the grace period and makes the players mortal.
    public static final int BattleGracePeriodAnnounceEvery = 1;
    public static final int BattleRoundEndDelayTimer = 10; // Triggered by the EndConditions being satified, and when finished starts respawns the players in the lobby.
    public static final int BattleRoundEndDelayTimerAnnounceEvery = 1;
    public static final int BattleRoundLimitTimer = 60; // Triggered on round start: timer force ends the game.
    public static final int BattleRoundLimitTimerAnnounceEvery = 1;
        // Sounds
    public static final int battleRoundStartTimerSoundForLast = 5;
    public static final int battleGraceTimerSoundForLast = 3;
    public static final int battleRoundLimitTimerSoundForLast = 30;
        // Game Balance Settings
    public static final double BattleSprintSpeedBalance = 1.3;
    /*
    4J-JEV: Valid tags.

    MaxFood: Maximum food points a player can have.
    MaxSaturation: Maximum food saturation a player can have.
    StartSaturation: Food saturation a player starts with.
    SaturationFloor: Min saturation level.
    ExhaustionDrop: The amount of exhaustion that causes a drop in 1 saturation/food level.
    HealthTickCount: How long it takes the player to regenerate health when healing.
    HealLevel: Minimum food level where the player will still heal.
    StarveLevel: Maximum food level where the player will starve.
    SprintLevel: 4J-ADDED: Minimum food required to sprint
    ExhaustionHeal: Exhaustion caused by healing (faster when health regenerates faster).
    ExhaustionJump: Exhaustion caused by jumping
    ExhaustionSprintJump: Exhaustion caused by long jumping
    ExhaustionMine: Exhaustion caused by breaking blocks
    ExhaustionAttack: Exhaustion caused by attacking
    ExhaustionDamage: Exhaustion caused by receiving damage

    4J-JEV: These are all per meter.
    ExhaustionWalk: Exhaustion caused by walking
    ExhaustionSprint: Exhaustion caused by sprinting
    ExhaustionSwim: Exhaustion caused by swimming

    4J-Added: This is per tick (keep much lower than the others)
    ExhaustionIdle: Exhaustion caused by existing

    4J IME - defaults listed below are for Survival as of 13/09/2017
    */
        // Food Balance
    public static final FoodBalance BattleStandardFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4.0, 40, 40, 4, 0, 0,
            4, 0.2, 0.8, 0.025, 0.3, 0.1, 0.04, 0.04, 0.085, 0.03, 0.004);
    // Fast Hunger - less punishment for action, triple idle hunger.
    public static final FoodBalance BattleFastHungerFoodBalance = new FoodBalance(
            20, 4, 4, 0, 5, 60, 40, 4, 0, 0,
            5, 0.2, 0.8, 0.025, 0.3, 0.1, 0, 0, 0.05, 0, 0.012);
    public static final FoodBalance BattleFastHealingFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4, 20, 10, 8, 0, 0,
            5, 0.2, 0.8, 0.025, 0.3, 0.1, 0, 0, 0.05, 0, 0.004);
    public static final FoodBalance BattleNoStarvingFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4, 40, 40, 4, -1, 0,
            4, 0.2, 0.8, 0.025, 0.3, 0.1, 0.04, 0.04, 0.085, 0.03, 0.004);
    public static final FoodBalance BattleAlwaysHealingFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4, 100, 40, 0, -1, 6,
            4, 0.2, 0.8, 0.025, 0.3, 0.1, 0.04, 0.04, 0.085, 0.03, 0.004);
    public static final FoodBalance BattleNoHungerFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4, 40, 40, 21, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    // Lead Boots - jumping and swimming are bad, sprinting is hard
    public static final FoodBalance BattleLeadBootsFoodBalance = new FoodBalance(
            20, 4, 4, 0, 4, 40, 40, 4, 0, 0,
            4, 1, 2.5,  0.025, 0.4, 0.1, 0.04, 0.04, 0.2, 0.5, 0.004);
    public static final FoodBalance BattleAlwaysHungryFoodBalance = new FoodBalance(
            20, 4, 4, 0, 1.5, 40, 40, 4, 0, 0,
            4, 0.2, 0.8, 0.025, 0.3, 0.1, 0.04, 0.04, 0.085, 0.03, 0.004);

    public static final FoodBalance[] BattleFoodBalances = new FoodBalance[]{
            BattleStandardFoodBalance,
            BattleFastHungerFoodBalance,
            BattleFastHealingFoodBalance,
            BattleNoStarvingFoodBalance,
            BattleAlwaysHealingFoodBalance,
            BattleNoHungerFoodBalance,
            BattleLeadBootsFoodBalance,
            BattleAlwaysHungryFoodBalance
    };
        // ChestRefill
    public static final int BattleCentralRefillChance = 1;
    public static final int BattleOuterRefillChance = 1;
	public static final int BattleHVRefillChance = 1;
	public static final int BattleNumberOfChestsToRefill = 4;
	public static final int BattleMinPlayerDistanceToEffectChance = 16;
	public static final int BattleMaxPlayerDistanceToEffectChance = 32;
	public static final String BattleCentralLootGroup = "UpdateItems";
	public static final String BattleOuterLootGroup = "OuterItems";
	public static final String BattleHVLootGroup = "HVItems";
        // ItemBalance
    // Potion of Fire Resistance - 20 seconds
    // Splash Potion of Weakness - 10 seconds
    // Potion of Strength - 10 seconds
    // Splash Potion of Slowness - 10 seconds
    // Potion of Regeneration - 15 seconds
    // Potion of Swiftness - 20 seconds
    // Potion of Leaping II - 20 seconds
    // Potion of Invisibility - 20 Seconds
    // Splash Potion of Poison - 10 seconds
    // Splash Potion of Poison II - 10 seconds
    // Splash Potion increase range - <BaseDamage>1.5</BaseDamage> <!-- Adjust potion range -->

    // Need done is LootSets. But that is code for the Battle Gamemode plugin




    // MG02 - Tumble
        // Host Options
    public static final int TumbleRoundLength = -1; // I assume this means it goes on forever
    public static final int TumbleMaxPlayer = 8;
    // Spectate modes: 0, 1, 2, 3, 4; with the default being 1. I need to check my WiiU with that the text values are and put it here.
    /*  Start Conditions
    <PlayersCondition op="GreaterThanOrEquals" value="2" activeOnly="false" readyOnly="true"/>
    start if 2 or more players are ready
        End Conditions
    <RoundTimeCondition value="300"/>
	<PlayersCondition op="LessThanOrEquals" value="1" activeOnly="true" readyOnly="false"/>
	End after 300 seconds or if 1 (or less) alive person is left
	    ShowdownConditions
	<RoundTimeCondition value="300" offset="-150"/>
	idk what offset means but the next line is my best take at it. Check the Battle Minigame's ShowdownConditions because it has an example with a comment
	Enter a 300 second showdown after 150 seconds of being in game
    */
    public static final boolean TumbleLockedInventory = true;
    public static final boolean TumbleNaturalRegeneration = false;
    public static final boolean TumbleAllowPvP = false;
    public static final Difficulty TumbleDifficultyOverride = Difficulty.HARD;
    public static final GameMode TumblePlayerGameMode = GameMode.SURVIVAL;
        // Player Abilities
    public static final boolean TumblePlayerAbilityInstabuild = true;
    public static final boolean TumblePlayerAbilityInvulnerable = true;

    public static final int TumbleLivesPerRound = 1;
    public static final int TumbleTeamCount = 16;
    // TumbleReamBasedSpawn = false
    // TumbleMaxTeamSize = 2
    public static final int TumbleMaxPlayersForSmallMaps = 6;
    // TumbleSpectateMode = $SpectateMode
    public static final boolean TumbleCanTntDestroyBlocks = true;
    public static final Material[] TumbleDestroyPermissionWhitelist = new Material[]{};
    public static final Material[] TumblePlacePermissionWhitelist = new Material[]{
            Material.TNT
    };
    public static final Material[] TumbleUsePermissionBlacklist = new Material[]{
            Material.WOODEN_HOE,
            Material.STONE_HOE,
            Material.IRON_HOE,
            Material.DIAMOND_HOE,
            Material.GOLDEN_HOE,
            Material.SKELETON_SKULL, // Skulls in general (id: 397); but why are they here?!

            Material.IRON_SHOVEL,
            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.GOLDEN_SHOVEL,
    }; // Dillion: this blacklist is to prevent players from hoeing the ground and doing something with a skull?
    public static final Material[] TumbleBlockUsePermissionWhitelist = new Material[]{
            Material.OAK_DOOR,
            Material.IRON_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,

            Material.IRON_TRAPDOOR,
            Material.OAK_TRAPDOOR, // it says "Wooden Trapdoor" but we need to be specific in plugins
            Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.ACACIA_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,

            Material.STONE_BUTTON,
            Material.OAK_BUTTON, // it says "Woodean Button" I assume its suppost to say wooden and I again need to type them all out
            Material.SPRUCE_BUTTON,
            Material.BIRCH_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON,
            Material.DARK_OAK_BUTTON,

            Material.LEVER,

            Material.CHEST,
            Material.ENDER_CHEST,
            Material.TRAPPED_CHEST,
    };
    public static final boolean TumbleBlockTickingOverride = true;
    public static final Material[] TumbleBlockTickingSpecificOverride = new Material[]{
            Material.SHORT_GRASS, // Grass
            Material.OAK_SAPLING,
            Material.LAVA, // the file says flowing lava but this is the closest
            Material.OAK_LEAVES, // it says "leaves" but we need to be specific in plugins
            Material.SPRUCE_LEAVES,
            Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES,
            Material.ACACIA_LEAVES,
            Material.DARK_OAK_LEAVES,
            Material.TALL_GRASS,
            Material.DEAD_BUSH,
            Material.DANDELION, // Yellow Flower
            Material.POPPY, // Red Flower
            Material.BROWN_MUSHROOM, // im not sure if these mushrooms are suppost to be their plant form or block form
            Material.RED_MUSHROOM,
            Material.WHEAT, // should be the plant and not the item
            Material.FARMLAND,
            Material.CACTUS,
            Material.LILY_PAD, // Water Lily
            Material.NETHER_WART, // again idk if it is suppost to be the plant form or a block form
            Material.CARROTS, // should be plant and not the item again
            // Leaves 2, which is apparently acaia and dark oak??
            Material.SUNFLOWER, // double plant, idk if just the sunflower but im including them all here. it also includes tall grass but i put that earlier
            Material.LILAC,
            Material.ROSE_BUSH,
            Material.PEONY,
            Material.ICE,
    };
        // Timers - measured in seconds
    public static final int TumbleRoundPreparingTimer = 5; // Time spent preparing before we start checking if the start conditions are satisfied.
    public static final int TumbleRoundStartDelayTimer = 15; // Start conditions satisfied, warning countdown before players are set loose.
    public static final int TumbleRoundStartDelayTimerAnnounceEvery = 1;
    // next two (Grace Period) probably will go unused
    public static final int TumbleGracePeriodTimer = 0; // Timer that ends the grace period and makes the players mortal.
    public static final int TumbleGracePeriodTimerAnnounceEvery = 1;
    public static final int TumbleRoundEndDelayTimer = 9; // Triggered by the EndConditions being satified, and when finished starts respawns the players in the lobby.
    public static final int TumbleRoundEndDelayTimerAnnounceEvery = 1;
    public static final int TumbleRoundLimitChangeAnnounceWhenRemaining = 60; // Triggered on round start: timer force ends the game.
    public static final int TumbleRoundLimitTimerChangedAnnounce = 1;
        // Sounds
    public static final int TumbleStartTimerForLast = 5;
    public static final int TumbleGraceTimerForLast = 3;
    public static final int TumbleRoundLimitTimerForLast = 30;
    /*  Item Balances
    Splash Potion & Lingering Potion, it says "<BaseDamage>1.5</BaseDamage>" but the comment says "<!-- Adjust potion range -->"
        Loot Sets - each are individual and are kits given based on game mode type thing
    Iron Shovel with Efficiency 5
    Snowball
    Firework Rocket, Small Yellow Ball "<FireworkData DyeColorAux="11" Type="0" />"
    Levitate Splash Potions, potion effect id is 0, but custom mob effect is "Levi". So Levitation 3 for 75 seconds
    */


    // MG03 - Glide
        // Host Options
    public static final boolean GlidePointsActive = false; // suppost to be either 0 or 1 but this is cleaner
    public static final int GlideMaxPlayers = 16; // either 8 or 16; default is 16

    /*  Start Conditions
    idk how to put it but it is <PlayersCondition op="GreaterThanOrEquals" value="2" activeOnly="false" readyOnly="true"/>
    so if there are 2 or more ready players it'll start (starting with the countdown first I assume)

        End Conditions
    <!-- Because of useRatio=true and value=1 this will end the game if all players are in the target area-->
	<PlayersEnteredVolumeCondition op="GreaterThanOrEquals" value="1" useTargetAreas="true" useRatio="true"/>
	<!--Note that this showdown time is reduced by 60 seconds every time another player crosses the finish line, to a minimum of 30 seconds remaining-->
	<ShowdownTimeCondition value="180" />
	So if everyone is done, or after the ShowdownTimer goes to 0, which starts at 180

        Showdown Conditions
    <PlayersEnteredVolumeCondition op="GreaterThanOrEquals" value="1" useTargetAreas="true" useRatio="false"/>
	<RoundTimeCondition value="1500" /> <!-- in seconds -->
	So to enter showdown either one person makes it or 25 minutes have passed
    */

    public static final boolean GlideLockedInventory = true;
    public static final boolean GlideNaturalRegeneration = false;
    public static final boolean GlideAllowPvP = false;
    public static final Difficulty GlideDifficultyOverride = Difficulty.HARD;
    public static final GameMode GlidePlayerGameMode = GameMode.ADVENTURE;
    public static final int GlideLivesPerRound = 0; // 0 means infinite
    public static final int GlideRoundCount = 3;
    public static final int GlideTeamCount = 16;
    // GlideReamBasedSpawn = false
    // GlideMaxTeamSize = 2
    // GlideCheckpointBasedSpawn = true
    public static final int GlideMaxPlayersForSmallMaps = 6;
    // GlideSpectateMode = $SpectateMode
    public static final Material[] GlideDestroyPermissionWhitelist = new Material[]{};
    public static final Material[] GlidePlacePermissionWhitelist = new Material[]{};
    public static final Material[] GlideUsePermissionWhitelist = new Material[]{};
    public static final Material[] GlideBlockUsePermissionWhitelist = new Material[]{};
    public static final boolean GlideBlockTickingOverride = false;
        // Timers - measured in seconds
    public static final int GlideRoundPreparingTimer = 5; // Time spent preparing before we start checking if the start conditions are satisfied.
    public static final int GlideRoundStartDelayTimer = 15; // Start conditions satisfied, warning countdown before players are set loose.
    public static final int GlideRoundStartDelayTimerAnnounceEvery = 1;
    // next two (Grace Period) probably will go unused
    public static final int GlideGracePeriodTimer = 0; // Timer that ends the grace period and makes the players mortal.
    public static final int GlideGracePeriodTimerAnnounceEvery = 1;
    public static final int GlideRoundEndDelayTimer = 10; // Triggered by the EndConditions being satified, and when finished starts respawns the players in the lobby.
    public static final int GlideRoundEndDelayTimerAnnounceEvery = 1;
    public static final int GlideRoundLimitChangeAnnounceWhenRemaining = 60; // Triggered on round start: timer force ends the game.
    public static final int GlideRoundLimitTimerChangedAnnounce = 1;
    public static final int GlideRoundTimeToReachEnd = 180; // Note that this showdown time is reduced by 60 seconds every time another player crosses the finish line, to a minimum of 30 seconds remaining
    public static final int GlideRoundTimeToReachEndAnnounceEvery = 1;
        // Sounds
    public static final int GlideStartTimerForLast = 5;
    public static final int GlideGraceTimerForLast = 3;
    public static final int GlideRoundLimitTimerForLast = 30;
    // there is a loot table in there but it is just: elytra on chest, nothing in hand
}
