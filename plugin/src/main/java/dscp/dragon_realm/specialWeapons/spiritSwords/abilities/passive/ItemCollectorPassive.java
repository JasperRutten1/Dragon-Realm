package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.w3c.dom.ranges.Range;

import java.util.ArrayList;
import java.util.List;

public class ItemCollectorPassive extends PassiveAbility{
    private static final double RANGE = 5;

    public ItemCollectorPassive() {
        super("Carrier Wind", 2001, 2, SpiritElement.AIR, 10);
    }

    @Override
    public void abilityCode(Player player, ItemStack sword, ItemMeta swordMeta) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        List<Entity> entities = new ArrayList<>(world.getNearbyEntities(playerLocation, RANGE, RANGE, RANGE));

        for(Entity entity: entities){
            double distance = playerLocation.distance(entity.getLocation());
            if(distance <= RANGE){
                if(entity instanceof Item){
                    Location entityLocation = entity.getLocation();
                    Vector vector = playerLocation.toVector().subtract(entityLocation.toVector());
                    vector.multiply((RANGE - distance) / RANGE);
                    vector.multiply(0.2);
                    entity.setVelocity(vector);
                }
            }
        }
    }
}
