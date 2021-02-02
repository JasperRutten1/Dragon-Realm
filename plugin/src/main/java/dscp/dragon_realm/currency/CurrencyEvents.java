package dscp.dragon_realm.currency;

import dscp.dragon_realm.Dragon_Realm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Random;

public class CurrencyEvents implements Listener {
    public static int DROP_CHANCE = 30;

    public CurrencyEvents(){
        Dragon_Realm.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Dragon_Realm.getInstance(), new CoinsMerge(), 0L, 20L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCoinsPickup(EntityPickupItemEvent event){
        Item item = event.getItem();
        ItemStack stack = item.getItemStack();
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        if(meta.getPersistentDataContainer().has(DroppedCoins.key, PersistentDataType.INTEGER)){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                int amount = meta.getPersistentDataContainer().get(DroppedCoins.key, PersistentDataType.INTEGER);
                PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(player);

                event.setCancelled(true);
                item.remove();
                wallet.changeDefaultCurrency(amount);
                player.sendMessage(ChatColor.GOLD + "Picked up " + amount + " Coins ("
                        + wallet.getCurrencyType().fromCoins(amount) + " " + wallet.getCurrencyType().getName() + ")");
            }
            else{
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void playerKillsEntity(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();

        if(entity instanceof ArmorStand) return;
        if(entity instanceof ItemFrame) return;

        if(entity.getKiller() != null){
            Player player = entity.getKiller();

            int dropAmount = new Random().nextInt(4) + 1;
            int dropChance = new Random().nextInt(100);

            if(dropChance <= DROP_CHANCE){
                DroppedCoins.dropCoinsNaturally(entity.getLocation(), dropAmount);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void itemMergeEvent(ItemMergeEvent event){
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        if(itemMeta.getPersistentDataContainer().has(DroppedCoins.key, PersistentDataType.INTEGER)){
            Item target = event.getTarget();
            ItemStack targetStack = target.getItemStack();
            ItemMeta targetMeta = targetStack.getItemMeta();
            assert targetMeta != null;

            if(targetMeta.getPersistentDataContainer().has(DroppedCoins.key, PersistentDataType.INTEGER)){
                int total = itemMeta.getPersistentDataContainer().get(DroppedCoins.key, PersistentDataType.INTEGER)
                        + targetMeta.getPersistentDataContainer().get(DroppedCoins.key, PersistentDataType.INTEGER);

                item.remove();
                DroppedCoins.coinsArray.remove(item);
                target.remove();
                DroppedCoins.coinsArray.remove(target);
                DroppedCoins.dropCoins(target.getLocation(), total);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(player);
        if(player.getKiller() != null){
            int dropAmount = (int) Math.ceil(wallet.getDefaultCurrencyInCoins() / 3);
            if(dropAmount < 1) return;
            wallet.changeDefaultCurrency(-dropAmount);
            DroppedCoins.dropCoinsNaturally(player.getLocation(), dropAmount);
        }
    }

    private class CoinsMerge implements Runnable{

        @Override
        public void run() {
            DroppedCoins.coinsArray.removeIf(item -> Bukkit.getEntity(item.getUniqueId()) == null);

            //merge coins
            ArrayList<Item> coinsArray = new ArrayList<>(DroppedCoins.coinsArray);
            for(Item coins : coinsArray){
                if(DroppedCoins.coinsArray.contains(coins)){
                    ArrayList<Item> otherCoins = new ArrayList<>(DroppedCoins.coinsArray);
                    otherCoins.remove(coins);
                    ArrayList<Item> nearbyCoins = new ArrayList<>();
                    World world = coins.getWorld();

                    //get nearby dropped coins
                    for(Item other : otherCoins){
                        double distance = coins.getLocation().distance(other.getLocation());
                        if(distance <= 2){
                            nearbyCoins.add(other);
                        }
                    }

                    if(nearbyCoins.size() >= 1){
                        //remove dropped coins from static array
                        DroppedCoins.coinsArray.removeAll(nearbyCoins);
                        DroppedCoins.coinsArray.remove(coins);

                        //calculate total (and remove dropped coins from in game)
                        ItemMeta originalMeta = coins.getItemStack().getItemMeta();
                        assert originalMeta != null;
                        coins.remove();

                        int total = originalMeta.getPersistentDataContainer().get(DroppedCoins.key, PersistentDataType.INTEGER);
                        for(Item nearby : nearbyCoins){
                            ItemMeta nearbyMeta = nearby.getItemStack().getItemMeta();
                            assert nearbyMeta != null;
                            total += nearbyMeta.getPersistentDataContainer().get(DroppedCoins.key, PersistentDataType.INTEGER);
                            nearby.remove();
                        }

                        //spawn new coin
                        Location loc = coins.getLocation();
                        DroppedCoins.dropCoins(loc, total);
                    }
                }
            }

        }

    }
}
