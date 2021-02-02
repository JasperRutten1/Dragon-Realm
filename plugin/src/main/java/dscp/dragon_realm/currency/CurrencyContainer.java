package dscp.dragon_realm.currency;

import dscp.dragon_realm.utils.AdvancedObjectIO;

import java.io.Serializable;

public class CurrencyContainer implements Serializable {
    private static final long serialVersionUID = -8659521142521359214L;

    private DefaultCurrencyType currencyType;
    private int defaultCurrency;

    private int dragonEyes;

    public CurrencyContainer(DefaultCurrencyType currencyType){
        this.currencyType = currencyType;
        this.defaultCurrency = 0;
        this.dragonEyes = 0;
    }

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

    public int getDefaultCurrencyInCoins(){
        return this.currencyType.toCoins(this.defaultCurrency);
    }
}
