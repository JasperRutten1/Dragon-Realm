package dscp.dragon_realm.dragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.ObjectIO;
import dscp.dragon_realm.dragonProtect.events.DPBreakBlockEvent;
import dscp.dragon_realm.dragonProtect.events.DPPlaceBlockEvent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class DragonProtect implements Serializable {
    private static final long serialVersionUID = 5186907977977093105L;

    private List<ProtectedZone> zones;
    private static List<Listener> events = getEventsList();
    private static List<UUID> inEditMode = new ArrayList<>();

    /**
     * constructor for DragonProtect objects.
     */
    public DragonProtect(){
        this.zones = new ArrayList<>();
        System.out.println("created new Dragon Protect Object");
    }

    //gui

    /**
     * send a message to a player with the dragon protect prefix
     * @param player the player to send the message to
     * @param message the message to send to the player
     */
    public static void sendMessage(Player player, String message){
        player.sendMessage(ChatColor.DARK_PURPLE + "[Dragon Protect] " + ChatColor.RESET + message);
    }

    //zones

    /**
     * checks if a zone with a given name exists.
     * @param name the name of the zone to check for
     * @return true if a zone with the given name exists, false if not
     */
    public boolean zoneExists(String name){
        for(ProtectedZone zone : zones){
            if(zone.getName().equals(Dragon_Realm_API.capitalizeFirstLetter(name))) return true;
        }
        return false;
    }

    /**
     * get the zone with a given name
     * @param name the name of the zone that will be looked for
     * @return the zone with given name, null if no zone was found
     */
    public ProtectedZone getZone(String name){
        for(ProtectedZone zone : zones){
            if(zone.getName().equals(Dragon_Realm_API.capitalizeFirstLetter(name))) return zone;
        }
        return null;
    }

    /**
     * get the zone a given chunk is in
     * @param chunk the chunk to get the zone for
     * @return the zone the given chunk is in, null if not in a zone
     */
    public ProtectedZone getZone(Chunk chunk){
        for(ProtectedZone zone : zones){
            if(zone.coversChunk(chunk)) return zone;
        }
        return null;
    }

    /**
     * check if a given zone already exists.
     * @param zone the zone to check
     * @return true if the zone already exists, false if not
     */
    public boolean zoneExists(ProtectedZone zone){
        return zones.contains(zone);
    }

    /**
     * create a new protected zone with a given name
     * @param name the name for this new protected zone
     * @return the protected zone that got created
     */
    public ProtectedZone createNewZone(String name){
        if(zoneExists(name)) return null;
        ProtectedZone zone = new ProtectedZone(name);
        zones.add(zone);
        return zone;
    }

    /**
     * remove a zone with a given name
     * @param name the nam of the zone that will be removed
     * @return the zone that got removed, null if no zone was removed
     */
    public ProtectedZone removeZone(String name){
        ProtectedZone zone = getZone(name);
        if(zone == null) return null;
        zones.remove(zone);
        return zone;
    }

    /**
     * create a list of all the zones in string format
     * @return a string with all the zones
     */
    public String listZones(){
        StringBuilder sb = new StringBuilder();
        for(ProtectedZone zone : zones){
            sb.append(zone.getName()).append("\n");
        }
        return sb.toString();
    }

    //edit mode

    /**
     * toggle edit mode for a player
     * @param player the player where the edit mode will be toggled
     */
    public void toggleEditMode(Player player){
        UUID uuid = player.getUniqueId();
        if(inEditMode.contains(uuid)){
            inEditMode.remove(uuid);
        }
        else inEditMode.add(uuid);
    }

    /**
     * check if a player is in edit mode
     * @param player the player to check
     * @return true if the player is in edit mode, false if not
     */
    public boolean isInEditMode(Player player){
        return inEditMode.contains(player.getUniqueId());
    }

    // events

    /**
     * get a list af all the events related to dragon protect
     * @return a list of events related to dragon protect
     */
    private static List<Listener> getEventsList(){
        ArrayList<Listener> events = new ArrayList<>();
        DragonProtect dp = Dragon_Realm.dragonProtect;

        events.add(new DPBreakBlockEvent(dp));
        events.add(new DPPlaceBlockEvent(dp));

        return events;
    }

    /**
     * register the events related to dragon protect
     */
    public static void registerEvents(){
        for(Listener event : events){
            Dragon_Realm.instance.getServer().getPluginManager().registerEvents(event, Dragon_Realm.instance);
        }
    }

    // load and save

    /**
     * load the dragon protect object from it's file, will create new object if file not found
     * @return the dragon protect object
     */
    public static DragonProtect load(){
        File dir = new File(Dragon_Realm.instance.getDataFolder(), "DragonProtect");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        File loadFile = new File(dir, "DragonProtect.dat");
        if(loadFile.exists()){
            return (DragonProtect) ObjectIO.loadObjectFromFile(loadFile);
        }
        else return new DragonProtect();
    }

    /**
     * save the dragon protect object to it's file
     */
    public static void save(){
        File dir = new File(Dragon_Realm.instance.getDataFolder(), "DragonProtect");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        File saveFile = new File(dir, "DragonProtect.dat");
        ObjectIO.writeObjectToFile(saveFile, Dragon_Realm.dragonProtect);
    }
}
