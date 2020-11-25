package dscp.dragon_realm.dragonProtect;

import dscp.dragon_realm.Dragon_Realm_API;
import org.bukkit.Chunk;

import java.io.Serializable;
import java.util.*;

public class ProtectedZone implements Serializable {
    private static final long serialVersionUID = 2411526520982116874L;

    private Map<Integer, List<Integer>> protectedChunks;
    private String name;

    /**
     * protected zone constructor
     * @param name the name of the new zone
     */
    public ProtectedZone(String name){
        if(name == null) throw new IllegalArgumentException("name can't be null");
        this.name = Dragon_Realm_API.capitalizeFirstLetter(name);
        this.protectedChunks = new HashMap<>();
    }

    /**
     * getter for the chunk map
     * @return the chunk map
     */
    public Map<Integer, List<Integer>> getProtectedChunks() {
        return protectedChunks;
    }

    /**
     * getter for the name of the zone
     * @return the name of the zone
     */
    public String getName() {
        return name;
    }

    /**
     * check if a chunk is covered by this zone
     * @param chunk the chunk to check for
     * @return true if the chunk is covered by this zone, false if not
     */
    public boolean coversChunk(Chunk chunk){
        if(!protectedChunks.containsKey(chunk.getX()) || protectedChunks.get(chunk.getX()) == null) return false;
        else return protectedChunks.get(chunk.getX()).contains(chunk.getZ());
    }

    /**
     * add a chunk to the covered chunks
     * @param chunk the chunk to add
     * @return true if successful, false if not
     */
    public boolean addChunkToZone(Chunk chunk){
        if(coversChunk(chunk)) return false;
        Integer x = chunk.getX();
        Integer z = chunk.getZ();
        if(!protectedChunks.containsKey(x)) protectedChunks.put(x, new ArrayList<>());
        if(!protectedChunks.get(x).contains(z)) protectedChunks.get(x).add(z);
        return true;
    }

    /**
     * remove a chunk from the covered chunks
     * @param chunk the chunk to remove
     * @return true if successful, false if not
     */
    public boolean removeChunkFromZone(Chunk chunk){
        if(!coversChunk(chunk)) return false;
        Integer x = chunk.getX();
        Integer z = chunk.getZ();
        protectedChunks.get(x).remove(z);
        if(protectedChunks.get(x).isEmpty()) protectedChunks.remove(x);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedZone)) return false;
        ProtectedZone that = (ProtectedZone) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
