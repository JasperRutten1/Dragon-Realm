package dscp.dragon_realm.customEnchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomEnchants {
    public static final Enchantment MAVERICKS_NUDES = new EnchantWrapper("maverics-nudes", "Maveric's nudes", 1, EnchantmentTarget.WEAPON);
    public static final double MAVERICS_NUDES_CHANCE = 0.50; //chance

    public static final Enchantment ENDER_EDGE = new EnchantWrapper("ender-edge", "Ender edge", 1, EnchantmentTarget.WEAPON);
    public static final int ENDER_EDGE_DISTANCE = 15; // distance the ender edge sword can teleport
    public static final int ENDER_EDGE_COOLDOWN = 60000; // cooldown in milli seconds

    public static List<Enchantment> AUTO_SMELT_CONFLICTS(){
        List<Enchantment> conflicts = new ArrayList<>();
        conflicts.add(Enchantment.SILK_TOUCH);
        return conflicts;
    }
    public static final Enchantment AUTO_SMELT = new EnchantWrapper("auto-smelt", "Auto Smelt", 1, EnchantmentTarget.TOOL, AUTO_SMELT_CONFLICTS());

    public static final Enchantment TELEPATHY = new EnchantWrapper("telepathy", "Telepathy", 1, EnchantmentTarget.TOOL);

    public static final Enchantment VELOCITY = new EnchantWrapper("velocity", "Velocity", 2, EnchantmentTarget.BOW);

    public static final Enchantment DEFLECT = new EnchantWrapper("deflect", "Deflect", 3, EnchantmentTarget.ARMOR);

    public static final Enchantment REPULSOR = new EnchantWrapper("repulsor", "Repulsor", 1, EnchantmentTarget.WEAPON);
    public static final int REPULSOR_MAX_CHARGE = 50;

    public static List<Enchantment> getCustomEnchants(){
        List<Enchantment> list = new ArrayList<>();
        list.add(MAVERICKS_NUDES);
        list.add(ENDER_EDGE);
        list.add(AUTO_SMELT);
        list.add(TELEPATHY);
        list.add(VELOCITY);
        list.add(DEFLECT);
        list.add(REPULSOR);

        return list;
    }

    public static void getEnchantedBook(Player player, Enchantment enchantment, int amount){
        if(amount > 64){
            player.sendMessage(ChatColor.RED + "you can only get a max of 64 books at once");
            amount = 64;
        }
        ItemStack books = new ItemStack(Material.BOOK, amount);
        enchantItem(books, enchantment);
        if(player.getInventory().firstEmpty() == -1){
            player.sendMessage(ChatColor.RED + "inventory full");
            return;
        }
        player.getInventory().addItem(books);
    }
    public static void getEnchantedBook(Player player, String key, int amount){
        Enchantment enchant = null;
        for(Enchantment e : getCustomEnchants()){
            if(e.getKey().equals(NamespacedKey.minecraft(key))) enchant = e;
        }
        if(enchant == null){
            player.sendMessage("could not find enchantment with this name");
            return;
        }
        getEnchantedBook(player, enchant, amount);
    }

    public static void enchantItem(ItemStack items, Enchantment enchantment){
        ItemMeta meta = items.getItemMeta();

        if(meta == null) return;
        if(meta.hasEnchant(enchantment)) return;

        List<String> lore = new ArrayList<>();
        if(meta.getLore() != null) lore.addAll(meta.getLore());
        lore.add(ChatColor.GRAY + enchantment.getName());
        meta.setLore(lore);
        items.setItemMeta(meta);
        items.addUnsafeEnchantment(enchantment, 1);
    }

    public static void enchantItem(Enchantment enchantment, Player player, int level){
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if(meta == null) return;
        if(meta.hasEnchant(enchantment)) return;

        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore.addAll(meta.getLore());
        lore.add(ChatColor.GRAY + enchantment.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(enchantment, level);
    }

    public static void register(){
        for(Enchantment enchantment : getCustomEnchants()){
            if(!(Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(enchantment)))
                registerEnchantment(enchantment);
        }
    }

    public static void registerEnchantment(Enchantment enchantment){
        boolean registered = true;
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        }
        catch (Exception e){
            registered = false;
            e.printStackTrace();
        }
        if(registered) System.out.println("registered new enchantment");
    }
}
