package me.dillionweaver.elytragamemode.data;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class GlideConstants {
    // start checkpoint index and ID : * = updated start point & ^ = needs testing start point
    public static final int[] lobby = new int[] { -340, 58, -341, 0, 0, 0 };
    public static final int[] mapBody = new int[] { 0, 1 }; // *
    public static final int[] mapMobs = new int[] { 2325, 253, 1295, 0, 0, 2};
    public static final int[] mapShrunk = new int[] { 3230, 244, 1072, 0, 0, 3 };
    public static final int[] mapCelts = new int[] { 999, 246, 1837, 0, 0, 4 };
    public static final int[] mapExcalibur = new int[] { 2219, 232, 2055, 0, 0, 5 };
    public static final int[] mapIcarus = new int[] { 3041, 199, 1786, 0, 0, 6 };
    public static final int[] mapDragon = new int[] { 59, 7 }; // * ^
    public static final int[] mapKraken = new int[] { 1647, 142, 337, 0, 0, 8};
    public static final int[] mapYeti = new int[] { 3116, 235, -167, 0, 0, 9 };
    public static final int[] mapCanyon = new int[] { 50, 10 }; // * ^
    public static final int[] mapCavern = new int[] { 13, 11 }; // *
    public static final int[] mapTemple = new int[] { 41, 12 }; // * ^
    public static final String[] idToName = new String[] { "lobby", "body", "mobs", "shrunk", "celts", "excalibur", "icarus", "dragon", "kraken", "yeti", "canyon", "cavern", "temple" };
    public static final int[] votableMapIds = new int[]{
            1,  // body
            7,  // dragon
            10, // canyon
            11, // cavern
            12, // temple
    };
    public static final int[][] idToCoordinates = new int[][] { lobby, mapBody, mapMobs, mapShrunk, mapCelts, mapExcalibur, mapIcarus, mapDragon, mapKraken, mapYeti, mapCanyon, mapCavern, mapTemple };

    // checkpoints : corner 1 (x, y, z), corner 2 (x, y, z), respawn position (x, y z), respawn rotation (v, v1), checkpoint "ID" (id), index
    // ID is just for chat message (Player1 reached checkpoint ID in X seconds!)
    public static final int[][] checkpoints = new int[][] {
            new int[] { 1297, 217, 1187,  1297, 217, 1187,  1297, 217, 1187,  -180, 0, 0 }, // body         0
            new int[] { 1291, 131, 1000,  1316, 173, 1002, 1304, 155, 1011,  -180, 0, 1 }, // body
            new int[] { 1300, 135, 784,  1317, 146, 786,  1308, 144, 792,  -180, 0, 2 }, // body
            new int[] { 1246, 59, 779,  1234, 67, 781,  1239, 65, 776,  0, 0, 3 }, // body
            new int[] { 1283, 69, 974,  1299, 79, 976,  1291, 76, 988, -180,  0, 4}, // body
            new int[] { 1256, 107, 974,  1237, 112, 976,  1247, 113, 968,  0, 0, 5}, // body                5
            new int[] { 1245, 78, 1044,  1247, 88, 1057,  1225, 95, 1051,  -90, 0, 6}, // body
            new int[] { 1162, 115, 1175,  1151, 124, 1177,  1157, 123, 1166,  0, 0, 7}, // body
            new int[] { 1147, 95, 1282,  1149, 102, 1267,  1158, 101, 1274,  90, 0, 8}, // body
            new int[] { 1153, 37, 1351,  1165, 39, 1353,  1160, 40, 1358,  -180, 0, 9}, // body
            new int[] { 1192, 7, 1205,  1194, 15, 1228,  1187, 14, 1217,  -90, 0, 10}, // body              10
            new int[] { 1285, 23, 1141,  1287, 34, 1128,  1293, 33, 1133,  90, 0, 11}, // body
            new int[] { 1300, 78, 1055,  1323, 87, 1057,  1310, 90, 1065,  180, 0, 12}, // body

            new int[] { 2146, 228, 3065,  2146, 228, 3067,  2146, 228, 3066,  90, 0, 0 }, // cavern
            new int[] { 1992, 174, 3119,  1970, 201, 3121,  1979, 194, 3108,  0, 0, 1 }, // cavern
            new int[] { 2019, 149, 3354,  2021, 168, 3366,  2000, 163, 3360,  -90, 0, 2 }, // cavern        15
            new int[] { 1989, 141, 3229,  1991, 154, 3218,  2018, 151, 3231,  120, 0, 3 }, // cavern
            new int[] { 2084, 104, 3245,  2086, 140, 3265,  2013, 126, 3255, -9, 0, 4 }, // cavern
            new int[] { 2120, 74, 3176,  2122, 92, 3162,  2137, 86, 3169,  90, 0, 5 }, // cavern
            new int[] { 2014, 96, 3146,  2016, 109, 3133,  2023, 104, 3140,  90, 0, 6 }, // cavern
            new int[] { 2036, 81, 3001,  2038, 102, 3020,  2020, 99, 3003,  -40, 0, 7 }, // cavern          20
            new int[] { 2037, 81, 3019,  2015, 102, 3021,  2020, 99, 3003,  -40, 0, 7 }, // cavern
            new int[] { 2176, 108, 3064,  2178, 120, 3072,  2143, 118, 3068,  -90, 0, 8 }, // cavern
            new int[] { 2175, 96, 3069,  2177, 104, 3078,  2166, 102, 3072,  -90, 0, 8 }, // cavern
            new int[] { 2100, 94, 3112,  2073, 128, 3114,  2086, 112, 3092,  0, 0, 9 }, // cavern

            new int[] { -334, 60, -352 }, // lobby                                                          25
            new int[] { -334, 60, -346 }, // lobby
            new int[] { -334, 60, -343 }, // lobby
            new int[] { -334, 60, -340 }, // lobby
            new int[] { -334, 60, -334 }, // lobby
            new int[] { -328, 60, -334 }, // lobby                                                          30
            new int[] { -325, 60, -334 }, // lobby
            new int[] { -322, 60, -334 }, // lobby
            new int[] { -317, 60, -334 }, // lobby
            new int[] { -317, 60, -340 }, // lobby
            new int[] { -317, 60, -343 }, // lobby                                                          35
            new int[] { -317, 60, -346 }, // lobby
            new int[] { -317, 60, -352 }, // lobby
            new int[] { -322, 60, -352 }, // lobby
            new int[] { -325, 60, -352 }, // lobby
            new int[] { -328, 60, -352 }, // lobby                                                          40

            new int[] { 3138, 235, 2857,  3138, 235, 2857,  3138, 235, 2857, 0, 0, 0 }, // temple
            new int[] { 3167, 184, 3118,  3114, 218, 3120,  3138, 206, 3077, 0, 0, 1 }, // temple
            new int[] { 3160, 133, 3177,  3131, 144, 3178,  3150, 147, 3168, 0, 0, 2}, // temple
            new int[] { 3166, 148, 3343,  3168, 139, 3330,  3146, 148, 3328, -64, 0, 3 }, // temple
            new int[] { 3257, 144, 3271,  3259, 160, 3289,  3329, 162, 3283, -90, 0, 4}, // temple          45
            new int[] { 3366, 142, 3252,  3368, 170, 3308,  3317, 183, 3279, -90, 0, 5 }, // temple
            new int[] { 3312, 115, 3258,  3324, 121, 3260,  3318, 125, 3280, 180, 0, 6 }, // temple
            new int[] { 3309, 105, 3004,  3326, 115, 3006,  3318, 120, 3033, 180, 0, 7 }, // temple
            new int[] { 3341, 50, 3017,  3315, 72, 3019,  3333, 67, 3006,  0, 0, 8 }, // temple

            new int[] { 1201, 246, 2778,  1201, 246, 2780,  1201, 246, 2779, 0, 0, 0 }, // canyon           50
            new int[] { 1227, 190, 2970,  1196, 211, 2972,  1199, 213, 2950, -32, 0, 1}, // canyon
            new int[] { 1214, 172, 3237,  1184, 195, 3239,  1198, 197, 3221, 0, 0, 2 }, // canyon
            new int[] { 1310, 118, 3352,  1341, 129, 3354,  1320, 128,  3367, 180, 0, 3 }, // canyon
            new int[] { 1331, 105, 3229,  1353, 105, 3231,  1344, 127, 3212, 90, 0, 4 }, // canyon
            new int[] { 1225, 50, 3146,  1244, 57, 3148,  1229, 56, 3155, -147, 0, 5 }, // canyon           55
            new int[] { 1132, 97, 3060,  1134, 110, 3045,  1140, 106, 3052, 95, 0, 6 }, // canyon
            new int[] { 1172, 65, 3205,  1156, 53, 3207,  1168, 66, 3193, 0, 0, 7 }, // canyon
            new int[] { 1112, 54, 3263,  1114, 46, 3281,  1140, 55, 3278, 105, 0, 8 }, // canyon

            new int[] { 652, 232, 334,  654, 232, 334,  653, 232, 334, -90, 0, 0 }, // dragon
            new int[] { 878, 176, 259,  880, 178, 291,  856, 184, 297, -147, 0, 1 }, // dragon              60
            new int[] { 1035, 162, 267,  1051, 168, 269,  1031, 176, 290, -146, 0, 2 }, // dragon
            new int[] { 825, 90, 350,  827, 118, 308,  851, 128, 323, 72, 0, 3 }, // dragon
            new int[] { 741, 126, 328,  743, 136, 344,  729, 139, 338, -95, 0, 4 }, // dragon
            new int[] { 784, 102, 274,  797, 112, 276,  789, 113, 285, 180, 0, 5 }, // dragon
            new int[] { 785, 70, 44,  797, 77, 46,  787, 89, 69, -180, 0, 6 }, // dragon                    65
            new int[] { 796, 73, -115,  813, 75, -117,  800, 89, -90, -180, 0, 7 }, // dragon
            new int[] { 890, 54, 1,  841, 74, 0,  867, 71, -26, 0, 0, 8 }, // dragon
    };

    public static final int[][] checkpointsInMapFromId = new int[][]{
            new int[] {25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40}, // lobby spawn points
            new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, // body
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {59, 60, 61, 62, 63, 64, 65, 66, 67}, // dragon
            new int[] {},
            new int[] {},
            new int[] {50, 51, 52, 53, 54, 55, 56, 57, 58}, // canyon
            new int[] {13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24}, //cavern
            new int[] {41, 42, 43, 44, 45, 46, 47, 48, 49}, // temple
    };


    public static final int[][] speedBoostBoundingBoxes = new int[][]{
            new int[] { 1973, 172, 3168,  1969, 175, 3173 },    // cavern   0
            new int[] { 1964, 155, 3268,  1960, 159, 3275 },    // cavern
            new int[] { 2009, 149, 3358,  2016, 153, 3362 },    // cavern
            new int[] { 2058, 138, 3319,  2051, 142, 3315 },    // cavern
            new int[] { 2008, 140, 3226,  2001, 144, 3222 },    // cavern
            new int[] { 2021, 118, 3253,  2028, 122, 3257 },    // cavern   5
            new int[] { 2117, 74, 3171,  2106, 78, 3167 },      // cavern
            new int[] { 1969, 83, 3074,  1973, 87, 3068 },      // cavern
            new int[] { 1965, 79, 3040,  1969, 83, 3036 },      // cavern
            new int[] { 1996, 74, 2994,  2003, 78, 2998 },      // cavern
            new int[] { 2082, 87, 3047,  2088, 91, 3051 },      // cavern   10
            new int[] { 2073, 108, 3066,  2080, 112, 3070 },    // cavern
            new int[] { 2149, 108, 3066,  2156, 112, 3070 },    // cavern
            new int[] { 2185, 92, 3104,  2178, 96, 3100 },      // cavern
            new int[] { 2183, 86, 3121,  2176, 90, 3117 },      // cavern

            new int[] { 1214, 39, 965,  1221, 44, 969 },        // body     15
            new int[] { 1289, 66, 979,  1293, 71, 972 },        // body
            new int[] { 1253, 104, 887,  1249, 108, 894 },      // body
            new int[] { 1158, 55, 1108,  1154, 59, 1115 },      // body
            new int[] { 1145, 47, 1291,  1152, 51, 1295 },      // body
            new int[] { 1143, 47, 1259,  1150, 51, 1263 },      // body     20
            new int[] { 1149, 92, 1277,  1142, 96, 1273 },      // body
            new int[] { 1200, 5, 1215,  1207, 9, 1219 },        // body
            new int[] { 1300, 18, 1137,  1293, 22, 1133 },      // body

            new int[] { 3147, 159, 3142,  3151, 163, 3135 },    // temple
            new int[] { 3125, 134, 3132,  3121, 138, 3139 },    // temple   25
            new int[] { 3172, 134, 3128,  3168, 138, 3135 },    // temple
            new int[] { 3164, 138, 3298,  3157, 142, 3294 },    // temple
            new int[] { 3162, 135, 3335,  3169, 139, 3339 },    // temple
            new int[] { 3245, 143, 3278,  3252, 147, 3282 },    // temple
            new int[] { 3359, 119, 3291,  3352, 123, 3287 },    // temple   30
            new int[] { 3316, 114, 3266,  3320, 117, 3259 },    // temple
            new int[] { 3330, 38, 3046,  3326, 42, 3053 },      // temple
            new int[] { 3330, 25, 3171,  3326, 29, 3178 },      // temple

            new int[] { 1162, 169, 3085,  1158, 173, 3092 },    // canyon
            new int[] { 1162, 168, 3130,  1158, 172, 3137 },    // canyon   35
            new int[] { 1323, 118, 3392,  1327, 122, 3385 },    // canyon
            new int[] { 1336, 119, 3283,  1340, 123, 3276 },    // canyon
            new int[] { 1237, 47, 3140,  1241, 51, 3133 },      // canyon
            new int[] { 1186, 43, 3054,  1190, 47, 3047 },      // canyon
            new int[] { 1164, 47, 3221,  1160, 51, 3228 },      // canyon   40
            new int[] { 1125, 42, 3274,  1118, 46, 3270 },      // canyon

            new int[] { 875, 165, 276,  882, 169, 280 },        // dragon
            new int[] { 910, 165, 335,  917, 169, 339 },        // dragon
            new int[] { 936, 165, 324,  943, 169, 328 },        // dragon
            new int[] { 1025, 131, 283,  1021, 135, 290 },      // dragon   45
            new int[] { 962, 121, 329,  958, 125, 325 },        // dragon
            new int[] { 705, 95, 348,  709, 99, 341 },          // dragon
            new int[] { 694, 125, 338,  701, 129, 342 },        // dragon
            new int[] { 756, 70, 162,  760, 74, 155 },          // dragon
            new int[] { 817, 70, 167,  821, 74, 160 },          // dragon   50
            new int[] { 815, 69, 116,  819, 73, 109 },          // dragon
            new int[] { 756, 79, 42,  760, 85, 35 },            // dragon
            new int[] { 789, 68, 42,  793, 72, 35 },            // dragon
            new int[] { 780, 72, -36,  784, 76, -43 },          // dragon
            new int[] { 860, 46, -57,  856, 50, -50 },          // dragon   55
            new int[] { 871, 44, -5,  867, 48, 2 },             // dragon
    };

    public static final int[][] speedBoostsInMapFromId = new int[][]{
            new int[] {},
            new int[] {15, 16, 17, 18, 19, 20, 21, 22, 23}, // body
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56}, // dragon
            new int[] {},
            new int[] {},
            new int[] {34, 35, 36, 37, 38, 39, 40, 41}, // canyon
            new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}, //cavern
            new int[] {24 ,25, 26, 27, 28, 29, 30, 31, 32, 33}, // temple
    };

    public static final int[][] updraftBoundingBoxes = new int[][]{
            new int[] { 1998, 174, 3093,  1996, 180, 3095 },    // cavern   0
            new int[] { 1965, 173, 3201,  1963, 180, 3203 },    // cavern
            new int[] { 2034, 136, 3308,  2031, 143, 3305 },    // cavern
            new int[] { 2040, 124, 3240,  2038, 131, 3238 },    // cavern
            new int[] { 2040, 123, 3234,  2038, 130, 3232 },    // cavern
            new int[] { 2034, 124, 3234,  2032, 131, 3232 },    // cavern   5
            new int[] { 2034, 125, 3240,  2032, 133, 3238 },    // cavern
            new int[] { 1978, 137, 3214,  1978, 142, 3214 },    // cavern
            new int[] { 1962, 134, 3216,  1962, 139, 3216 },    // cavern
            new int[] { 1952, 128, 3227,  1952, 134, 3227 },    // cavern
            new int[] { 1953, 121, 3250,  1955, 127, 3252 },    // cavern   10
            new int[] { 2062, 74, 3172,  2060, 81, 3170 },      // cavern
            new int[] { 2062, 74, 3168,  2060, 81, 3166 },      // cavern
            new int[] { 2042, 77, 3168,  2040, 83, 3166 },      // cavern
            new int[] { 2039, 80, 3157,  2037, 87, 3155 },      // cavern
            new int[] { 1983, 94, 3139,  1982, 99, 3137 },      // cavern   15
            new int[] { 1979, 79, 3108,  1977, 84, 3106 },      // cavern
            new int[] { 1960, 83, 3072,  1962, 89, 3070 },      // cavern
            new int[] { 1963, 67, 3033,  1965, 76, 3031 },      // cavern
            new int[] { 1969, 67, 3033,  1971, 76, 3031 },      // cavern
            new int[] { 1969, 67, 3028,  1971, 76, 3026 },      // cavern   20
            new int[] { 1963, 67, 3028,  1965, 76, 3026 },      // cavern
            new int[] { 2031, 78, 3028,  2032, 85, 3028 },      // cavern
            new int[] { 2031, 78, 3031,  2032, 85, 3031 },      // cavern
            new int[] { 2030, 78, 3030,  2033, 85, 3029 },      // cavern
            new int[] { 2038, 86, 3053,  2040, 93, 3055 },      // cavern   25
            new int[] { 2054, 79, 3020,  2058, 84, 3022 },      // cavern
            new int[] { 2178, 93, 3071,  2181, 98, 3075 },      // cavern
            new int[] { 2210, 91, 3118,  2208, 98, 3116 },      // cavern
            new int[] { 2107, 93, 3083,  2105, 99, 3085 },      // cavern

            new int[] { 1307, 130, 842,  1309, 136, 840 },      // body     30
            new int[] { 1210, 44, 910,  1202, 37, 903 },        // body
            new int[] { 1189, 36, 938,  1186, 42, 941 },        // body
            new int[] { 1245, 38, 971,  1248, 46, 974 },        // body
            new int[] { 1253, 37, 959,  1256, 45, 962 },        // body
            new int[] { 1256, 39, 969,  1260, 52, 973 },        // body     35
            new int[] { 1262, 49, 978,  1264, 57, 980 },        // body
            new int[] { 1268, 60, 985,  1270, 67, 987 },        // body
            new int[] { 1276, 65, 994,  1278, 72, 992 },        // body
            new int[] { 1275, 64, 994,  1277, 70, 996 },        // body
            new int[] { 1275, 65, 997,  1273, 71, 999 },        // body     40
            new int[] { 1293, 89, 894,  1286, 102, 891 },       // body
            new int[] { 1212, 75, 1040,  1217, 83, 1045 },      // body
            new int[] { 1233, 88, 1049,  1238, 95, 1054 },      // body
            new int[] { 1264, 78, 1048,  1267, 85, 1051 },      // body
            new int[] { 1231, 57, 1097,  1229, 63, 1095 },      // body     45
            new int[] { 1215, 54, 1097,  1213, 60, 1095 },      // body
            new int[] { 1205, 53, 1097,  1203, 60, 1095 },      // body
            new int[] { 1194, 53, 1097,  1192, 59, 1095 },      // body
            new int[] { 1184, 53, 1097,  1182, 59, 1095 },      // body
            new int[] { 1174, 53, 1098,  1172, 59, 1096 },      // body     50
            new int[] { 1182, 44, 1125,  1140, 110, 1150 },     // body
            new int[] { 1189, 46, 1280,  1176, 80, 1270 },      // body
            new int[] { 1179, 85, 1257,  1175, 92, 1261 },      // body
            new int[] { 1166, 87, 1290,  1162, 93, 1286 },      // body
            new int[] { 1269, 10, 1207,  1271, 15, 1209 },      // body     55
            new int[] { 1288, 15, 1215,  1292, 21, 1218 },      // body
            new int[] { 1290, 14, 1195,  1292, 21, 1193 },      // body
            new int[] { 1265, 9, 1187,  1267, 16, 1185 },       // body
            new int[] { 1318, 22, 1203,  1320, 28, 1201 },      // body
            new int[] { 1309, 21, 1156,  1306, 28, 1153 },      // body     60
            new int[] { 1250, 24, 1118,  1248, 31, 1116 },      // body
            new int[] { 1297, 18, 1068,  1317, 65, 1084 },      // body

            new int[] { 3165, 189, 3028,  3163, 198, 3030 },    // temple
            new int[] { 3126, 135, 3193,  3170, 147, 3205 },    // temple
            new int[] { 3181, 150, 3247,  3179, 161, 3249 },    // temple   65
            new int[] { 3130, 152, 3234,  3128, 164, 3236 },    // temple
            new int[] { 3125, 157, 3256,  3123, 167, 3258 },    // temple
            new int[] { 3125, 157, 3256,  3123, 167, 3258 },    // temple
            new int[] { 3225, 136, 3336,  3227, 148, 3343 },    // temple
            new int[] { 3235, 138, 3336,  3237, 147, 3334 },    // temple   70
            new int[] { 3239, 138, 3338,  3241, 147, 3340 },    // temple
            new int[] { 3236, 143, 3325,  3243, 157, 3323 },    // temple
            new int[] { 3171, 155, 3339,  3173, 160, 3341 },    // temple
            new int[] { 3278, 141, 3286,  3280, 153, 3288 },    // temple
            new int[] { 3278, 141, 3272,  3280, 155, 3274 },    // temple   75
            new int[] { 3317, 145, 3279,  3319, 156, 3281 },    // temple
            new int[] { 3296, 163, 3314,  3294, 150, 3312 },    // temple
            new int[] { 3361, 152, 3259,  3359, 141, 3257 },    // temple
            new int[] { 3361, 153, 3303,  3359, 140, 3301 },    // temple
            new int[] { 3307, 105, 3189,  3309, 117, 3187 },    // temple   80
            new int[] { 3330, 117, 3143,  3332, 127, 3141 },    // temple
            new int[] { 3306, 104, 3134,  3308, 115, 3132 },    // temple
            new int[] { 3320, 93, 3094,  3322, 104, 3092 },     // temple
            new int[] { 3322, 98, 3044,  3320, 84, 3046 },      // temple

            new int[] { 1213, 182, 3082,  1215, 167, 3080 },    // canyon   85
            new int[] { 1309, 116, 3368,  1308, 111, 3369 },    // canyon
            new int[] { 1316, 109, 3336,  1320, 118, 3332 },    // canyon
            new int[] { 1210, 46, 3175,  1207, 35, 3178 },      // canyon
            new int[] { 1177, 91, 2982,  1179, 80, 2984 },      // canyon
            new int[] { 1155, 91, 3046,  1158, 80, 3043 },      // canyon   90
            new int[] { 1078, 90, 3039,  1080, 80, 3037 },      // canyon

            new int[] { 910, 165, 295,  912, 183, 297 },        // dragon
            new int[] { 919, 104, 308,  917, 123, 306 },        // dragon
            new int[] { 772, 99, 367,  774, 79, 374 },          // dragon
            new int[] { 760, 102, 379,  762, 91, 381 },         // dragon   95
            new int[] { 711, 121, 309,  709, 99, 311 },         // dragon
            new int[] { 703, 99, 311,  705, 121, 309 },         // dragon
            new int[] { 667, 133, 322,  669, 115, 320 },        // dragon
            new int[] { 673, 133, 322,  675, 115, 320 },        // dragon
            new int[] { 794, 66, 7,  796, 83, 5 },              // dragon   100
            new int[] { 874, 71, -75,  876, 56, -77 },          // dragon
            new int[] { 849, 55, 110,  851, 39, 108 },          // dragon
            new int[] { 876, 56, 112,  878, 38, 110 },          // dragon
    };

    public static final int[][] updraftsInMapFromId = new int[][]{
            new int[] {},
            new int[] {30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62}, // body
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {92, 93, 94, 95, 96, 97, 98, 99, 100 ,101, 102, 103}, // dragon
            new int[] {},
            new int[] {},
            new int[] {85, 86, 87, 88, 89, 90, 91}, // canyon
            new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29}, //cavern
            new int[] {63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84}, // temple
    };

    public static final int[][] finishLineBoundingBoxes = new int[][]{
            new int[] { 2093, 26, 3289,  2079, 45, 3292 },      // cavern   0
            new int[] { 1295, 19, 890,  1325, 49, 895 },        // body     1
            new int[] { 3342, 8, 3375,  3328, 19, 3379 },       // temple   2
            new int[] { 1082, 103, 3451,  1115, 14, 3445 },     // canyon   3
            new int[] { 894, 26, 211,  847, 75, 217 },          // dragon   4
    };

    public static final int[][] finishLinesInMapFromId = new int[][]{
            new int[] {},
            new int[] {1}, // body
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {},
            new int[] {4}, // dragon
            new int[] {},
            new int[] {},
            new int[] {3}, // canyon
            new int[] {0}, // cavern
            new int[] {2}, // temple
    };

    public static final long[] mapIdToAverageTimeToCompleteMS = new long[]{
            0,
            (2*60 + 30) * 1000,     // body ~ 2min 30sec
            0,
            0,
            0,
            0,
            0,
            (1*60 + 20) * 1000,     // dragon ~ 1min 20sec  needs testing
            0,
            0,
            (1*60 + 5) * 1000,      // canyon ~ needs testing
            (1*60 + 5) * 1000,      // cavern ~ 1min 5sec
            (1*60 + 5) * 1000,      // temple ~ needs testing
    };

    public static final String[][] mapIdToMusicName = new String[][]{
            {"minecraft:glide.music"}, // lobby
            {"minecraft:glide.music.pack2_body"}, // body
            {"minecraft:glide.music"}, // mobs
            {"minecraft:glide.music.pack2_shrunk"}, // shrunk
            {"minecraft:glide.music.pack3_celts"}, // celts
            {"minecraft:glide.music.pack3_excalibur"}, // excalibur
            {"minecraft:glide.music.pack3_icarus"}, // icarus
            {"minecraft:glide.music"}, // dragon
            {"minecraft:glide.music"}, // kraken
            {"minecraft:glide.music"}, // yeti
            {"minecraft:glide.music.canyon", "minecraft:glide.music"}, // canyon
            {"minecraft:glide.music"}, // cavern
            {"minecraft:glide.music"}, // temple
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

    public static final String pluginMessagePrefix = "[Glide] ";
    public static final String votingMenuTitle = "Vote for a map";
    public static final String endResultTitle = "Results";

    public static final HashMap<String, String> rankToColorCode = new HashMap<>();
    static {
        rankToColorCode.put("S", "&6");
        rankToColorCode.put("A", "&e");
        rankToColorCode.put("B", "&9");
        rankToColorCode.put("C", "&7");
        rankToColorCode.put("D", "&8");
        rankToColorCode.put("E", "&c");
        rankToColorCode.put("F", "&4");
    };

    public static final boolean stopPlayerFromInteractingWithWhateverTheyWant = true;

    public static final String saveDataFilename = "GlideSaveData.yml";
    public static final String elytraWorldKeyName = "GlideWorld";

    public static final Vector putLobbyPosition = new Vector(-449, 4, -288);
}
