package dscp.dragon_realm.specialWeapons.spiritSwords.events;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SSPlayerDeathEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        List<ItemStack> items = new ArrayList<>(event.getDrops());
        for(ItemStack itemStack : items){
            if(SpiritSword.isSpiritSword(itemStack)){
                ItemMeta meta = itemStack.getItemMeta();
                if(SpiritSword.getLevel(meta) >= 2 && SpiritSword.isOwner(meta, player)){
                    SpiritSword.addSpiritSwordToSoulBindMap(player.getUniqueId(), meta);
                    event.getDrops().remove(itemStack);
                }
            }
        }
    }
}
