package dscp.dragon_realm.currency;

import java.io.Serializable;

public class CurrencyContainer implements Serializable {
    private static final long serialVersionUID = -8659521142521359214L;

    private DefaultCurrencyType currencyType;
    private int defaultCurrency;

    private int enderPowder;
    private int enderPowder_max;

    private int dragonPearl;
    private int dragonPearl_max;

    public CurrencyContainer(DefaultCurrencyType currencyType, int enderDust_max, int dragonPearl_max){
        this.currencyType = currencyType;
        this.defaultCurrency = 0;
        this.enderPowder_max = enderDust_max;
        this.dragonPearl_max = dragonPearl_max;
        this.enderPowder = 0;
        this.dragonPearl = 0;
    }

    //default currency

    public DefaultCurrencyType getCurrencyType() {
        return currencyType;
    }

    public int getDefaultCurrency() {
        return defaultCurrency;
    }

    public int convertDefaultType(DefaultCurrencyType newCurrencyType){
        int newDefaultCurrency = (int) Math.floor(this.currencyType.toCoins(this.defaultCurrency) / newCurrencyType.getValue());
        this.currencyType = newCurrencyType;
        this.defaultCurrency = newDefaultCurrency;
        return newDefaultCurrency;
    }

    public int changeDefaultCurrency(int coins){
        this.defaultCurrency += this.currencyType.fromCoins(coins);
        return this.defaultCurrency;
    }

    public boolean hasEnoughDefault(int amount, DefaultCurrencyType type){
        return type.toCoins(amount) <= getCurrencyType().toCoins(getDefaultCurrency());
    }

    public int getDefaultCurrencyInCoins(){
        return this.currencyType.toCoins(this.defaultCurrency);
    }

    public boolean hasDefault(int coins){
        return getDefaultCurrencyInCoins() >= coins;
    }

    public void transferDefaultTo(CurrencyContainer container, int coins){
        if(hasDefault(coins)){
            container.changeDefaultCurrency(coins);
            changeDefaultCurrency(-coins);
        }
    }

    //ender powder

    public int getEnderPowder() {
        return enderPowder;
    }

    public int getEnderPowder_max() {
        return enderPowder_max;
    }

    public int changeEnderPowder(int amount){
        this.enderPowder += amount;
        return this.enderPowder;
    }

    public boolean hasEnderPowder(int amount){
        return enderPowder >= amount;
    }

    //dragon pearl

    public int getDragonPearl() {
        return dragonPearl;
    }

    public int getDragonPearl_max() {
        return dragonPearl_max;
    }

    public int changeDragonPearl(int amount){
        this.dragonPearl += amount;
        return this.dragonPearl;
    }

    public boolean hasDragonPearl(int amount){
        return this.dragonPearl >= amount;
    }
}
