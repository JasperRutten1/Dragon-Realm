package dscp.dragon_realm.currency;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.builders.ItemStackBuilder;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class DroppedCurrency {
    public static ArrayList<Item> coinsArray = new ArrayList<>();
    public static ArrayList<Item> powderArray = new ArrayList<>();
    public static  ArrayList<Item> pearlArray = new ArrayList<>();

    public static NamespacedKey coinsKey = new NamespacedKey(Dragon_Realm.instance, "coins");
    public static NamespacedKey powderKey = new NamespacedKey(Dragon_Realm.instance, "powder");
    public static NamespacedKey pearlKey = new NamespacedKey(Dragon_Realm.instance, "pearl");

    public static void dropCoinsNaturally(Location location, int amount){
        World world = location.getWorld();
        assert world != null;
        Item coins = world.dropItemNaturally(location, new ItemStackBuilder(Material.SUNFLOWER)
                .name("coins")
                .customModelData(1111111)
                .addPersistentData(coinsKey, PersistentDataType.INTEGER, amount)
                .build()
        );

        coins.setCustomName(ChatColor.GOLD + "" + amount + " Coins");
        coins.setCustomNameVisible(true);
        coins.setPickupDelay(60);
        coins.setInvulnerable(true);
        coinsArray.add(coins);
    }

    public static void dropCoins(Location location, int amount){
        World world = location.getWorld();
        assert world != null;
        Item coins = world.dropItem(location, new ItemStackBuilder(Material.SUNFLOWER)
                .name("coins")
                .customModelData(1111111)
                .addPersistentData(coinsKey, PersistentDataType.INTEGER, amount)
                .build()
        );

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
        return meta.getPersistentDataContainer().has(DroppedCurrency.coinsKey, PersistentDataType.INTEGER);
    }

    public static void dropEnderPowder(Location location, int amount){
        World world = location.getWorld();
        assert world != null;
        Item powder = world.dropItem(location, new ItemStackBuilder(Material.BLAZE_POWDER)
                .name("ender_dust")
                .addPersistentData(powderKey, PersistentDataType.INTEGER, amount)
                .customModelData(1111111)
                .build()
        );

        powder.setCustomName(ChatColor.DARK_AQUA + (amount > 1 ? (amount + " ") : "") + "Ender Powder");
        powder.setCustomNameVisible(true);
        powder.setPickupDelay(60);
        powder.setInvulnerable(true);
        powderArray.add(powder);
    }

    public static boolean isDroppedPowder(Item item){
        ItemStack stack = item.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        return meta.getPersistentDataContainer().has(powderKey, PersistentDataType.INTEGER);
    }

    public static void dropDragonPearl(Location location, int amount){
        World world = location.getWorld();
        assert world != null;
        Item pearl = world.dropItem(location, new ItemStackBuilder(Material.ENDER_EYE)
                .name("dragon_pearl")
                .addPersistentData(pearlKey, PersistentDataType.INTEGER, amount)
                .customModelData(1111111)
                .build()
        );

        pearl.setCustomName(ChatColor.DARK_RED + (amount > 1 ? (amount + " ") : "") + "Dragon Pearl");
        pearl.setCustomNameVisible(true);
        pearl.setPickupDelay(60);
        pearl.setInvulnerable(true);
        pearlArray.add(pearl);
    }

    public static boolean isDroppedPearl(Item item){
        ItemStack stack = item.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        return meta.getPersistentDataContainer().has(pearlKey, PersistentDataType.INTEGER);
    }
}
