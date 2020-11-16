package dscp.dragon_realm.kingdoms;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.ObjectIO;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.kingdoms.members.KingdomMembers;
import dscp.dragon_realm.kingdoms.vault.KingdomVault;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 *
 */
public class Kingdom implements Serializable {
    private static final long serialVersionUID = -7307977757795245408L;

    public static List<Kingdom> kingdoms;
    public static List<Kingdom> removedKingdoms = new ArrayList<>();

    private KingdomMembers members;
    private String name;
    private KingdomClaim claim;
    private KingdomVault vault;

    private final Map<UUID, Long> joinInvitations = new HashMap<>();

    public Kingdom(String name, Player king){
        if(name == null) throw new IllegalArgumentException("name can't be null");
        if(king == null) throw new IllegalArgumentException("king can't be null");

        this.name = Dragon_Realm_API.capitalizeFirstLetter(name);
        this.members = new KingdomMembers(this);
        this.members.addMember(king.getUniqueId(), KingdomMemberRank.KING);
        this.claim = new KingdomClaim(this);
        this.vault = new KingdomVault(this);
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

    public Map<UUID, Long> getJoinInvitations() {
        return joinInvitations;
    }

    public KingdomVault getVault() {
        return vault;
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
        if(!kingdom.getMembers().getKing().equals(member)) throw new KingdomException("only the king of the kingdom can remove the kingdom");

        kingdoms.remove(kingdom);
        removedKingdoms.add(kingdom);
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

    /**
     * checks if a player (member) is part of a kingdom
     * @param player the player that will be checked
     * @return true is player is a member of a kingdom, false if not
     */
    public static boolean isMemberOfKingdom(Player player){
        for(Kingdom kingdom : kingdoms){
            if(kingdom.getMembers().isMemberOfKingdom(player)) return true;
        }
        return false;
    }

    /**
     * adds a member to the members class of this kingdom
     * @param player the player that will be added as a KingdomMember to the members class
     * @return the added KingdomMember
     * @throws KingdomException thrown if the player is already part of a kingdom
     */
    public KingdomMember addMemberToKingdom(Player player) throws KingdomException {
        if(player == null) throw new IllegalArgumentException("player can't be null");
        if(isMemberOfKingdom(player)) throw new KingdomException("player is already part of a kingdom");

        return members.addMember(player.getUniqueId());
    }

    /**
     * remove a KingdomMember from the members class
     * @param player the player (OfflinePlayer) that will be removed from the members class
     * @return the KingdomMember that got removed from the members class
     * @throws KingdomException thrown if the member is not part of this kingdom
     */
    public KingdomMember removeKingdomMember(OfflinePlayer player) throws KingdomException {
        if(player == null) throw new IllegalArgumentException("player ca,'t be null");
        if(members.isMemberOfKingdom(player)) throw new KingdomException("player is not part of this kingdom");

        return members.removeMember(player);
    }

    /**
     * sends a message to all online members of this kingdom
     * @param message the message that will be send to all the kingdom members
     */
    public void sendMembersMessage(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(members.isMemberOfKingdom(player)){
                player.sendMessage(message);
            }
        }
    }

    public void invitePlayerToKingdom(Player inviteSender, Player inviteReceiver){
        if(inviteSender == null) throw new IllegalArgumentException("sender can't be null");
        if(inviteReceiver == null) throw new IllegalArgumentException("receiver can't be null");

        inviteReceiver.sendMessage(ChatColor.GREEN + "you have been invited by "
                + ChatColor.DARK_AQUA + inviteSender.getName() + ChatColor.GREEN + " to join the " +
                ChatColor.GOLD + this.name + ChatColor.GREEN + " kingdom");

        TextComponent clickToAcceptMessage = new TextComponent(ChatColor.BLUE + "click here to accept");
        TextComponent hoverText = new TextComponent(ChatColor.GREEN + "click to join the " + ChatColor.GOLD + this.name + ChatColor.GREEN + " kingdom");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{hoverText});
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kingdom acceptinvite " + this.name);
        clickToAcceptMessage.setHoverEvent(hoverEvent);
        clickToAcceptMessage.setClickEvent(clickEvent);
        inviteReceiver.spigot().sendMessage(clickToAcceptMessage);

        joinInvitations.put(inviteReceiver.getUniqueId(), System.currentTimeMillis() + 60000);
    }

    /**
     * adds a member to the kingdom if the member is invited to it
     * @param player the player that will join the kingdom
     * @throws KingdomException thrown if the player is not invited or the player is already part of a kingdom
     */
    public void inviteAcceptation(Player player) throws KingdomException {
        if(!joinInvitations.containsKey(player.getUniqueId())) throw new KingdomException("you where not invited to join this kingdom");
        if(joinInvitations.get(player.getUniqueId()) < System.currentTimeMillis()){
            joinInvitations.remove(player.getUniqueId());
            throw new KingdomException("you are not invited to this kingdom");
        }
        if(Kingdom.isMemberOfKingdom(player)) throw new KingdomException("you are already part of a kingdom");

        members.addMember(player.getUniqueId());
        joinInvitations.remove(player.getUniqueId());
    }

    // claims


    // misc
    public static Kingdom getKingdomFromName(String name){
        for(Kingdom kingdom : kingdoms){
            if(kingdom.name.toLowerCase().equals(name.toLowerCase())) return kingdom;
        }
        return null;
    }

    public static boolean kingdomExists(String name){
        return !(getKingdomFromName(name) == null);
    }

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
    public static void saveKingdoms(File dir){
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

    // move removed kingdom files

    public static void moveRemovedKingdoms(File kingdomsDir, File removedDir){
        if(Kingdom.removedKingdoms == null || Kingdom.removedKingdoms.size() == 0){
            return;
        }
        if(!kingdomsDir.exists() || !kingdomsDir.isDirectory()){
            kingdomsDir.mkdir();
        }
        if(!removedDir.exists() || !removedDir.isDirectory()){
            removedDir.mkdir();
        }
        for(File file : Objects.requireNonNull(kingdomsDir.listFiles())){
            for(Kingdom removedKingdom : removedKingdoms){
                if(file.getName().equals(removedKingdom.name + ".dat")){
                    file.delete();
                    File kingdomFile = new File(removedDir, removedKingdom.getName() + ".dat");
                    ObjectIO.writeObjectToFile(kingdomFile, removedKingdom);
                }
            }
        }
        removedKingdoms.clear();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append("\n")
                .append("King: ").append(getMembers().getKing().getPlayer().getName()).append("\n");
        sb.append("Claimed chunks: ").append(getClaim().size()).append("\n");
        if(getClaim().getSettlements().size() > 0){
            sb.append("Settlements: \n");
            if(getClaim().getCapital() != null){
                sb.append(" - ").append(getClaim().getCapital().getName()).append(", Capital \n");
            }
            for(Settlement settlement : getClaim().getSettlements()){
                if(!settlement.isCapital()){
                    sb.append(" - ").append(settlement.getName()).append(", ")
                            .append(settlement.getLevel().getName()).append("\n");
                }
            }
        }

        return sb.toString();
    }
}
