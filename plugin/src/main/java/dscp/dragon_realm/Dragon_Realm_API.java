package dscp.dragon_realm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
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

    public static double distanceBetweenBlocks(Location loc1, Location loc2){
        return loc1.distance(loc2);
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

    public static void spawnParticlesBetween(Location loc1, Location loc2, Particle particle, double multiplier, int count){
        if(loc1 == null) throw new IllegalArgumentException("location 1 can't be null");
        if(loc2 == null) throw new IllegalArgumentException("location 2 can't be null");
        if(loc1.distance(loc2) < 0.5) return;
        assert loc1.getWorld() != null;
        if(!loc1.getWorld().equals(loc2.getWorld())) throw new IllegalArgumentException("locations must be in the same world");
        Vector vector = loc2.toVector().subtract(loc1.toVector()).multiply(multiplier);

        for(Location loc = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc1.getZ()) ; loc1.distance(loc) < loc1.distance(loc2) ; loc.add(vector)){
            assert loc.getWorld() != null;
            loc.getWorld().spawnParticle(particle, loc, count, 0, 0, 0, 0);
        }
    }

    public static String capitalizeFirstLetter(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight(null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (Block block : sightBlock) sight.add(block.getLocation());
        for (Location location : sight) {
            for (Entity entity : entities) {
                if (Math.abs(entity.getLocation().getX() - location.getX()) < 1.3) {
                    if (Math.abs(entity.getLocation().getY() - location.getY()) < 1.5) {
                        if (Math.abs(entity.getLocation().getZ() - location.getZ()) < 1.3) {
                            return entity;
                        }
                    }
                }
            }
        }
        return null; //Return null if no entity was found
    }

    public static Block getNearestBlockInSight(Player player, int range, ArrayList<Material> filter){
        ArrayList<Block> lineOfSight = (ArrayList<Block>) player.getLineOfSight(null, range);

        for(Block block : lineOfSight){
            if(!filter.contains(block.getType())){
                return block;
            }
        }
        return null;
    }

    public static Block getNearestBlockInSight(Player player, int range){
        ArrayList<Material> materials = new ArrayList<>();
        materials.add(Material.AIR);
        return getNearestBlockInSight(player, range, materials);
    }
}
