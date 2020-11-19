package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class KingdomMember implements Serializable {
    private static final long serialVersionUID = -2035948641377905712L;

    Kingdom kingdom;
    KingdomMemberRank rank;
    UUID playerUUID;

    //constructors

    /**
     * constructor for the KingdomMember class
     * @param kingdom
     * the kingdom the player is in
     * @param playerUUID
     * the UUID of the player that is part of the kingdom
     */
    public KingdomMember(Kingdom kingdom, UUID playerUUID){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        if(playerUUID == null) throw new IllegalArgumentException("playerID can't be nul");

        this.kingdom = kingdom;
        this.playerUUID = playerUUID;
        this.rank = KingdomMemberRank.PEASANT;
    }

    /**
     * constructor for the KingdomMember class
     * @param kingdom
     * the kingdom the player is in
     * @param playerUUID
     * the UUID of the player that is part of the kingdom
     * @param rank
     * the rank the member will get on creation
     */
    public KingdomMember(Kingdom kingdom, UUID playerUUID, KingdomMemberRank rank){
        this(kingdom, playerUUID);

        if(rank == null) throw new IllegalArgumentException("rank can't be null");
        this.rank = rank;
    }

    // getters

    public Kingdom getKingdom() {
        return kingdom;
    }

    public KingdomMemberRank getRank() {
        return rank;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    // get player

    public OfflinePlayer getPlayer(){
        return Bukkit.getOfflinePlayer(this.playerUUID);
    }

    // ranks and permission

    public boolean hasPermission(KingdomMemberRank neededRank){
        return this.rank.getRankValue() >= neededRank.getRankValue();
    }

    public void promoteMember() throws KingdomException {
        if(this.rank.isHighestRank()) throw new KingdomException("member already has the highest rank");
        if(this.rank.getHigherRank() == KingdomMemberRank.ROYAL
                && kingdom.getMembers().getMembersWithRankExact(KingdomMemberRank.ROYAL).size() >= 2)
            throw new KingdomException("a kingdom can only have 2 members with the rank of royal");
        if(this.rank.getHigherRank() == KingdomMemberRank.NOBEL
                && kingdom.getMembers().getMembersWithRankExact(KingdomMemberRank.NOBEL).size() >= 3)
            throw new KingdomException("a kingdom can only have 3 members with the rank of nobel");
        this.rank = this.rank.getHigherRank();
    }

    public void demoteMember() throws KingdomException {
        if(this.rank.getLowerRank() == KingdomMemberRank.NOBEL
                && kingdom.getMembers().getMembersWithRankExact(KingdomMemberRank.NOBEL).size() >= 3)
            throw new KingdomException("a kingdom can only have 3 members with the rank of nobel");
        if(this.rank.isLowestRank()) throw new KingdomException("member is already at the lowest rank");
        this.rank = this.rank.getLowerRank();
    }

    // equals override


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KingdomMember)) return false;
        KingdomMember that = (KingdomMember) o;
        return kingdom.equals(that.kingdom) &&
                playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingdom, playerUUID);
    }
}

