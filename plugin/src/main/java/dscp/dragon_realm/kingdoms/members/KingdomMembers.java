package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * class for kingdom members
 */
public class KingdomMembers implements Serializable {
    private static final long serialVersionUID = 7680294706878414282L;

    Kingdom kingdom;
    Map<UUID, KingdomMember> members;

    /**
     * constructor for the KingdomMembers class
     * @param kingdom
     * the kingdom that the members are in
     */
    public KingdomMembers(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");

        this.kingdom = kingdom;
        this.members = new HashMap<>();
    }

    //getters

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Map<UUID, KingdomMember> getMembers() {
        return members;
    }

    // add member to members

    /**
     * add a member to the members of the kingdom
     * @param playerUUID
     * the UUID of the player that will be added as a KingdomMember to the members
     * @return the KingdomMember of the given player, null if member already in members
     */
    public KingdomMember addMember(UUID playerUUID){
        if(playerUUID == null) throw new IllegalArgumentException("player can't be null");
        if(members.containsKey(playerUUID)) return null;
        KingdomMember newMember = new KingdomMember(this.kingdom, playerUUID);
        members.put(playerUUID, newMember);
        return newMember;
    }

    /**
     * add a member to the members of the kingdom
     * @param playerUUID
     * the UUID of the player that will be added as a KingdomMember to the members
     * @param rank
     * the rank the member will receive
     * @return the KingdomMember of the given player, null if member already in members
     */
    public KingdomMember addMember(UUID playerUUID, KingdomMemberRank rank){
        if(playerUUID == null) throw new IllegalArgumentException("player can't be null");
        if(members.containsKey(playerUUID)) return null;
        KingdomMember newMember = new KingdomMember(this.kingdom, playerUUID, rank);
        members.put(playerUUID, newMember);
        return newMember;
    }

    /**
     * add a member to the members of a kingdom
     * @param kingdom
     * the kingdom teh member will be added to
     * @param playerUUID
     * the UUID of the player that will be added as a KingdomMember to the members
     * @return the KingdomMember of the given player, null is member already in member
     */
    public static KingdomMember addMember(Kingdom kingdom, UUID playerUUID){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().addMember(playerUUID);
    }

    /**
     * add a member to the members of a kingdom
     * @param kingdom
     * the kingdom teh member will be added to
     * @param playerUUID
     * the UUID of the player that will be added as a KingdomMember to the members
     * @param rank
     * the rank the member will receive
     * @return the KingdomMember of the given player, null if member is already in members
     */
    public static KingdomMember addMember(Kingdom kingdom, UUID playerUUID, KingdomMemberRank rank){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().addMember(playerUUID, rank);
    }

    // remove member from members

    /**
     * remove a member from the members
     * @param player
     * the member that will be removed
     * @return
     * the removed member, null if member not in members
     */
    public KingdomMember removeMember(OfflinePlayer player){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        return members.remove(player.getUniqueId());
    }

    /**
     * remove a member from the members of the kingdom
     * @param kingdom
     * the kingdom where the member will be removed
     * @param player
     * the member that will be removed
     * @return
     * the removed member, null if member not in members
     */
    public static KingdomMember removeMember(Kingdom kingdom, OfflinePlayer player){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().removeMember(player);
    }

    // get member

    /**
     * get the member from a player
     * @param player
     * the player that the member contains
     * @return the member of the player, null if player not member of kingdom
     */
    public KingdomMember getMember(OfflinePlayer player){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        return members.get(player.getUniqueId());
    }

    /**
     * get the member from a player
     * @param kingdom
     * the kingdom that will be searched
     * @param player
     * the player that the member contains
     * @return the member of the player, null if player not member of kingdom
     */
    public static KingdomMember getMember(Kingdom kingdom, OfflinePlayer player){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().removeMember(player);
    }

    // is member of kingdom?

    /**
     * check if the given player is a member of the kingdom
     * @param player
     * the player the be checked
     * @return true if player is member, false if not
     */
    public boolean isMemberOfKingdom(OfflinePlayer player){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        return members.containsKey(player.getUniqueId());
    }

    /**
     * check if the given player is a member of a kingdom
     * @param kingdom
     * the kingdom to check
     * @param player
     * the player the be checked
     * @return true if player is member, false if not
     */
    public static boolean isMemberOfKingdom(Kingdom kingdom, OfflinePlayer player){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().isMemberOfKingdom(player);
    }

    // get members with rank

    /**
     * get the king of the kingdom
     * @return the KingdomMember that is king of the kingdom
     * @throws KingdomException
     * throws if the kingdom has no king
     */
    public KingdomMember getKing() throws KingdomException {
        for(Map.Entry<UUID, KingdomMember> entry : members.entrySet()){
            if(entry.getValue().hasPermission(KingdomMemberRank.KING)) return entry.getValue();
        }
        throw new KingdomException("a kingdom must have a king at all times");
    }

    /**
     * get all the members with a given rank in the kingdom
     * @param rankNeeded the rank the members need
     * @return a arraylist of all the members that have the rank
     */
    public ArrayList<KingdomMember> getMembersWithRank(KingdomMemberRank rankNeeded){
        ArrayList<KingdomMember> membersWithRank = new ArrayList<>();
        for(Map.Entry<UUID, KingdomMember> entry : members.entrySet()){
            if(entry.getValue().getRank() == rankNeeded) membersWithRank.add(entry.getValue());
        }
        return membersWithRank;
    }

}
