package dscp.dragon_realm.kingdoms.claims.settlements;

import lombok.Getter;
import org.bukkit.Material;

public enum SettlementLevel {
    Outpost(1, "outpost", Material.IRON_INGOT),
    Village(2, "village", Material.GOLD_INGOT),
    Town(3, "town", Material.DIAMOND),
    City(4, "city", Material.EMERALD);

    int level;
    String name;
    Material guiItem;

    SettlementLevel(int level, String name, Material guiItem){
        this.level = level;
        this.name = name;
        this.guiItem = guiItem;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public Material getGuiItem() {
        return guiItem;
    }

    public static SettlementLevel getHighestLevel(){
        SettlementLevel highest = null;
        for(SettlementLevel level : values()){
            if(highest == null) highest = level;
            else if(highest.getLevel() < level.getLevel()) highest = level;
        }
        return highest;
    }

    public static SettlementLevel getLevelFromInt(int level){
        for(SettlementLevel sl : values()){
            if(sl.level == level) return sl;
        }
        return null;
    }

    public SettlementLevel getNextLevel(){
        /**
        SettlementLevel newLevel = null;
        for(SettlementLevel level : values()){
            if(newLevel == null) newLevel = level;
            else if(level.getLevel() > this.getLevel())
                newLevel = SettlementLevel.getLevelFromInt(Math.min(newLevel.getLevel(), level.getLevel()));
        }
        return newLevel;
         **/
        if(this == getHighestLevel()) return this;
        else return getLevelFromInt(this.getLevel() + 1);
    }
}
