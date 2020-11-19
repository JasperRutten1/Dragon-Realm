package dscp.dragon_realm.builders;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {

    private ChatColor lineColor = ChatColor.GRAY;
    private List<String> lines;

    /**
     * Create a new lore builder
     */
    public LoreBuilder() {
        this.lines = new ArrayList<>();
    }

    /**
     * Create a new lore builder with the specified lore builder as origin
     */
    public LoreBuilder(LoreBuilder origin) {
        this.lines = new ArrayList<>(origin.lines);
    }

    /**
     * Create a new lore builder with the specified ItemStack lore builder as origin
     */
    public LoreBuilder(ItemStack origin) {
        if(!origin.hasItemMeta()) return;

        List<String> loreOrigin = origin.getItemMeta().getLore();
        List<String> lore = (loreOrigin == null ? new ArrayList<>() : loreOrigin);

        this.lines = new ArrayList<>(lore);
    }


    /**
     * Append an empty line to the lore list
     *
     * @return self
     */
    public LoreBuilder blank() {
        this.lines.add("");
        return this;
    }

    /**
     * Set the line start color for all next lines
     *
     * @param lineColor ChatColor
     * @return self
     */
    public LoreBuilder lineColor(ChatColor lineColor) {
        this.lineColor = lineColor;
        return this;
    }

    /**
     * Reset the line start color
     *
     * @return self
     */
    public LoreBuilder resetColor() {
        this.lineColor = ChatColor.GRAY;
        return this;
    }

    /**
     * Append a div line to the lore list
     *
     * @return self
     */
    public LoreBuilder div() {
        this.lines.add(ChatColor.DARK_GRAY + "------------------------");
        return this;
    }

    /**
     * Append a line to the lore, colored GRAY. The text is
     * automatically colorized.
     *
     * @param line Line
     * @return self
     */
    public LoreBuilder line(String line) {
        if(line != null) this.lines.add(ChatColor.translateAlternateColorCodes('&', lineColor + line));
        return this;
    }

    /**
     * Append a line to the lore if matches params, colored GRAY. The text is
     * automatically colorized.
     *
     * @param line Line
     * @return self
     */
    public LoreBuilder lineIf(String line, boolean value) {
        if(line != null && value) this.lines.add(ChatColor.translateAlternateColorCodes('&', lineColor + line));
        return this;
    }

    /**
     * Append the specified lines to the lore, colored GRAY. The text is
     * automatically colorized.
     *
     * @param lines Lines
     * @return self
     */
    public LoreBuilder lines(String... lines) {
        if(lines != null) for(String line : lines) line(line);

        return this;
    }

    /**
     * Append the specified lines to the lore, colored GRAY. The text is
     * automatically colorized.
     *
     * @param lines Lines
     * @return self
     */
    public LoreBuilder lines(List<String> lines) {
        if(lines != null) for(String line : lines) line(line);

        return this;
    }

    /**
     * Build the lore lines
     *
     * @return String list
     */
    public List<String> build() {
        return new ArrayList<>(lines);
    }

}
