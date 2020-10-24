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
import java.util.Map;

/**
 * the class used for the claimed land of a kingdom
 */
public class KingdomClaim implements Serializable {
    private static final long serialVersionUID = -4244201835422078128L;

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

    //getters

    public Map<ChunkCoordinates, Chunk> getClaimedChunks() {
        return claimedChunks;
    }

    public KingdomClaim claimChunk(Player player) throws KingdomException {
        if(player == null) throw new IllegalArgumentException("player can't be null");
        World world = player.getLocation().getWorld();
        assert world != null;
        if(world.getEnvironment() != World.Environment.NORMAL) throw new KingdomException("a kingdom can only claim land in the overworld");

        Chunk chunk = world.getChunkAt(player.getLocation());

        ChunkCoordinates coordinates = new ChunkCoordinates(chunk.getX(), chunk.getZ());
        if(claimedChunks.containsKey(coordinates)){
            player.sendMessage(ChatColor.GRAY + "this chunk is already claimed by your kingdom");
            return this;
        }
        else if(chunkHasBeenClaimed(coordinates)){
            player.sendMessage(ChatColor.RED + "this chunk has already been claimed by another kingdom");
            return this;
        }

        this.getClaimedChunks().put(coordinates, chunk);
        return this;
    }

    public static boolean chunkHasBeenClaimed(ChunkCoordinates chunkCoordinates){
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getClaim().getClaimedChunks().containsKey(chunkCoordinates)) return true;
        }
        return false;
    }

    public static Kingdom getKingdomFromChunk(ChunkCoordinates chunkCoordinates){
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getClaim().getClaimedChunks().containsKey(chunkCoordinates)) return kingdom;
        }
        return null;
    }


}
