package dscp.dragon_realm.NPCs.merchants;

import dscp.dragon_realm.Dragon_Realm;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;

public class Merchant {

    public static NamespacedKey typeKey = new NamespacedKey(Dragon_Realm.instance, "merchantType");

    public static void summonMerchant(Location location, int id, boolean noAi){
       World world = location.getWorld();
       assert world != null;

       Villager villager = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
       villager.setCustomName("Cosmetic Merchant");
       villager.setCustomNameVisible(true);
       villager.setAI(!noAi);
       villager.getPersistentDataContainer().set(typeKey, PersistentDataType.INTEGER, id);
       villager.setInvulnerable(true);
       villager.setProfession(Villager.Profession.NONE);
    }

    public static void summonMerchant(Location location, int id){
        summonMerchant(location, id, false);
    }

    public static boolean isMerchant(Villager villager){
        return villager.getPersistentDataContainer().has(typeKey, PersistentDataType.INTEGER);
    }
}
