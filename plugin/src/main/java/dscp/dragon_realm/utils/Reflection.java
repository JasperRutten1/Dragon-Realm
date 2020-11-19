package dscp.dragon_realm.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for working with reflected code. This class and the reflection package both
 * act as a reflection api on top of some commonly used NMS and OBC classes.
 */
public class Reflection {

    private static Map<UUID, Object> playerConnections = new HashMap<>();

    private static Method sendPacket = null;

    public static String version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

    /**
     * Initiate the listener required to retrieve the player connection
     *
     * @return Listener
     */
    public static Listener init() {
        return new Listener() {

            @EventHandler
            public void on(PlayerJoinEvent e) {
                try {
                    Object entityPlayer = e.getPlayer().getClass().getMethod("getHandle").invoke(e.getPlayer());
                    Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                    playerConnections.put(e.getPlayer().getUniqueId(), playerConnection);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @EventHandler
            public void on(PlayerQuitEvent e) {
                try {
                    playerConnections.remove(e.getPlayer().getUniqueId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        };
    }

    // Locked constructor
    private Reflection() {
    }

    /**
     * Returns the NMS class object of the specified class name
     *
     * @param className Name of requested class
     * @return Class
     * @throws ClassNotFoundException when class cannot be found
     */
    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + version + "." + className);
    }

    /**
     * Returns the OBC class object of the specified class name
     *
     * @param className Name of requested class
     * @return Class
     * @throws ClassNotFoundException when class cannot be found
     */
    public static Class<?> getOBCClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + version + "." + className);
    }

    /**
     * Sends the specified registerPacket object to the specified player's playerconnection
     *
     * @param player Player receiving registerPacket
     * @param packet Packet to be sent
     * @throws Exception when an Exception occurs
     */
    public static void sendPacket(Player player, Object packet) throws Exception {
        if(!playerConnections.containsKey(player.getUniqueId())) {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            playerConnections.put(player.getUniqueId(), playerConnection);
        }
        if(sendPacket == null) {
            sendPacket = playerConnections.get(player.getUniqueId()).getClass().getMethod("sendPacket", getNMSClass("Packet"));
        }

        sendPacket.invoke(playerConnections.get(player.getUniqueId()), packet);
    }

    /**
     * Update the title of the currently opened GUI menu
     *
     * @param player Player
     * @param title Title
     */
    public static void updateWindowTitle(Player player, String title) {
        try {
            // Gather data
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object activeCont = entityPlayer.getClass().getField("activeContainer").get(entityPlayer);
            Object message = getNMSClass("ChatComponentText").getConstructor(String.class).newInstance(title);
            int activeContId = activeCont.getClass().getField("windowId").getInt(activeCont);

            // Create the packet
            Object packet = getNMSClass("PacketPlayOutOpenWindow")
                .getConstructor(
                    int.class,
                    String.class,
                    getNMSClass("IChatBaseComponent"),
                    int.class
                ).newInstance(
                    activeContId,
                    "minecraft:chest",
                    message,
                    player.getOpenInventory().getTopInventory().getSize()
                );

            // Send packet
            sendPacket(player, packet);

            // Update inventory
            entityPlayer.getClass().getMethod("updateInventory", getNMSClass("Container")).invoke(entityPlayer, activeCont);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
