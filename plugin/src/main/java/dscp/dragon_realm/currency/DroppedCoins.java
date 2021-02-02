package dscp.dragon_realm.currency;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.builders.ItemStackBuilder;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;
import java.util.ArrayList;

public class DroppedCoins {
    public static ArrayList<Item> coinsArray = new ArrayList<>();

    public static NamespacedKey key = new NamespacedKey(Dragon_Realm.instance, "coins");

    public static void dropCoinsNaturally(Location location, int amount){
        World world = location.getWorld();
        ItemStack stack = new ItemStackBuilder(Material.SUNFLOWER)
                .name("Coins")
                .build();

        ItemMeta meta = stack.getItemMeta();

        assert world != null;
        assert meta != null;

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, amount);
        meta.setCustomModelData(1111111);

        stack.setItemMeta(meta);

        Item coins = world.dropItemNaturally(location, stack);
        coins.setCustomName(ChatColor.GOLD + "" + amount + " Coins");
        coins.setCustomNameVisible(true);
        coins.setPickupDelay(60);
        coins.setInvulnerable(true);
        coinsArray.add(coins);
    }

    public static  void dropCoins(Location location, int amount){
        World world = location.getWorld();
        ItemStack stack = new ItemStackBuilder(Material.SUNFLOWER)
                .name("Coins")
                .build();

        ItemMeta meta = stack.getItemMeta();

        assert world != null;
        assert meta != null;

        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, amount);

        stack.setItemMeta(meta);

        Item coins = world.dropItem(location, stack);
        coins.setCustomName(ChatColor.GOLD + "" + amount + " Coins");
        coins.setCustomNameVisible(true);
        coins.setPickupDelay(60);
        coins.setInvulnerable(true);
        coinsArray.add(coins);
    }

    private static boolean isDroppedCoins(Item item){
        ItemStack stack = item.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        return meta.getPersistentDataContainer().has(DroppedCoins.key, PersistentDataType.INTEGER);
    }
}
