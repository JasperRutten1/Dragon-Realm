package dscp.dragon_realm;

import dscp.dragon_realm.kingdoms.claims.ChunkCoordinates;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class Dragon_Realm_API {

    /**
     * turns a Location in to a string containing the coordinates of the Location
     * @param location
     * the Location object that will be turned in to a string
     * @return
     * a string displaying the coordinates of the location
     */
    public static String LocationToString(Location location){
        StringBuilder sb = new StringBuilder();
        sb.append("( ").append(location.getBlockX()).append(", ").append(location.getBlockY()).append(", ").append(location.getBlockZ()).append(" )");
        return sb.toString();
    }

    public static Object load(File f){
        try{
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(f)));
            Object result = in.readObject();
            in.close();
            return result;
        }
        catch (Exception e){
            return null;
        }
    }

    public static void save(Object o, File f){
        try{
            try{
                if(f.exists()){
                    f.createNewFile();
                }
            }
            catch (IOException e){
                e.printStackTrace();
                System.out.println("error in creating new file");
            }
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
            out.writeObject(o);
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int distanceBetweenChunks(ChunkCoordinates chunk1, ChunkCoordinates chunk2){
        return (int) Math.max(Math.abs(chunk1.getX() - chunk2.getX()),Math.abs(chunk1.getZ() - chunk2.getZ()));
    }

    public static void giveItem(Player player, ItemStack item){
        Map<Integer, ItemStack> leftOver = player.getInventory().addItem(item);
        for(Map.Entry<Integer, ItemStack> entry : leftOver.entrySet()){
            player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
        }
    }

    public static void consoleLog(String string){
        System.out.println("[Dragon_Realm] " + string);
    }

    public static Player getPlayerFromName(String name){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getName().equals(name)) return player;
        }
        return null;
    }
}