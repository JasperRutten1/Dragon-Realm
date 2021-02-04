package dscp.dragon_realm.kingdoms.bank;

import dscp.dragon_realm.currency.CurrencyContainer;
import dscp.dragon_realm.currency.DefaultCurrencyType;

import java.util.UUID;

public class KingdomBankAccount extends CurrencyContainer {
    private KingdomBank bank;
    private UUID playerUUID;

    public KingdomBankAccount(UUID playerUUID, KingdomBank bank) {
        super(bank.getDefaultCurrencyType(), 150, 25);
        this.bank = bank;
        this.playerUUID = playerUUID;
    }


}
