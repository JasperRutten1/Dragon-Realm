package dscp.dragon_realm.bounty;

import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.UUID;

public class Bounty implements Serializable {
    private static final long serialVersionUID = -6782037735624835768L;

    private UUID targetUUID, placerUUID;
    private int bounty;

    public Bounty(OfflinePlayer target, OfflinePlayer placer, int bounty){
        this.targetUUID = target.getUniqueId();
        this.placerUUID = placer.getUniqueId();
        this.bounty = bounty;
    }

    public int getBounty() {
        return bounty;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public UUID getPlacerUUID() {
        return placerUUID;
    }
}

