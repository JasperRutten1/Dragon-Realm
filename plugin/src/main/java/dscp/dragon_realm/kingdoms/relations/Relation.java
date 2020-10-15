package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.KingdomException;

public enum Relation {
    Neutral(0, "Neutral"),
    Friendly(1, "Friendly"),
    Allied(2, "Allied");

    int relInt;
    String relName;

    Relation(int relInt, String relName){
        this.relInt = relInt;
        this.relName = relName;
    }

    public int getRelInt() {
        return relInt;
    }
    public String getRelName() {
        return relName;
    }

    public Relation getRelationFromInt(int i){
        for(Relation relation : values()){
            if(relation.getRelInt() == i) return relation;
        }
        return null;
    }

    public boolean hasRelationOrBetter(Relation relation){
        return this.relInt > relation.getRelInt();
    }

    public Relation getRelationHigher(){
        Relation newRel = getRelationFromInt(this.relInt + 1);
        if(newRel == null) return this;
        else return newRel;
    }
    public static Relation getRelationHigher(Relation relation) throws KingdomException {
        if(relation == null) throw new KingdomException("relation can't be null");
        return relation.getRelationHigher();
    }

    public Relation getRelationLower(){
        Relation newRel = getRelationFromInt(this.relInt - 1);
        if(newRel == null) return this;
        else return newRel;
    }
    public static Relation getRelationLower(Relation relation) throws KingdomException {
        if(relation == null) throw new KingdomException("relation can't be null");
        else return relation.getRelationLower();

    }

}
