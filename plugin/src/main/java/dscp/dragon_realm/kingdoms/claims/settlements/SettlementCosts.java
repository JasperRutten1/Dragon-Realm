package dscp.dragon_realm.kingdoms.claims.settlements;

public enum SettlementCosts {
    OUTPOST(2500, SettlementLevel.Outpost),
    VILLAGE(10000, SettlementLevel.Village),
    TOWN(300000, SettlementLevel.Town),
    CITY(1000000, SettlementLevel.City);

    long coins;
    SettlementLevel level;

    SettlementCosts(long coins, SettlementLevel level){
        if(level == null) throw new IllegalArgumentException("level can't be null");
        this.coins = coins;
        this.level = level;
    }

    public SettlementLevel getLevel() {
        return level;
    }

    public long getCoins() {
        return coins;
    }

    public static SettlementCosts getCost(SettlementLevel level){
        for(SettlementCosts cost : values()){
            if(cost.level == level){
                return cost;
            }
        }
        return null;
    }
}
