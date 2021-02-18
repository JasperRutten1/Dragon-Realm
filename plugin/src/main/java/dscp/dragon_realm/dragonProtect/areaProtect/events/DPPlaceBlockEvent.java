package dscp.dragon_realm.dragonProtect.areaProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class
DPPlaceBlockEvent implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedArea areaBlock = ProtectedArea.getArea(block.getChunk());
        ProtectedArea areaPlayer = ProtectedArea.getArea(player.getLocation().getChunk());

        if(areaBlock == null && areaPlayer == null) return;
        if(DragonProtect.isInEditMode(player)) return;
        event.setCancelled(true);
        if(player.hasPermission("dscp.dp.edit"))
            DragonProtect.sendMessage(player, "you must be in edit mode to place a block");
        else DragonProtect.sendMessage(player, "you do not have permission to place blocks here");
    }
}
