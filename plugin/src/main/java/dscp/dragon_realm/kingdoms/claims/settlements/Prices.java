package dscp.dragon_realm.kingdoms.claims.settlements;

public enum Prices {
    SETTLEMENT_STARTING_PRICE(1000, 5),
    TOWN_UPGRADE(5000, 20),
    VILLAGE_UPGRADE(20000, 50),
    CITY_UPGRADE(100000, 200);

    int coins;
    int essence;

    Prices(int coins, int essence){
        this.coins = coins;
        this.essence = essence;
    }
}
