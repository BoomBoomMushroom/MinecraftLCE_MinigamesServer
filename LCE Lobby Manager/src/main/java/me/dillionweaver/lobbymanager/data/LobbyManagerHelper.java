package me.dillionweaver.lobbymanager.data;


import com.sk89q.worldedit.regions.Region;

public class LobbyManagerHelper {
    public static boolean bracketListIncludes(Object[] data, Object contains){
        for(Object d1 : data){
            if(d1 == contains){ return true; }
        }
        return false;
    }

    public static boolean coordinateInsideBoundingBox(double[] coordinate, double[] corner1, double[] corner2){
        if(corner1.length != corner2.length && corner1.length != coordinate.length){
            throw new Error("Sizes of coordinates are different!");
        }

        double[] min = new double[]{
                Math.min(corner1[0], corner2[0]),
                Math.min(corner1[1], corner2[1]),
                Math.min(corner1[2], corner2[2])
        };
        double[] max = new double[]{
                Math.max(corner1[0], corner2[0]),
                Math.max(corner1[1], corner2[1]),
                Math.max(corner1[2], corner2[2])
        };

        boolean isWithin = ( coordinate[0] >= min[0] && coordinate[0] <= max[0] &&
                            coordinate[1] >= min[1] && coordinate[1] <= max[1] &&
                            coordinate[2] >= min[2] && coordinate[2] <= max[2] );

        return isWithin;
    }
}
