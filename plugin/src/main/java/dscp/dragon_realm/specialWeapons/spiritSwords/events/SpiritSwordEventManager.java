package dscp.dragon_realm.specialWeapons.spiritSwords.events;

import dscp.dragon_realm.Dragon_Realm;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class SpiritSwordEventManager {

    public static void registerEvents(){
        List<Listener> events = new ArrayList<>();

        events.add(new SSDamageEvent());
        events.add(new SSPlayerDeathEvent());
        events.add(new SSRespawnEvent());

        for(Listener event : events){
            Dragon_Realm.instance.getServer().getPluginManager().registerEvents(event, Dragon_Realm.instance);
        }
    }
}
