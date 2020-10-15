package dscp.dragon_realm.kingdoms.claims.settlements;

public enum SettlementTypes {
    OUTPOST(0, "Outpost", 500, 20),
    TOWN(1, "Town", 750, 25),
    VILLAGE(2, "Village", 1250, 35),
    CITY(3, "City", 2000, 50);

    int level;
    String name;
    int power;
    int powerGeneration;

    SettlementTypes(int level, String name, int power, int powerGeneration){
        this.level = level;
        this.name = name;
        this.power = power;
        this.powerGeneration = powerGeneration;
    }

    public int getLevel() {
        return level;
    }
    public String getName() {
        return name;
    }
    public int getPower() {
        return power;
    }
    public int getPowerGeneration() {
        return powerGeneration;
    }


}
