package dscp.dragon_realm.currency;

public enum DefaultCurrencyType {
    COINS(1, "Coins", 1111111),
    DRACHMA(1.2, "Drachma", 1111112),
    OBOL(0.5, "Obol", 1111113);

    private double value;
    private String name;

    DefaultCurrencyType(double value, String name, int textureData){
        this.value = value;
        this.name = name;
    }

    public int toCoins(int amount){
        return (int) Math.floor(this.value * amount);
    }

    public int fromCoins(int coins){
        return (int) Math.ceil(coins/value);
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
