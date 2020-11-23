package dscp.dragon_realm.kingdoms.claims.settlements;

import lombok.Getter;
import org.bukkit.Material;

public enum SettlementLevel {
    Outpost(1, 500, 10, 50, 5000, "outpost", Material.IRON_INGOT),
    Village(2, 1000, 12, 100, 20000, "village", Material.GOLD_INGOT),
    Town(3, 3000, 15, 200, 100000, "town", Material.DIAMOND),
    City(4, 7500, 20, 400, 250000, "city", Material.EMERALD);

    String name;
    Material guiItem;
    int level;
    double foodStorage, maxTaxingRate, maxPopulation, maxCoins;

    SettlementLevel(int level, double foodStorage, double maxTaxingRate, double maxPopulation, double maxCoins, String name, Material guiItem){
        this.level = level;
        this.foodStorage = foodStorage;
        this.maxTaxingRate = maxTaxingRate;
        this.maxPopulation = maxPopulation;
        this.name = name;
        this.guiItem = guiItem;
    }

    public int getLevel() {
        return level;
    }

    public double getFoodStorage() {
        return foodStorage;
    }

    public double getMaxPopulation() {
        return maxPopulation;
    }

    public double getMaxTaxingRate() {
        return maxTaxingRate;
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
        if(this == getHighestLevel()) return this;
        else return getLevelFromInt(this.getLevel() + 1);
    }
}
