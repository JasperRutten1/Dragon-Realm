package dscp.dragon_realm.dragonProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import sun.jvm.hotspot.opto.Block;

public class DPPlayerDamageEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();
        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pzDamaged = dp.getZone(damaged.getLocation().getChunk());

        if(event.getDamager() instanceof Player){ //hit by player
            Player damager = (Player) event.getDamager();
            ProtectedZone pzDamager = dp.getZone(damager.getLocation().getChunk());

            if(pzDamaged == null && pzDamager == null) return;
            event.setCancelled(true);
            DragonProtect.sendMessage(damager, "pvp is disabled in this area");
        }
        else if(event.getDamager() instanceof Projectile){ //shot by player
            Projectile projectile = (Projectile) event.getDamager();
            if(projectile.getShooter() instanceof Player){
                Player shooter = (Player) projectile.getShooter();
                ProtectedZone pzShooter = dp.getZone(shooter.getLocation().getChunk());

                if(!(pzDamaged == null && pzShooter == null)){
                    event.setCancelled(true);
                    DragonProtect.sendMessage(shooter, "pvp is disabled in this area");
                }
            }
        }
        else if(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION){ //damaged by block explosion
            if(!(pzDamaged == null)) event.setCancelled(true);
        }
        else if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){ //damaged by entity explosion
            if(!(pzDamaged == null)) event.setCancelled(true);
        }
    }
}
