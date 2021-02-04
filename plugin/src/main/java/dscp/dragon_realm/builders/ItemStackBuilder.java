package dscp.dragon_realm.builders;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Creates itemstacks
 */
public class ItemStackBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemStackBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemStackBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    public ItemStackBuilder name(String name) {
        this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemStackBuilder lore(LoreBuilder lore) {
        this.meta.setLore(lore.build());
        return this;
    }

    public ItemStackBuilder customModelData(int data){
        this.meta.setCustomModelData(data);
        return this;
    }

    public ItemStackBuilder addEnchants(Enchantment[] enchantments, int[] amplifiers) {
        int i = 0;
        for(Enchantment enchantment : enchantments) {
            if(enchantment != null) meta.addEnchant(enchantment, amplifiers[i], true);
            i++;
        }
        return this;
    }

    public ItemStackBuilder addEnchant(Enchantment enchantment, int amplifier) {
        if(enchantment != null) meta.addEnchant(enchantment, amplifier, true);
        return this;
    }

    public ItemStackBuilder removeEnchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemStackBuilder setArmorColor(Color color) {
        try {
            LeatherArmorMeta ameta = (LeatherArmorMeta) meta;
            ameta.setColor(color);
        } catch(ClassCastException ex) {
            return this;
        }

        return this;
    }

    public ItemStackBuilder setFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemStackBuilder setUnbreakable() {
        meta.setUnbreakable(true);
        return this;
    }

    public ItemStackBuilder setSkullOwner(OfflinePlayer player) {
        ((SkullMeta) meta).setOwningPlayer(player);
        return this;
    }

    public <T, Z> ItemStackBuilder addPersistentData(NamespacedKey key, PersistentDataType<T, Z> dataType, Z data){
        meta.getPersistentDataContainer().set(key, dataType, data);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(meta);
        return this.item;
    }
}
