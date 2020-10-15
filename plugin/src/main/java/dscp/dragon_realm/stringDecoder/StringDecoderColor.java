package dscp.dragon_realm.stringDecoder;

import org.bukkit.ChatColor;

public enum StringDecoderColor {
    LIGHT_GREEN(ChatColor.GREEN, "LIGHT_GREEN", "LG"),
    DARK_GREEN(ChatColor.DARK_GREEN, "DARK_GREEN", "DG"),
    LIGHT_RED(ChatColor.RED, "LIGHT_RED", "LR"),
    DARK_RED(ChatColor.DARK_RED, "DARK_RED", "DR"),
    LIGHT_AQUA(ChatColor.AQUA, "LIGHT_AQUA", "LA"),
    DARK_AQUA(ChatColor.DARK_AQUA, "DARK_AQUA", "DA"),
    LIGHT_BLUE(ChatColor.BLUE, "LIGHT_BLUE", "LB"),
    DARK_BLUE(ChatColor.DARK_BLUE, "DARK_BLUE", "DB"),
    LIGHT_PURPLE(ChatColor.LIGHT_PURPLE, "LIGHT_¨PURPLE", "LP"),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "DARK_PURPLE", "DP"),
    BOLD(ChatColor.BOLD, "BOLD", "BO"),
    UNDERLINED(ChatColor.UNDERLINE, "UNDERLINED", "UL");

    ChatColor chatColor;
    String name;
    String code;

    static final String openChar = "{";
    static final String closeChar = "}";

    StringDecoderColor(ChatColor chatColor, String name, String code){
        this.chatColor = chatColor;
        this.name = name;
        if(code.length() != 2) throw new IllegalArgumentException("code must be 2 characters");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
