package dscp.dragon_realm.specialWeapons.spiritSwords;

import dscp.dragon_realm.utils.weightMap.WeightMap;
import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.util.Random;

public enum SpiritElement {
    WATER(ChatColor.AQUA, "Water", 1, 10),
    AIR(ChatColor.WHITE, "Air", 2, 10),
    FIRE(ChatColor.RED, "Fire", 3, 10),
    EARTH(ChatColor.GREEN, "Earth", 4, 10),
    VOID(ChatColor.DARK_PURPLE, "Void", 5, 5);

    ChatColor chatColor;
    String name;
    int intValue;
    int weight;

    SpiritElement(ChatColor chatColor, String name, int intValue, int weight){
        this.chatColor = chatColor;
        this.name = name;
        this.intValue = intValue;
        this.weight = weight;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getColoredName(){
        return chatColor + name;
    }

    public int getWeight() {
        return weight;
    }

    public static SpiritElement getRandomElement(){
        WeightMap<SpiritElement> weightMap = new WeightMap<>();
        for(SpiritElement element : values()){
            weightMap.addItem(element, element.weight);
        }
        return weightMap.getRandom();
    }

    public static SpiritElement getElementWithIntValue(int intValue){
        for(SpiritElement element : values()){
            if(element.getIntValue() == intValue) return element;
        }
        return null;
    }
}
