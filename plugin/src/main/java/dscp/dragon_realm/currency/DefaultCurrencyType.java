package dscp.dragon_realm.currency;

public enum DefaultCurrencyType {
    COINS(1, "coins"),
    DRACHMA(1.2, "Drachma");

    private double value;
    private String name;

    DefaultCurrencyType(double value, String name){
        this.value = value;
        this.name = name;
    }

    public int toCoins(int amount){
        return (int) Math.floor(this.value * amount);
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
