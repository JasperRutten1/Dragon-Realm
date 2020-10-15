package dscp.dragon_realm.colorDecoder;

import org.bukkit.ChatColor;

public enum Colors {
    GREEN(ChatColor.GREEN, "GREEN"),
    BLUE(ChatColor.BLUE, "BLUE");

    ChatColor chatColor;
    String name;

    Colors(ChatColor chatColor, String name){
        this.chatColor = chatColor;
        this.name = name;
    }
}
