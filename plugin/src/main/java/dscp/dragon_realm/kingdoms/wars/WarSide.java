package dscp.dragon_realm.kingdoms.wars;

import dscp.dragon_realm.kingdoms.Kingdom;

public class WarSide {
    private Kingdom kingdom;
    private int warScore;
    private double exhaustion;

    public WarSide(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.warScore = 0;
        this.exhaustion = 0.0;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public double getExhaustion() {
        return exhaustion;
    }

    public int getWarScore() {
        return warScore;
    }
}
