package dscp.dragon_realm.specialWeapons.spiritSwords.events;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SSRespawnEvent implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if(SpiritSword.playerInSoulBindMap(player.getUniqueId())){
            for(SpiritSword ss : SpiritSword.getSpiritSwordFromSoulBindMap(player.getUniqueId())){
                SpiritSword.giveSpiritSword(ss, player);
            }
            SpiritSword.clearPlayerSoulBindMap(player.getUniqueId());
        }
    }
}
