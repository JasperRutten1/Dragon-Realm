package dscp.dragon_realm.kingdoms.claims.settlements;

public enum SettlementCosts {
    OUTPOST(2500,0, 0, SettlementLevel.Outpost),
    VILLAGE(10000,1,0, SettlementLevel.Village),
    TOWN(50000,5,1, SettlementLevel.Town),
    CITY(300000,20,5, SettlementLevel.City);

    long coins;
    SettlementLevel level;

    SettlementCosts(int coins, int enderDust, int dragonPearl, SettlementLevel level){
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
