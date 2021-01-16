package dscp.dragon_realm.kingdoms.vault;

import dscp.dragon_realm.currency.CurrencyContainer;
import dscp.dragon_realm.currency.DefaultCurrencyType;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KingdomVault extends CurrencyContainer {
    private static final long serialVersionUID = 5768758585003340606L;

    private Kingdom kingdom;

    public KingdomVault(Kingdom kingdom) {
        super(DefaultCurrencyType.COINS);
        this.kingdom = kingdom;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }


}
