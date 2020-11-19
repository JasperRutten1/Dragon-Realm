package dscp.dragon_realm.kingdoms.members;

public enum KingdomMemberRank {
    KING(6, "King", "&6"),
    ROYAL(5, "Royal", "&e"),
    NOBEL(4, "Nobel", "&b"),
    KNIGHT(3, "Knight", "&9"),
    SQUIRE(2,"Squire", "&a"),
    FOOTMEN(1, "Footmen", "&7"),
    PEASANT(0, "Peasant", "&f");

    int rankValue;
    String name;
    String color;

    KingdomMemberRank(int rankValue, String name, String color){
        this.rankValue = rankValue;
        this.name = name;
        this.color = color;
    }

    public int getRankValue() {
        return rankValue;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDisplayName(){
        return color + name;
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

    public static boolean canPromote(KingdomMember promoter, KingdomMember toPromote){
        if(promoter.equals(toPromote)) return false;
        if(toPromote.getRank().isHighestRank()) return false;
        if(promoter.getRank().getRankValue() < 3) return false;
        return promoter.getRank().getRankValue() > toPromote.getRank().getRankValue();
    }

    public static boolean canDemote(KingdomMember demoter, KingdomMember toDemote){
        if(demoter.equals(toDemote)) return false;
        if(toDemote.getRank().isLowestRank()) return false;
        if(demoter.getRank().getRankValue() < 3) return false;
        return demoter.getRank().getRankValue() > toDemote.getRank().getRankValue();
    }
}
