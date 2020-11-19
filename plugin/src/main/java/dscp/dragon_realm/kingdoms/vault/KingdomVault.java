package dscp.dragon_realm.kingdoms.vault;

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

public class KingdomVault implements Serializable {
    private static final long serialVersionUID = 5768758585003340606L;

    private Kingdom kingdom;
    private long coins;
    private ItemStack[] itemVault;
    private List<UUID> playersWatching = new ArrayList<>();

    public KingdomVault(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.itemVault = getEmptyInventory().getContents();
    }

    //getters

    public long getCoins() {
        return coins;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public ItemStack[] getItemVault() {
        return itemVault;
    }

    private static Inventory getEmptyInventory(){
        return Bukkit.createInventory(null, 27, "kingdom vault");
    }

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
