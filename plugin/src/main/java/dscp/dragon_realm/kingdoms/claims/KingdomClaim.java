package dscp.dragon_realm.kingdoms.claims;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
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

    /**
     * constructor for KingdomClaim class
     * @param kingdom
     * the kingdom that this claim belongs to
     */
    public KingdomClaim(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");

        this.kingdom = kingdom;
        this.claimedChunks = new HashMap<>();
    }

    //getters

    public Map<Integer, List<Integer>> getClaimedChunks() {
        return claimedChunks;
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
     * @param world the world where the chunk is in
     * @return the chunk object with coords x and z
     */
    public static Chunk getChunkObject(int x, int z, World world){
        return world.getChunkAt(x, z);
    }
}
