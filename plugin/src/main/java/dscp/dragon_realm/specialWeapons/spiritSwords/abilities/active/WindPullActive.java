package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class WindPullActive extends ActiveAbility{
    public static final int RANGE = 15;

    public WindPullActive() {
        super("Wind Pull", 2001, SpiritElement.AIR, 5, 15000);
    }

    @Override
    public void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {
        if(hasCooldown(player)){
            player.sendMessage(ChatColor.GRAY + "in cooldown");
            return;
        }

        Entity entity = Dragon_Realm_API.getNearestEntityInSight(player, RANGE);

        if(entity instanceof LivingEntity){
            if(entity instanceof ArmorStand) return;
            Vector vector = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
            Vector velocity = new Vector(vector.getX(), 0.35, vector.getZ()).multiply(1.35);
            entity.setVelocity(velocity);
            setCooldown(player);
        }
    }

    @Override
    public void leftClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void other(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void repeatingCode(Player player) {

    }
}
