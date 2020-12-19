package dscp.dragon_realm.dragonProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DPEnderPearlEvent implements Listener {
    @EventHandler
    public void onEnderPearl(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pzFrom = dp.getZone(event.getFrom().getChunk());
        if(event.getTo() == null) return;
        ProtectedZone pzTo = dp.getZone(event.getTo().getChunk());

        if(!(pzFrom == null && pzTo == null)){
            if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
                    event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT){
                event.setCancelled(true);
                DragonProtect.sendMessage(player, "can not teleport in or out of protected zone");
            }
        }
    }
}
