package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;

import java.io.Serializable;

public class KingdomInvite implements Serializable {
    private static final long serialVersionUID = 1405286379462764393L;

    private Kingdom kingdom; //the kingdom the invite is bound to
    private long timeValid; //the time in milliseconds when the invite is no longer valid

    public KingdomInvite(Kingdom kingdom, long timeValid) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        if(timeValid == 0) throw new KingdomException("timeValid can't be 0");
        this.kingdom = kingdom;
        this.timeValid = timeValid;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public long getTimeValid() {
        return timeValid;
    }

    public boolean isValid(long time){
        return time <= this.timeValid;
    }
}
