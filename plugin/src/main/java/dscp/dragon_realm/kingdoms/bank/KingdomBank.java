package dscp.dragon_realm.kingdoms.bank;

import dscp.dragon_realm.currency.DefaultCurrencyType;
import dscp.dragon_realm.kingdoms.Kingdom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomBank implements Serializable {
    private static final long serialVersionUID = 8413810371649515997L;

    private Kingdom kingdom;
    private HashMap<UUID, KingdomBankAccount> accounts;
    private DefaultCurrencyType defaultCurrencyType;

    public KingdomBank(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.defaultCurrencyType = DefaultCurrencyType.COINS;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public DefaultCurrencyType getDefaultCurrencyType() {
        return defaultCurrencyType;
    }

    public HashMap<UUID, KingdomBankAccount> getAccounts() {
        return accounts;
    }

    public KingdomBankAccount getAccount(UUID playerUUID){
        if(playerUUID == null) throw new IllegalArgumentException("player UUID can't be null");
        return accounts.get(playerUUID);
    }

    public void convertToCurrencyType(DefaultCurrencyType type){
        this.defaultCurrencyType = type;
        for(Map.Entry<UUID, KingdomBankAccount> entry : accounts.entrySet()){
            KingdomBankAccount account = entry.getValue();
            account.convertDefaultType(type);
        }
    }
}
