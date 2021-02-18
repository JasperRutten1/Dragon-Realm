package dscp.dragon_realm.dragonProtect.areaProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DPBreakBlockEvent implements Listener {

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ProtectedArea area = ProtectedArea.getArea(block.getChunk());

        if(area != null && !DragonProtect.isInEditMode(player)){
            event.setCancelled(true);
            if(player.hasPermission("dscp.dp.edit")){
                DragonProtect.sendMessage(player, "you must be in edit mode to break blocks");
            }
            else{
                DragonProtect.sendMessage(player, "you can not break blocks here");
            }
            return;
        }
    }
}
