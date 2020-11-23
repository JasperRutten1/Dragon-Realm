package dscp.dragon_realm.kingdoms.claims.settlements.resources;

import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;

public class SettlementFarmLand {
    private static ArrayList<Material> farmMaterials = getFarmBlockMaterials();
    private static ArrayList<Material> ignoreMaterials = getIgnoreBlockMaterials();

    public static ArrayList<Block> getFarmlandBlocksInSettlement(Settlement settlement){
        Chunk centerChunk = settlement.getCenterChunk();
        ArrayList<Block> farmlandBlocks = new ArrayList<>();
        for(Chunk chunk : KingdomClaim.getChunksInRadius(centerChunk.getX(), centerChunk.getZ(), settlement.getLevel().getLevel())){
            farmlandBlocks.addAll(getFarmLandBlocksInChunk(chunk));
        }
        return farmlandBlocks;
    }

    public static ArrayList<Block> getFarmLandBlocksInChunk(Chunk chunk){
        System.out.println(System.nanoTime());
        ArrayList<Block> farmLandBlocks = new ArrayList<>();
        for(int x = 0 ; x < 16 ; x += 3){
            for(int z = 0 ; z < 16 ; z += 3){
                Block farmBlock = getFirstFarmLandBlock(chunk, x, z);
                if(farmBlock != null && !farmLandBlocks.contains(farmBlock)){
                    farmLandBlocks.addAll(getFarmlandBlocksAroundBlock(chunk, x, farmBlock.getY() ,z, null));
                }
            }
        }
        System.out.println(System.nanoTime());
        return farmLandBlocks;
    }


    private static ArrayList<Block> getFarmlandBlocksAroundBlock(Chunk chunk , int x, int y, int z, ArrayList<Block> blocks){
        if(blocks == null) blocks = new ArrayList<>();
        for(int xd = x - 1 ; xd < 16 && xd < x + 2 ; xd++){
            if(xd >= 0){
                for(int zd = z - 1 ; zd < 16 && zd < z + 2 ; zd++){
                    if(zd >= 0 && !(xd == x && zd == z)){
                        Block block = chunk.getBlock(xd, y, zd);
                        if(block.getType().equals(Material.FARMLAND) && !blocks.contains(block) && block.getLightLevel() == 15){
                            blocks.add(block);
                            getFarmlandBlocksAroundBlock(chunk ,xd, y, zd, blocks);
                        }
                    }
                }
            }
        }
        return blocks;
    }


    private static Block getFirstFarmLandBlock(Chunk chunk, int x, int z){
        for(int y = 255 ; y > 0 ; y--){
            Block block = chunk.getBlock(x, y, z);
            if(!block.getType().equals(Material.AIR)){
                if(ignoreMaterials.contains(block.getType()) || farmMaterials.contains(block.getType())){
                    if(farmMaterials.contains(block.getType()) && block.getRelative(0, -1 , 0).getType().equals(Material.FARMLAND))
                        return block.getRelative(0, -1, 0);
                }
                else if(block.getType().equals(Material.FARMLAND)) return block;
                else return null;
            }
        }
        return null;
    }

    private static ArrayList<Material> getFarmBlockMaterials(){
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(Material.WHEAT);
        materials.add(Material.POTATOES);
        materials.add(Material.CARROTS);
        materials.add((Material.BEETROOTS));
        return  materials;
    }

    private static ArrayList<Material> getIgnoreBlockMaterials(){
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(Material.GLASS);
        materials.add(Material.GRAY_STAINED_GLASS_PANE);
        materials.add(Material.GREEN_STAINED_GLASS);
        materials.add((Material.BLACK_STAINED_GLASS));
        materials.add((Material.WHITE_STAINED_GLASS));
        materials.add((Material.ORANGE_STAINED_GLASS));
        materials.add((Material.MAGENTA_STAINED_GLASS));
        materials.add((Material.LIGHT_BLUE_STAINED_GLASS));
        materials.add((Material.YELLOW_STAINED_GLASS));
        materials.add((Material.LIME_STAINED_GLASS));
        materials.add((Material.PINK_STAINED_GLASS));
        materials.add((Material.LIGHT_GRAY_STAINED_GLASS));
        materials.add((Material.CYAN_STAINED_GLASS));
        materials.add((Material.PURPLE_STAINED_GLASS));
        materials.add((Material.BLUE_STAINED_GLASS));
        materials.add((Material.BROWN_STAINED_GLASS));
        materials.add((Material.RED_STAINED_GLASS));
        return  materials;
    }

    private static Map<Material, Double> generateCropValueMap(){
        Map<Material, Double> cropValueMap = new HashMap<>();
        cropValueMap.put(Material.WHEAT, )
    }
}
