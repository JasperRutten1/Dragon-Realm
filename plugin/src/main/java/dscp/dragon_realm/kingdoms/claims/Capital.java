package dscp.dragon_realm.kingdoms.claims;

import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Capital implements Serializable {
    private static final long serialVersionUID = -1507049068608743648L;

    public static double powerGeneration = 50;
    public static double increaseMaxPower = 3000;

    private ChunkCoordinates centerChunk;
    private List<ChunkCoordinates> chunkList;

    public Capital(Chunk centerChunk) throws KingdomException {
        if(centerChunk == null) throw new KingdomException("center chunk can't be null");
        if(!centerChunk.getWorld().getEnvironment().equals(World.Environment.NORMAL)) throw new KingdomException("can only put capital in overworld");
        this.centerChunk = new ChunkCoordinates(centerChunk.getX(), centerChunk.getZ());
        this.chunkList = new ArrayList<>();
        for(double x = centerChunk.getX() - 2 ; x <= centerChunk.getX() + 2 ; x++){
            for(double z = centerChunk.getZ() - 2 ; z <= centerChunk.getZ() + 2 ; z++){
                this.chunkList.add(new ChunkCoordinates(x, z));
            }
        }
    }

    public ChunkCoordinates getCenterChunk() {
        return centerChunk;
    }

    public List<ChunkCoordinates> getChunkList() {
        return chunkList;
    }
}
