package dscp.dragon_realm.kingdoms;

import dscp.dragon_realm.ObjectIO;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.kingdoms.members.KingdomMembers;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class Kingdom implements Serializable {
    private static final long serialVersionUID = -7307977757795245408L;

    public static List<Kingdom> kingdoms;

    private KingdomMembers members;
    private String name;
    private KingdomClaim claim;

    public Kingdom(String name, Player king){
        if(name == null) throw new IllegalArgumentException("name can't be null");
        if(king == null) throw new IllegalArgumentException("king can't be null");

        this.name = name;
        this.members = new KingdomMembers(this);
        this.members.addMember(king.getUniqueId(), KingdomMemberRank.KING);
        this.claim = new KingdomClaim(this);
    }

    //getters

    public String getName() {
        return name;
    }

    public KingdomMembers getMembers() {
        return members;
    }

    public KingdomClaim getClaim() {
        return claim;
    }

    //create and remove kingdom

    /**
     * create a new kingdom object and add it to the static kingdoms array list
     * @param name the name of the new kingdom
     * @param king the player that will be king of the new kingdom
     * @return the new kingdom object
     * @throws KingdomException thrown if kingdom with this name already exists or if player is already part of a kingdom
     */
    public static Kingdom createKingdom(String name, Player king) throws KingdomException {
        for(Kingdom kingdom : kingdoms){
            if(kingdom.getName().equals(name)){
                throw new KingdomException("a kingdom with this name already exists");
            }
            if(kingdom.getMembers().isMemberOfKingdom(king)){
                throw new KingdomException("you are already part of a kingdom");
            }
        }
        Kingdom kingdom = new Kingdom(name, king);
        king.sendMessage(ChatColor.GOLD + "created kingdom with name " + ChatColor.DARK_PURPLE + kingdom.getName());
        kingdoms.add(kingdom);
        return kingdom;
    }

    /**
     * remove a kingdom from the static kingdoms array list
     * @param player the player that is king of the kingdom
     * @return the removed kingdom
     * @throws KingdomException thrown if member is not part of a kingdom or if member isn't the king of his kingdom
     */
    public static Kingdom removeKingdom(Player player) throws KingdomException {
        if (player == null) throw new IllegalArgumentException("player");

        Kingdom kingdom = getKingdomFromPlayer(player);
        if(kingdom == null) throw new KingdomException("you are not part of a kingdom");
        KingdomMember member = kingdom.getMembers().getMember(player);
        if(member == null) throw new KingdomException("you are not part of a kingdom");
        if(member.equals(kingdom.getMembers().getKing())) throw new KingdomException("only the king of the kingdom can remove the kingdom");

        kingdoms.remove(kingdom);
        player.sendMessage(ChatColor.GREEN + "Removed kingdom");
        return kingdom;
    }

    // members

    /**
     * get the kingdom a player is in
     * @param player the player of witch you want the kingdom of
     * @return the kingdom the player is in, null if no kingdom found
     */
    public static Kingdom getKingdomFromPlayer(OfflinePlayer player){
        for(Kingdom kd : kingdoms){
            if(kd.getMembers().isMemberOfKingdom(player)) return kd;
        }
        return null;
    }

    // claims


    //load in kingdom

    /**
     * loads the kingdoms in a directory from the data files inside
     * @param dir the directory containing the kingdom data files
     * @return the ArrayList with all the kingdoms
     */
    public static ArrayList<Kingdom> loadKingdoms(File dir){
        if(dir == null) throw new IllegalArgumentException("dir can't be null");
        ArrayList<Kingdom> kingdoms = new ArrayList<>();
        Kingdom.kingdoms = kingdoms;
        try{
            if(!dir.exists() || !dir.isDirectory()){
                dir.mkdir();
            }
            if(Objects.requireNonNull(dir.listFiles()).length == 0){
                System.out.println("no kingdom files found");
                return kingdoms;
            }
            System.out.println("loading in kingdoms:");
            for(File kingdomFile : Objects.requireNonNull(dir.listFiles())){
                Object object = ObjectIO.loadObjectFromFile(kingdomFile);
                if(object instanceof Kingdom){
                    Kingdom kingdom = (Kingdom) object;
                    kingdoms.add(kingdom);
                    System.out.println("kingdom '" + kingdom.getName() + "' loaded");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("exception in loading in kingdoms");
        }
        Kingdom.kingdoms = kingdoms;
        return kingdoms;
    }

    //save kingdoms

    /**
     * saves all the kingdom objects to a directory in separated data files
     * @param dir the dir where the kingdom objects will be saved
     */
    public static void SaveKingdoms(File dir){
        if(Kingdom.kingdoms == null || Kingdom.kingdoms.size() == 0){
            System.out.println("no kingdoms to save");
            return;
        }
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        for(Kingdom kingdom : Kingdom.kingdoms){
            File kingdomFile = new File(dir, kingdom.getName() + ".dat");
            ObjectIO.writeObjectToFile(kingdomFile, kingdom);
        }
    }

    // equals and to hash override


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kingdom)) return false;
        Kingdom kingdom = (Kingdom) o;
        return Objects.equals(name, kingdom.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
