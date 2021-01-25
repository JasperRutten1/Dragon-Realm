package dscp.dragon_realm.currency;

import dscp.dragon_realm.utils.AdvancedObjectIO;

import java.io.Serializable;

public class CurrencyContainer implements Serializable {
    private static final long serialVersionUID = -8659521142521359214L;

    private DefaultCurrencyType currencyType;
    private int defaultCurrency;

    public CurrencyContainer(DefaultCurrencyType currencyType){
        this.currencyType = currencyType;
        this.defaultCurrency = 0;
    }

    public DefaultCurrencyType getCurrencyType() {
        return currencyType;
    }

    public int getDefaultCurrency() {
        return defaultCurrency;
    }

    public int convergeDefaultType(DefaultCurrencyType newCurrencyType){
        int newDefaultCurrency = (int) Math.floor(this.currencyType.toCoins(this.defaultCurrency) * newCurrencyType.getValue());
        this.currencyType = newCurrencyType;
        this.defaultCurrency = newDefaultCurrency;
        return newDefaultCurrency;
    }

    public int changeDefaultCurrency(int coins){
        this.defaultCurrency += this.currencyType.fromCoins(coins);
        return this.defaultCurrency;
    }

    public void transferDefaultTo(CurrencyContainer container, int coins){
        container.changeDefaultCurrency(coins);
        this.changeDefaultCurrency(-coins);
    }
}
