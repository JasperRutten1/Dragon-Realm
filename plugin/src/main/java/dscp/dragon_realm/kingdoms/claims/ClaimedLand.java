package dscp.dragon_realm.kingdoms.claims;

import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClaimedLand implements Serializable {
    private static final long serialVersionUID = -1376370152421386532L;

    private List<ChunkCoordinates> chunkList;

    public ClaimedLand() throws KingdomException {
        chunkList = new ArrayList<>();
    }

    public List<ChunkCoordinates> getChunkList() {
        return chunkList;
    }

    public boolean containsChunk(Chunk chunk){
        for(ChunkCoordinates cc : chunkList){
            if(chunk.equals(chunk.getWorld().getChunkAt((int) cc.getX(),(int) cc.getZ()))) return true;
        }
        return false;
    }
    public static boolean containsChunk(ClaimedLand claim, Chunk chunk){
        return claim.containsChunk(chunk);
    }

    public void addChunk(Chunk chunk) throws KingdomException {
        if(chunk == null) throw new KingdomException("chunk can't be null");
        if(this.containsChunk(chunk)) throw new KingdomException("this chunk is already claimed by this kingdom");
        chunkList.add(new ChunkCoordinates(chunk.getX(), chunk.getZ()));
    }
    public void addChunk(Location location) throws KingdomException {
        if(location == null) throw new KingdomException("position 1 can't be null");
        addChunk(location.getChunk());
    }
    public void addChunk(Player player) throws KingdomException {
        if(player == null) throw new KingdomException("player can't be null");
        addChunk(player.getLocation());
    }

    public void removeChunk(Chunk chunk) throws KingdomException {
        if(chunk == null) throw new KingdomException("chunk can't be null");
        if(!this.containsChunk(chunk)) throw new KingdomException("this chunk is not claimed by this kingdom");
        this.chunkList.remove(new ChunkCoordinates(chunk.getX(), chunk.getZ()));
    }
    public void removeChunk(Location location) throws KingdomException {
        if(location == null) throw new KingdomException("location can't be null");
        this.removeChunk(location.getChunk());
    }
    public void removeChunk(Player player) throws KingdomException {
        if(player == null) throw new KingdomException("player can't be null");
        this.removeChunk(player.getLocation());
    }

    public double size(){
        return chunkList.size() * 16 * 16;
    }
    public static double size(ClaimedLand claimedLand){
        return claimedLand.size();
    }
}
