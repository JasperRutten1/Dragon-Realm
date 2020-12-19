package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.advancedParticles.AdvancedParticles;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Random;

public class WormHolePassive extends PassiveAbility{
    public WormHolePassive() {
        super("Wormhole", 5001, 0, SpiritElement.VOID, 10);
    }

    @Override
    public void abilityCode(Player player, ItemStack sword, ItemMeta swordMeta) {

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onArrowHit(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Projectile)) return;

        Player player = (Player) event.getEntity();
        Projectile projectile = (Projectile) event.getDamager();

        if(SpiritSword.isSpiritSword(player.getInventory().getItemInMainHand())
                && SpiritSword.isOwner(player.getInventory().getItemInMainHand().getItemMeta(), player)){
            int random = new Random().nextInt(100);
            if(random <= 33){
                event.setCancelled(true);
                Vector vector = projectile.getVelocity().normalize().multiply(1.3);
                Projectile proj = (Projectile) player.getWorld().spawnEntity(player.getLocation().add(vector), projectile.getType());
                proj.setVelocity(projectile.getVelocity());
                AdvancedParticles.particleSphere(projectile.getLocation(), Particle.PORTAL, 0.3, 10, 0);
                AdvancedParticles.particleSphere(proj.getLocation(), Particle.PORTAL, 0.3, 10, 0);
                projectile.remove();
            }
        }
    }
}
