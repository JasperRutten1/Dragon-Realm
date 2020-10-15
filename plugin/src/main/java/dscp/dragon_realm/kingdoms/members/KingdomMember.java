package dscp.dragon_realm.kingdoms.members;

import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Objects;

public class KingdomMember implements Serializable {
    private static final long serialVersionUID = 9041565223205936921L;

    private Player player;
    private KingdomRank rank;

    // constructors

    public KingdomMember(Player player) throws KingdomException {
        this(player, KingdomRank.PEASANT);
    }

    public KingdomMember(Player player, KingdomRank rank) throws KingdomException {
        if(player == null) throw new KingdomException("player can't be null");
        this.player = player;
        this.rank = rank;
    }

    // getters

    public Player getPlayer() {
        return player;
    }
    public KingdomRank getRank() {
        return rank;
    }

    // ranks

    public boolean hasRankOrHigher(KingdomRank rank){
        return this.rank.hasRankOrHigher(rank);
    }

    public KingdomRank rankUp(){
        this.rank = this.rank.getRankHigher();
        return this.rank;
    }
    public KingdomRank rankDown(){
        this.rank = this.rank.getRankLower();
        return this.rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KingdomMember)) return false;
        KingdomMember member = (KingdomMember) o;
        return Objects.equals(player.getUniqueId(), member.player.getUniqueId()) &&
                rank == member.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, rank);
    }
}
