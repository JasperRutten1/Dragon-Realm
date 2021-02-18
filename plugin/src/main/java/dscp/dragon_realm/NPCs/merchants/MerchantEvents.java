package dscp.dragon_realm.NPCs.merchants;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.EventListener;

public class MerchantEvents implements Listener {
    @EventHandler
    public void rightClickVillager(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof Villager)) return;

        Player player = event.getPlayer();
        Villager villager = (Villager) event.getRightClicked();

        if(!Merchant.isMerchant(villager)) return;

        int id = villager.getPersistentDataContainer().get(Merchant.typeKey, PersistentDataType.INTEGER);
        MerchantType type = MerchantType.getType(id);
        if(type == null) return;

        type.getMenu().open(player);
        event.setCancelled(true);
    }

}
