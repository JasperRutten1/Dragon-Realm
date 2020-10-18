package dscp.dragon_realm.kingdoms.claims;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * the class used for the claimed land of a kingdom
 */
public class KingdomClaim {
    private Kingdom kingdom;
    private Map<ChunkCoordinates, Chunk> claimedChunks;

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

    


}
