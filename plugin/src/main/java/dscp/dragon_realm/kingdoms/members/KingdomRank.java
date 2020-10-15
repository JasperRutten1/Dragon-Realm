package dscp.dragon_realm.kingdoms.members;

import org.bukkit.ChatColor;

public enum KingdomRank {
    KING(6,  ChatColor.GOLD + "King"),
    NOBEL(5,  ChatColor.DARK_AQUA + "Nobel"),
    DUKE(4, ChatColor.AQUA + "Duke"),
    KNIGHT(3, ChatColor.WHITE + "Knight"),
    SQUIRE(2, ChatColor.WHITE + "Squire"),
    GUARD(1, ChatColor.WHITE + "Guard"),
    PEASANT(0, ChatColor.WHITE + "Peasant");

    private final int value;
    private final String name;

    private KingdomRank(int rank, String name){
        this.value = rank;
        this.name = name;
    }

    public int getValue(){
        return value;
    }

    public KingdomRank getRankHigher(){
        if(this == KING) return KING;
        return getRankFromValue(this.getValue() + 1);
    }
    public KingdomRank getRankLower(){
        if(this == PEASANT) return PEASANT;
        return getRankFromValue(this.getValue() - 1);
    }

    public KingdomRank getRankFromValue(int value){
        for(KingdomRank kr : values()){
            if(kr.value == value) return kr;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean hasRankOrHigher(KingdomRank neededRank){
        return this.value >= neededRank.getValue();
    }
}
