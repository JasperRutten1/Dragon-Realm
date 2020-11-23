package dscp.dragon_realm.kingdoms.claims;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the class used for the claimed land of a kingdom
 */
public class KingdomClaim implements Serializable {
    private static final long serialVersionUID = -4244201835422078128L;

    private Kingdom kingdom;
    private Map<Integer, List<Integer>> claimedChunks;
    public static World WORLD = Bukkit.getWorld("world");
    private List<Settlement> settlements;

    /**
     * constructor for KingdomClaim class
     * @param kingdom
     * the kingdom that this claim belongs to
     */
    public KingdomClaim(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");

        this.kingdom = kingdom;
        this.claimedChunks = new HashMap<>();
        this.settlements = new ArrayList<>();
    }

    //getters

    public Map<Integer, List<Integer>> getClaimedChunks() {
        return claimedChunks;
    }

    public List<Settlement> getSettlements() {
        return settlements;
    }

    // claim chunks and check if claimed

    /**
     * save the chunk to this kingdom
     * @param x the x coordinate of the chunk
     * @param z the z coordinate of the chunk
     * @return this object
     */
    public boolean claimChunk(int x, int z){
        if(isClaimed(x, z)) return false;
        if(claimedChunks.get(x) == null){
            claimedChunks.put(x, new ArrayList<>());
        }
        claimedChunks.get(x).add(z);
        return true;
    }

    public void removeClaim(Integer x, Integer z){
        if(!isClaimed(x, z)) return;
        claimedChunks.get(x).remove(z);
        if(claimedChunks.get(x).isEmpty()) claimedChunks.remove(x);
    }

    /**
     * check if a kingdom has claimed a chunk
     * @param x the x coordinate of the chunk
     * @param z the z coordinate of the chunk
     * @return true if already claimed, false if not
     */
    public static boolean isClaimed(int x, int z){
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getClaim().claimedChunks.containsKey(x) && kingdom.getClaim().claimedChunks.get(x).contains(z)) return true;
        }
        return false;
    }

    /**
     * get the kingdom that claimed a chunk
     * @param x the x coordinate of the chunk
     * @param z the z coordinate of the chunk
     * @return the kingdom that claimed the chunk, null if not claimed
     */
    public static Kingdom claimedBy(int x, int z){
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getClaim().claimedChunks.containsKey(x) && kingdom.getClaim().claimedChunks.get(x).contains(z)) return kingdom;
        }
        return null;
    }

    /**
     * get the chunk object of the chunk at x, z
     * @param x the x coordinate of the chunk
     * @param z the z coordinate of the chunk
     * @return the chunk object with coords x and z
     */
    public static Chunk getChunkObject(int x, int z){
        return WORLD.getChunkAt(x, z);
    }

    public static boolean hasBiome(Biome biome, Chunk chunk){
        for(int x = 0 ; x < 16 ; x++){
            for(int z = 0 ; z < 16 ; z++){
                Block block = chunk.getBlock(x, 63, z);
                if(block.getBiome().equals(biome)) return true;
            }
        }

        return false;
    }

    //map

    /**
     * generates a string showing the claimed chunks around a player
     * @param player the player that will be used as the center of the map
     * @return a string containing the chunk map
     */
    public static String generateClaimMap(Player player){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        Chunk playerChunk = player.getLocation().getChunk();
        int x = playerChunk.getX();
        int z = playerChunk.getZ();
        StringBuilder sb = new StringBuilder();

        sb.append("player chunk location: (").append(x).append(" , ").append(z).append(")\n");
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);

        for(int j = z + 9 ; j >= z - 9 ; j--){
            for(int i = x - 17 ; i <= x + 17; i++){
                Kingdom claimedBy = claimedBy(i, j);
                if (claimedBy == null) sb.append(ChatColor.GRAY);
                else if(claimedBy.equals(kingdom) && kingdom.getClaim().isCoveredBySettlement(i, j)) sb.append(ChatColor.GOLD);
                else if(claimedBy.equals(kingdom)) sb.append(ChatColor.GREEN);
                else sb.append(ChatColor.RED);
                sb.append("▇");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //check claim

    public boolean checkClaimedInRadius(int x, int z, int r){
        for(int i = x - r ; i <= x + r ; i++){
            if(claimedChunks.containsKey(i)){
                for(int j = z - r ; j <= z + r ; j++){
                    if(claimedChunks.get(i) == null || !claimedChunks.get(i).contains(j)) return false;
                }
            }
            else return false;
        }
        return true;
    }

    public boolean checkClaimedInRadius(Chunk chunk, int r){
        if(chunk == null) throw new IllegalArgumentException("chunk can't be null");
        return checkClaimedInRadius(chunk.getX(), chunk.getZ(), r);
    }

    public static ArrayList<Chunk> getChunksInRadius(int x, int z, int r){
        ArrayList<Chunk> chunks = new ArrayList<>();
        for(int i = x - r ; i <= x + r ; i++){
            for(int j = z - r ; j <= z + r ; j++){
                chunks.add(WORLD.getChunkAt(i, j));
            }
        }
        return chunks;
    }

    public ArrayList<Chunk> getChunksInRadius(Chunk chunk, int r){
        if(chunk == null) throw new IllegalArgumentException("chunk can't be null");
        return getChunksInRadius(chunk.getX(), chunk.getZ(), r);
    }

    public static boolean isOverworldChunk(Chunk chunk){
        return chunk.getWorld().equals(WORLD);
    }

    public int size(){
        int size = 0;
        for(Map.Entry<Integer, List<Integer>> entry : claimedChunks.entrySet()){
            size += entry.getValue().size();
        }
        return size;
    }

    public static int distance(Chunk a, Chunk b){
        return Math.max(Math.abs(a.getX() - b.getX()), Math.abs(a.getZ() - b.getZ()));
    }

    //settlements

    public KingdomClaim addSettlement(Settlement settlement) {
        if(settlement == null) throw new IllegalArgumentException("settlement can't be null");
        settlements.add(settlement);
        return this;
    }

    public Settlement removeSettlement(Settlement settlement) throws KingdomException {
        if(settlement == null) throw new IllegalArgumentException("settlement can't be null");
        if(!settlements.contains(settlement)) throw new KingdomException("this settlement is not part of this kingdom");
        settlements.remove(settlement);
        return settlement;
    }

    public boolean hasSettlementWithName(String name){
        for(Settlement settlement : settlements){
            if(settlement.getName().equals(name)) return true;
        }
        return false;
    }

    public Settlement getSettlement(Chunk chunk){
        for(Settlement settlement : settlements){
            if(settlement.isCovered(chunk)) return settlement;
        }
        return null;
    }

    public Settlement getSettlement(String name){
        for(Settlement settlement : settlements){
            if(settlement.getName().equals(Dragon_Realm_API.capitalizeFirstLetter(name))) return settlement;
        }
        return null;
    }

    public Settlement getCapital(){
        for(Settlement settlement : settlements){
            if(settlement.isCapital()) return settlement;
        }
        return null;
    }

    public boolean isCoveredBySettlement(int x, int z){
        for(Settlement settlement : settlements){
            if(settlement.isCovered(x, z)) return true;
        }
        return false;
    }

    public boolean isGovernor(KingdomMember member){
        for(Settlement settlement : settlements){
            if(member.equals(settlement.getGovernor())) return true;
        }
        return false;
    }
}
