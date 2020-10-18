package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * class for kingdom members
 */
public class KingdomMembers {
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

    // add member to members

    /**
     * add a member to the members of the kingdom
     * @param player
     * the player that will be added as a KingdomMember to the members
     * @return the KingdomMember of the given player, null if member already in members
     */
    public KingdomMember addMember(OfflinePlayer player){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        if(members.containsKey(player.getUniqueId())) return null;
        KingdomMember newMember = new KingdomMember(this.kingdom, player);
        members.put(player.getUniqueId(), newMember);
        return newMember;
    }

    /**
     * add a member to the members of the kingdom
     * @param player
     * the player that will be added as a KingdomMember to the members
     * @param rank
     * the rank the member will receive
     * @return the KingdomMember of the given player, null if member already in members
     */
    public KingdomMember addMember(OfflinePlayer player, KingdomMemberRank rank){
        if(player == null) throw new IllegalArgumentException("player can't be null");
        if(members.containsKey(player.getUniqueId())) return null;
        KingdomMember newMember = new KingdomMember(this.kingdom, player, rank);
        members.put(player.getUniqueId(), newMember);
        return newMember;
    }

    /**
     * add a member to the members of a kingdom
     * @param kingdom
     * the kingdom teh member will be added to
     * @param player
     * the player that will be added as a KingdomMember to the members
     * @return the KingdomMember of the given player, null is member already in member
     */
    public static KingdomMember addMember(Kingdom kingdom, OfflinePlayer player){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().addMember(player);
    }

    /**
     * add a member to the members of a kingdom
     * @param kingdom
     * the kingdom teh member will be added to
     * @param player
     * the player that will be added as a KingdomMember to the members
     * @param rank
     * the rank the member will receive
     * @return the KingdomMember of the given player, null if member is already in members
     */
    public static KingdomMember addMember(Kingdom kingdom, OfflinePlayer player, KingdomMemberRank rank){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return kingdom.getMembers().addMember(player, rank);
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

}
