package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.advancedParticles.AdvancedParticles;
import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.w3c.dom.ranges.Range;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GroundSlamActive extends ActiveAbility{

    private ArrayList<Player> groundSlammers = new ArrayList<>();

    private static final double RANGE = 3.0;
    private static final double DAMAGE_MULTIPLIER = 4;

    public GroundSlamActive() {
        super("Ground Slam", 3001, SpiritElement.EARTH, 5, 30000, 2);
    }

    @Override
    public void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {
        if(player.getLocation().add(0,-1,0).getBlock().getType() == Material.AIR) return;
        if(hasCooldown(player)){
            player.sendMessage(ChatColor.GRAY + "in cooldown");
            return;
        }

        Vector vector = new Vector(0, 1.5, 0);
        player.setVelocity(vector);
        setCooldown(player);

        Dragon_Realm.instance.getServer().getScheduler().scheduleSyncDelayedTask(Dragon_Realm.instance, new Runnable() {
            @Override
            public void run() {
                groundSlammers.add(player);
            }
        }, 20L);
    }

    @Override
    public void leftClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void other(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void repeatingCode(Player player) {
        if(groundSlammers.contains(player)){
            if(((Entity) player).isOnGround()){
                groundSlammers.remove(player);
                groundSlam(player);
            }
        }
    }

    @Override
    public void abilityInfo(BookBuilder.BookPageBuilder pageBuilder) {
        pageBuilder.addLine("This ability will launch the holder in to the air.")
                .addLine("When the holder then hits the ground, it will create a shock wave that will damage and knock away all other players in range")
                .addBlankLine()
                .addLine("A player that is closer to the center of the shock wave will receive more damage then a player further away from teh center")
                .addBlankLine()
                .addLine("Range: " + RANGE + " blocks.");
    }

    private void groundSlam(Player player){
        ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        ArrayList<Player> inRangePlayers = new ArrayList<>();

        for(Player p : onlinePlayers){
            if(player.getLocation().distance(p.getLocation()) <= RANGE) inRangePlayers.add(p);
        }

        //particles
        BukkitScheduler scheduler = Dragon_Realm.instance.getServer().getScheduler();
        for(double i = 0.5 ; i <= 3 ; i += 0.5){
            AdvancedParticles.particleCircle(player.getLocation(), Particle.CRIT, i, (int) (i * 20), 0);
        }

        for(Player p : inRangePlayers){
            if(!p.equals(player)){
                if(((Entity) p).isOnGround()){
                    double distance = player.getLocation().distance(p.getLocation());
                    Vector vector = p.getLocation().toVector().subtract(player.getLocation().toVector());

                    //knockback
                    vector.add(new Vector(0, 0.5, 0));
                    vector.normalize();
                    vector.multiply((RANGE - distance) + 1);
                    p.setVelocity(vector);

                    //deal damage
                    double damage = (RANGE - distance) * DAMAGE_MULTIPLIER;
                    p.damage(damage);
                }
            }
        }

        player.sendMessage("slam!");
    }
}
