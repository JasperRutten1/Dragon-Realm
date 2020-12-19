package dscp.dragon_realm.specialWeapons.spiritSwords.events;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SSDamageEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        Player damager = (Player) event.getDamager();

        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            if(!SpiritSword.isSpiritSword(damager.getInventory().getItemInMainHand())) return;
            ItemStack sword = damager.getInventory().getItemInMainHand();
            ItemMeta meta = sword.getItemMeta();

            if(!SpiritSword.isOwner(meta, damager)){
                damager.sendMessage(ChatColor.RED + "this sword is bound to another player");
                event.setCancelled(true);
                return;
            }

            if(event.getEntity() instanceof Player){
                SpiritSword.addExperience(meta, event.getFinalDamage() * 2);
            }else{
                SpiritSword.addExperience(meta, event.getFinalDamage());
            }
            SpiritSword.updateSpiritSwordMeta(meta, sword);
        }
    }
}
