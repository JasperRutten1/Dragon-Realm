package dscp.dragon_realm.kingdoms.members.ranks;

public enum KingdomMemberRank {
    KING(6),
    ROYAL(5),
    NOBEL(4),
    KNIGHT(3),
    SQUIRE(2),
    FOOTMEN(1),
    PEASANT(0);

    int rankValue;

    KingdomMemberRank(int rankValue){
        this.rankValue = rankValue;
    }

    public int getRankValue() {
        return rankValue;
    }

    public KingdomMemberRank getRankFromValue(int value){
        for(KingdomMemberRank rank : values()){
            if(rank.rankValue == value) return rank;
        }
        return null;
    }
}
