package dscp.dragon_realm.kingdoms.members;

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

    public boolean isHighestRank(){
        for(KingdomMemberRank rank : values()){
            if(rank.getRankValue() > this.rankValue) return false;
        }
        return true;
    }

    public boolean isLowestRank(){
        return rankValue == 0;
    }

    public KingdomMemberRank getHigherRank(){
        if(isHighestRank()) return null;
        else return getRankFromValue(rankValue + 1);
    }

    public KingdomMemberRank getLowerRank(){
        if(isLowestRank()) return null;
        else return getRankFromValue(rankValue - 1);
    }
}
