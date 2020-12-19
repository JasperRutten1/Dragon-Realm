package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class AquaLaunchActive extends ActiveAbility{

    public AquaLaunchActive() {
        super("Aqua Launch", 1001, SpiritElement.WATER, 10, 5000);
    }

    @Override
    public void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {
        if(player.getEyeLocation().getBlock().getType() == Material.WATER){
            if(hasCooldown(player)){
                player.sendMessage(ChatColor.GRAY + "in cooldown");
                return;
            }

            Vector vector = player.getEyeLocation().getDirection().normalize().multiply(2);
            player.setVelocity(vector);
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
