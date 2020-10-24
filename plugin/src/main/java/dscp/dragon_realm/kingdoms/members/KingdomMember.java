package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
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
        if(playerUUID == null) throw new IllegalArgumentException("player can't be nul");

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

