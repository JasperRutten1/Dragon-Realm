package dscp.dragon_realm.dragonProtect.events;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
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
        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pz = dp.getZone(block.getChunk());

        if(!(pz == null)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event){
        Entity entity = event.getEntity();
        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pz = dp.getZone(entity.getLocation().getChunk());

        if(!(pz == null)) event.setCancelled(true);
    }
}
