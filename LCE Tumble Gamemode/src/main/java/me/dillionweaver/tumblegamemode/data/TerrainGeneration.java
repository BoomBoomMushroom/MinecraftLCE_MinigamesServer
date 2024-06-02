package me.dillionweaver.tumblegamemode.data;

import me.dillionweaver.tumblegamemode.TumbleGamemode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerrainGeneration {
    private final TumbleGamemode main;
    public TerrainGeneration(TumbleGamemode main){
        this.main = main;
    }

    public void wipeMapAndFillWithBlock(Material fillType){
        int maxRadius = TumbleConstants.maxRadius + 2; // add a margin to catch any stray blocks!
        int[] start = new int[]{-maxRadius, TumbleConstants.fillCornerB[1], -maxRadius} ;
        int[] end = new int[]{maxRadius, TumbleConstants.fillCornerA[1], maxRadius};

        for(int x = start[0]; x < end[0]; x++){
            for(int y = start[1]; y < end[1]; y++){
                for(int z = start[2]; z < end[2]; z++){
                    Location currentLoc = new Location(main.tumbleWorld, x, y, z);
                    Block block = currentLoc.getBlock();
                    if(block.getType() != fillType) {
                        block.setType(fillType);
                    }
                }
            }
        }
    }

    public List<Block> generateLayer(List<Material> layerMaterials, int startYLevel, boolean randomLayer){
        if(randomLayer){
            layerMaterials = main.layers.getSafeMaterialList();
        }
        List<Block> blocksPlaced = new ArrayList<>();

        int yOffset = 0;

        boolean isUpLayer = main.random.nextBoolean();

        for(int radius=TumbleConstants.minRadius; radius<=TumbleConstants.maxRadius; radius++){
            int closerRadius = findClosestInt(radius, TumbleConstants.minRadius, TumbleConstants.maxRadius);
            int yIncrement = closerRadius - radius;
            int sign = (int)Math.signum(yIncrement);
            if(isUpLayer){
                if(yIncrement % 3 == 0){
                    yOffset -= sign;
                }
            }
            else{
                if(yIncrement % 2 == 0){
                    yOffset += sign;
                }
            }


            for(int deg=0;deg<360;deg++){
                double rad = Math.toRadians(deg);
                double x = Math.cos(rad) * radius;
                double z = Math.sin(rad) * radius;
                double y = startYLevel+yOffset;
                Location blockLocation = new Location(main.tumbleWorld, x, y, z);

                Material blockMaterial = layerMaterials.get( main.random.nextInt(layerMaterials.size()) );
                Block newBlock = blockLocation.getBlock();
                newBlock.setType(blockMaterial);

                blocksPlaced.add(newBlock);
            }
        }

        // end of func
        return blocksPlaced;
    }


    public static int findClosestInt(int target, int low, int high) {
        if (low > high) {
            throw new IllegalArgumentException("Low cannot be greater than high");
        }

        int diffLow = target - low;
        int diffHigh = high - target;

        return diffLow < diffHigh ? low : high;
    }
}
