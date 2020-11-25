package dscp.dragon_realm.dragonProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class
DPPlaceBlockEvent implements Listener {
    private DragonProtect dragonProtect;

    public DPPlaceBlockEvent(DragonProtect dragonProtect){
        this.dragonProtect = dragonProtect;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pzBlock = dp.getZone(block.getChunk());
        ProtectedZone pzPlayer = dp.getZone(player.getLocation().getChunk());

        if(pzBlock == null && pzPlayer == null) return;
        if(dp.isInEditMode(player)) return;
        event.setCancelled(true);
        if(player.hasPermission("dscp.dp.edit"))
            DragonProtect.sendMessage(player, "you must be in edit mode to place a block");
        else DragonProtect.sendMessage(player, "you do not have permission to place blocks here");
    }
}
