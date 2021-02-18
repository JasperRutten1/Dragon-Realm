package dscp.dragon_realm.dragonProtect.areaProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class DPExplosionEvent implements Listener {
    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event){
        Block block = event.getBlock();
        ProtectedArea area = ProtectedArea.getArea(block.getChunk());
        if(!(area == null)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        ProtectedArea area = ProtectedArea.getArea(entity.getLocation().getChunk());
        if(!(area == null)) event.setCancelled(true);
    }
}
