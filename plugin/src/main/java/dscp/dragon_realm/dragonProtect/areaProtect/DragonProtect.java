package dscp.dragon_realm.dragonProtect.areaProtect;


import dscp.dragon_realm.Dragon_Realm;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.Serializable;
import java.util.*;

public class DragonProtect implements Serializable {
    private static final long serialVersionUID = 5186907977977093105L;

    private static HashMap<UUID, Boolean> editModeList = new HashMap<>();

    public static void onEnable(){
        System.out.println("loading protected areas");
        ProtectedArea.loadAreas();
    }

    public static void onDisable(){
        System.out.println("disabling dragon protect, saving data");
        ProtectedArea.saveAreas();
    }

    public static void scheduleTasks(){
        BukkitScheduler scheduler = Dragon_Realm.instance.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Dragon_Realm.instance, new DragonProtectSave(), 20L, DragonProtectSave.repDelay);
    }

    private static class DragonProtectSave implements Runnable{
        private static long repDelay = 60L;

        @Override
        public void run() {
            ProtectedArea.saveAreas();
        }
    }

    public static void sendMessage(Player player, String message){
        player.sendMessage(ChatColor.DARK_PURPLE + "[DP]" + ChatColor.RESET + message);
    }

    public static HashMap<UUID, Boolean> getEditModeList() {
        return editModeList;
    }

    public static boolean isInEditMode(Player player){
        return editModeList.getOrDefault(player.getUniqueId(), false);
    }
}
