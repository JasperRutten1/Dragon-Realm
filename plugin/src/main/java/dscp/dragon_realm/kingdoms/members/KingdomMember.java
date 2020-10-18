package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.members.ranks.KingdomMemberRank;
import org.bukkit.OfflinePlayer;

public class KingdomMember {
    Kingdom kingdom;
    KingdomMemberRank rank;
    OfflinePlayer player;

    //constructors

    /**
     * constructor for the KingdomMember class
     * @param kingdom
     * the kingdom the player is in
     * @param player
     * the player that is part of the kingdom
     */
    public KingdomMember(Kingdom kingdom, OfflinePlayer player){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        if(player == null) throw new IllegalArgumentException("player can't be nul");

        this.kingdom = kingdom;
        this.player = player;
        this.rank = KingdomMemberRank.PEASANT;
    }

    /**
     * constructor for the KingdomMember class
     * @param kingdom
     * the kingdom the player is in
     * @param player
     * the player that is part of the kingdom
     * @param rank
     * the rank the member will get on creation
     */
    public KingdomMember(Kingdom kingdom, OfflinePlayer player, KingdomMemberRank rank){
        this(kingdom, player);

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

    public OfflinePlayer getPlayer() {
        return player;
    }


}

