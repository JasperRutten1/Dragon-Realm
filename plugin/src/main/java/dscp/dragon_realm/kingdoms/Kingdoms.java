package dscp.dragon_realm.kingdoms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Kingdoms implements Serializable {
    private static final long serialVersionUID = -767662434221754257L;

    private List<Kingdom> kingdomsList;

    public Kingdoms(){
        this.kingdomsList = new ArrayList<>();
    }

    public List<Kingdom> getKingdomsList() {
        return kingdomsList;
    }

    public void add(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        if(kingdomsList.contains(kingdom)) return;
        kingdomsList.add(kingdom);
    }

    public void remove(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        kingdomsList.remove(kingdom);
    }
}
