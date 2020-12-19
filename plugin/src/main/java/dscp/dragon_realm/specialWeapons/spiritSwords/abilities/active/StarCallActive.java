package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.advancedParticles.AdvancedParticles;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class StarCallActive extends ActiveAbility{
    public static final int RANGE = 20;

    public static final ArrayList<StarCallStar> stars = new ArrayList<>();


    public StarCallActive() {
        super("Star Call", 5002, SpiritElement.VOID, 5, 120000, 2);
    }

    @Override
    public void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {
        if(hasCooldown(player)){
            player.sendMessage(ChatColor.GRAY + "in cooldown");
            return;
        }

        Block targetBlock = player.getTargetBlockExact(RANGE);
        if(targetBlock == null) return;

        Location spawnLocation = targetBlock.getLocation().add(new Random().nextInt(5),
                new Random().nextInt(5) + 10, new Random().nextInt(5));

        stars.add(new StarCallStar(System.currentTimeMillis() + 3000, spawnLocation, targetBlock.getLocation()));
        setCooldown(player);

    }

    @Override
    public void leftClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void other(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void repeatingCode(Player player) {
        for(StarCallStar star : stars){
            if(star.isSpawned()){ //star is spawned in
                star.world.spawnParticle(Particle.FLAME, star.fireBall.getLocation(), 20, 0.1, 0.1, 0.1, 0.1);
                if(star.spawnTime + 20000 < System.currentTimeMillis()){
                    star.fireBall.remove();
                    stars.remove(star);
                }
            }
            else{ //star is not spawned in
                if(star.getSpawnTime() > System.currentTimeMillis()){ //before spawn
                    AdvancedParticles.particleSphere(star.spawn, Particle.PORTAL, 0.5, 10);
                }
                else{ //spawn
                    star.spawn();
                    Vector vector = star.target.toVector().subtract(star.spawn.toVector()).normalize();
                    vector.multiply(1.3);
                    star.fireBall.setVelocity(vector);
                }
            }
        }
    }

    public StarCallStar getStar(Fireball fireball){
        for(StarCallStar star: stars){
            if(fireball.equals(star.fireBall)) return star;
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onExplosion(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Fireball)) return;
        Fireball fireball = (Fireball) entity;

        StarCallStar star = getStar(fireball);
        if(star == null) return;

        event.setCancelled(true);
        stars.remove(star);
    }

    private static class StarCallStar {
        long spawnTime;
        boolean isSpawned;
        Fireball fireBall;
        Location spawn;
        Location target;
        World world;

        public static final double MULTIPLIER = 1.3;

        public StarCallStar(long spawnTime, Location spawn, Location target){
            if(!Objects.equals(spawn.getWorld(), target.getWorld())) throw new IllegalArgumentException("both locations must be in the same world");
            this.spawnTime = spawnTime;
            this.spawn = spawn;
            this.target = target;
            World world = spawn.getWorld();
            assert world != null;
            this.world = world;
            this.isSpawned = false;
        }

        public long getSpawnTime() {
            return spawnTime;
        }

        public Fireball getFireBall() {
            return fireBall;
        }

        public boolean isSpawned() {
            return isSpawned;
        }

        public void spawn(){
            if(isSpawned) return;
            Entity entity = world.spawnEntity(spawn, EntityType.FIREBALL);
            Fireball fireball = (Fireball) entity;
            fireball.setIsIncendiary(false);
            fireball.setYield(4F);
            fireball.setFireTicks(0);
            fireball.setDirection(target.toVector().subtract(spawn.toVector()).normalize());

            this.fireBall = fireball;
            this.isSpawned = true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StarCallStar)) return false;
            StarCallStar that = (StarCallStar) o;
            return spawnTime == that.spawnTime && isSpawned == that.isSpawned && Objects.equals(fireBall, that.fireBall) && Objects.equals(spawn, that.spawn) && Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(spawnTime, isSpawned, fireBall, spawn, target);
        }
    }
}
