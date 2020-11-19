package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.KingdomException;

public enum Relation {
    ENEMY(-2, "Enemy", "&4"),
    AGGRESSIVE(-1, "Aggressive", "&c"),
    NEUTRAL(0, "Neutral", "&e"),
    FRIENDLY(1, "Friendly", "&a"),
    ALLIED(2, "Allied", "&2");

    int relInt;
    String relName;
    String color;

    Relation(int relInt, String relName, String color){
        this.relInt = relInt;
        this.relName = relName;
        this.color = color;
    }

    public int getRelInt() {
        return relInt;
    }

    public String getRelName() {
        return relName;
    }

    public String getColor() {
        return color;
    }

    public String getDisplayName(){
        return color + relName;
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

    public static Relation getHighestRelation(){
        Relation relation = null;
        for(Relation r : values()){
            if(relation == null) relation = r;
            else if(relation.getRelInt() < r.getRelInt()) relation = r;
        }
        return relation;
    }

    public boolean isHighestRelation(){
        return this == getHighestRelation();
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

    public static Relation getLowestRelation(){
        Relation relation = null;
        for(Relation r : values()){
            if(relation == null) relation = r;
            else if(relation.getRelInt() > r.getRelInt()) relation = r;
        }
        return relation;
    }

    public boolean isLowestRelation(){
        return this == getLowestRelation();
    }

}
