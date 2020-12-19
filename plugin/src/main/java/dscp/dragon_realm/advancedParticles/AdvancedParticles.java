package dscp.dragon_realm.advancedParticles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdvancedParticles {

    public static void particleCircle(Location center, Particle particle, double radius, int amount){
        particleCircle(center, particle, radius, amount, 0);
    }

    public static void particleCircle(Location center, Particle particle, double radius, int amount, int extra){
        World world = center.getWorld();
        assert world != null;
        ArrayList<Location> circle = getCircle(center, radius, amount);
        for(Location loc : circle){
            world.spawnParticle(particle, loc, 1, 0, 0, 0, extra);
        }
    }

    public static void particleCircle(Player player, Particle particle, double radius, int amount){
        particleCircle(player, particle, radius, amount, 0);
    }

    public static void particleCircle(Player player, Particle particle, double radius, int amount, int extra){
        Location center = player.getLocation().add(0, 0.5, 0);
        particleCircle(center, particle, radius, amount, extra);
    }

    public static void particleSphere(Location center, Particle particle, double radius, int amount){
        particleSphere(center, particle, radius, amount, 0);
    }

    public static void particleSphere(Location center, Particle particle, double radius, int amount, int extra){
        World world = center.getWorld();
        assert world != null;
        ArrayList<Location> sphere = getSphere(center, radius, amount);
        for(Location loc : sphere){
            world.spawnParticle(particle, loc, 1, 0, 0, 0, extra);
        }
    }

    public static void particleSphere(Player player, Particle particle, double radius, int amount){
        particleSphere(player, particle, radius, amount, 0);
    }

    public static void particleSphere(Player player, Particle particle, double radius, int amount, int extra){
        Location center = player.getLocation().add(0, 0.5, 0);
        particleSphere(center, particle, radius, amount, extra);
    }

    public static ArrayList<Location> getCircle(Location center, double radius, int amount){
        if(amount <= 0) throw new IllegalArgumentException("amount must be strictly positive");
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for(int i = 0 ; i < amount ; i++){
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    public static ArrayList<Location> getSphere(Location center, double radius, int amount){
        if(amount <= 0) throw new IllegalArgumentException("amount must be strictly positive");
        World world = center.getWorld();
        double x = center.getX(), z = center.getZ();
        double step = (radius * 2) / amount;
        double startY = center.getY() - radius;
        ArrayList<Location> locations = new ArrayList<>();
        for(double i = startY ; i <= center.getY() + radius ; i = i + step){
            double h = Math.abs(center.getY() - i);
            double r = Math.sqrt((radius * radius) - (h * h)); //Pythagoras
            ArrayList<Location> circle = getCircle(new Location(world, x, i, z), r, amount);
            locations.addAll(circle);
        }
        return locations;
    }
}
