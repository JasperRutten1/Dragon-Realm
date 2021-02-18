package dscp.dragon_realm.dragonProtect.areaProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DPEnderPearlEvent implements Listener {
    @EventHandler
    public void onEnderPearl(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        ProtectedArea areaFrom = ProtectedArea.getArea(player.getLocation().getChunk());
        if(event.getTo() == null) return;
        ProtectedArea areaTo = ProtectedArea.getArea(event.getTo().getChunk());

        if(!(areaTo == null && areaFrom == null)){
            if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
                    event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT){
                event.setCancelled(true);
                DragonProtect.sendMessage(player, "can not teleport in or out of protected zone");
            }
        }
    }
}
