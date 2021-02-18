package dscp.dragon_realm.bounty;

import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.*;

public class Bounty implements Serializable {
    private static final long serialVersionUID = -6782037735624835768L;

    private UUID targetUUID;
    private HashMap<UUID, Integer> placerMap;

    public Bounty(OfflinePlayer target, OfflinePlayer placer, int amount){
        this.targetUUID = target.getUniqueId();
        this.placerMap = new HashMap<>();
        this.placerMap.put(placer.getUniqueId(), amount);
    }

    public HashMap<UUID, Integer> getPlacerMap() {
        return placerMap;
    }

    public int getTotalAmount() {
        int amount = 0;
        for(Integer am : placerMap.values()){
            amount += am;
        }
        return amount;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public ArrayList<UUID> getPlacers() {
        return new ArrayList<>(placerMap.keySet());
    }

    public void combineWith(Bounty bounty){
        if(!this.targetUUID.equals(bounty.getTargetUUID())) return;

        for(Map.Entry<UUID, Integer> entry : bounty.getPlacerMap().entrySet()){
            UUID placer = entry.getKey();
            int amount = entry.getValue();

            if(this.placerMap.containsKey(placer)){
                this.placerMap.put(placer, this.getPlacerMap().get(placer) + amount);
            }
            else{
                this.placerMap.put(placer, amount);
            }
        }
    }

    public boolean isPlacer(UUID playerUUID){
        return getPlacers().contains(playerUUID);
    }

    public Integer removePlacer(UUID playerUUID){
        if(!isPlacer(playerUUID)) return 0;

        Integer amount = placerMap.remove(playerUUID);
        if(amount == null) return 0;
        else return amount;
    }
}

