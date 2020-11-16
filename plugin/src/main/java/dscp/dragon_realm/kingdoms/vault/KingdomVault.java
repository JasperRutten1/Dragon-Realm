package dscp.dragon_realm.kingdoms.vault;

import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;

public class KingdomVault implements Serializable {
    private static final long serialVersionUID = 5768758585003340606L;

    private Kingdom kingdom;
    private long coins;
    // private Inventory itemVault;

    public KingdomVault(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        // this.itemVault = Bukkit.createInventory(null, 27, "kingdom vault");
    }

    //getters

    public long getCoins() {
        return coins;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    /**
    public Inventory getItemVault() {
        return itemVault;
    }**/

    public KingdomVault addCoins(long coins) throws VaultException {
        if(this.coins >= Long.MAX_VALUE - coins) throw new VaultException("can not add coins, long limit reached");
        this.coins += coins;
        return this;
    }

    public KingdomVault removeCoins(long coins) throws VaultException {
        if(this.coins - coins <= 0) throw new VaultException("can not subtract coins, can not go below zero");
        this.coins -= coins;
        return this;
    }
}
