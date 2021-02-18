package dscp.dragon_realm.dragonProtect.areaProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DPPlayerDamageEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();
        ProtectedArea pzDamaged = ProtectedArea.getArea(damaged.getLocation().getChunk());

        if(event.getDamager() instanceof Player){ //hit by player
            Player damager = (Player) event.getDamager();
            ProtectedArea pzDamager = ProtectedArea.getArea(damager.getLocation().getChunk());

            if(pzDamaged == null && pzDamager == null) return;
            event.setCancelled(true);
            DragonProtect.sendMessage(damager, "pvp is disabled in this area");
        }
        else if(event.getDamager() instanceof Projectile){ //shot by player
            Projectile projectile = (Projectile) event.getDamager();
            if(projectile.getShooter() instanceof Player){
                Player shooter = (Player) projectile.getShooter();
                ProtectedArea pzShooter = ProtectedArea.getArea(shooter.getLocation().getChunk());

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
