package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingdomRelations implements Serializable {
    private static final long serialVersionUID = -3345511281010404841L;

    private Kingdom kingdom;
    private Map<Kingdom, Relation> relations;

    public KingdomRelations(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");

        this.kingdom = kingdom;
        this.relations = new HashMap<>();
    }
    public KingdomRelations(Kingdom kingdom, Map<Kingdom, Relation> relations) throws KingdomException {
        this(kingdom);

        if(relations == null) throw new KingdomException("relations can't be null");
        if(!(relations instanceof HashMap)) throw new KingdomException("map must me of type hashmap");

        this.relations = relations;
    }

    public void setRelationForKingdom(Kingdom kingdom, Relation relation) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        if(relation == null) throw new KingdomException("relation can't be null");

        this.relations.put(kingdom, relation);
    }

    public Relation getRelationForKingdom(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new KingdomException("kingdom can't be null");
        Relation relation = this.relations.get(kingdom);
        if(relation == null) return Relation.Neutral;
        else return relation;
    }
}
