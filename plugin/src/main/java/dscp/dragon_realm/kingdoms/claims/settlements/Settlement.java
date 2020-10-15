package dscp.dragon_realm.kingdoms.claims.settlements;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;

import java.io.Serializable;

public class Settlement implements Serializable {
    private static final long serialVersionUID = -4617056264275810513L;

    SettlementTypes type;
    String name;
    Kingdom kingdom;

    public Settlement(SettlementTypes type, Kingdom kingdom, String name) throws KingdomException {
        if(type == null) throw new KingdomException("type can't be null");
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        if(name == null) throw new KingdomException("name can't be null");

        this.type = type;
        this.name = name;
        this.kingdom = kingdom;
    }

    public SettlementTypes getType() {
        return type;
    }



}
