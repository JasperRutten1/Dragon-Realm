package dscp.dragon_realm.builders;

import com.google.gson.Gson;
import dscp.dragon_realm.enums.FontInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An easy to way create TextComponents
 */
public class TextBuilder {

    private List<BaseComponent> components;
    private BaseComponent current;

    public TextBuilder() {
        this.components = new ArrayList<>();
        this.current = null;
    }

    public TextBuilder(TextBuilder origin) {
        this.components = new ArrayList<>(origin.components);
        this.current = origin.current == null ? null : new TextComponent(origin.current);
    }

    public TextBuilder(BaseComponent... components) {
        this.components = new ArrayList<>(Arrays.asList(components));
        this.components.remove(components.length - 1);

        this.current = new TextComponent(components[components.length - 1]);
    }

    /**
     * Append a string of text to the builder
     *
     * @param text Text
     * @return self
     */
    public TextBuilder text(String text) {
        if(this.current != null) this.components.add(this.current);
        this.current = new TextComponent(text);
        gray();
        return this;
    }

    /**
     * Append a string of text to the builder
     *
     * @param text Text
     * @return self
     */
    public TextBuilder text(TextBuilder text) {
        if(this.current != null) this.components.add(this.current);
        this.components.addAll(text.components);
        this.current = text.current;
        return this;
    }

    /**
     * Append a string of text to the builder
     *
     * @param text Text
     * @return self
     */
    public TextBuilder text(Object text) {
        if(this.current != null) this.components.add(this.current);
        this.current = new TextComponent(Objects.toString(text));
        gray();
        return this;
    }

    /**
     * Insert a phrase into the current builder
     *
     * @param phrase Phrase to insert
     * @param params Phrase parameters
     * @return self
     */
    public TextBuilder insertPhrase(Phrase phrase, Object... params) {
        text(phrase.format(params));
        return this;
    }

    /**
     * Start a new line
     *
     * @return self
     */
    public TextBuilder newLine() {
        text("\n");
        return this;
    }

    /**
     * Append a colorized string of text to the builder
     *
     * @param text Text
     * @return TextBuilder
     */
    public TextBuilder colorizedText(String text) {
        text(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }

    /**
     * Resolve the legacy formatted string into this TextBuilder
     *
     * @param text Text
     * @return TextBuilder
     */
    public TextBuilder parsedText(String text) {
        text(new TextBuilder(TextComponent.fromLegacyText(text)));
        return this;
    }

    /*
     * FORMATTING
     */

    /**
     * Set the color for the previously appended text
     *
     * @param color Chat Color
     * @return TextBuilder
     */
    public TextBuilder color(ChatColor color) {
        this.current.setColor(color);
        return this;
    }

    /**
     * Set the color for the previously appended text
     *
     * @param color Chat Color
     * @return TextBuilder
     */
    public TextBuilder color(org.bukkit.ChatColor color) {
        this.current.setColor(color.asBungee());
        return this;
    }

    /**
     * Set the text color to DARK_BLUE
     *
     * @return TextBuilder
     */
    public TextBuilder darkBlue() {
        color(ChatColor.DARK_BLUE);
        return this;
    }

    /**
     * Set the text color to DARK_GREEN
     *
     * @return TextBuilder
     */
    public TextBuilder darkGreen() {
        color(ChatColor.DARK_GREEN);
        return this;
    }

    /**
     * Set the text color to DARK_AQUA
     *
     * @return TextBuilder
     */
    public TextBuilder darkAqua() {
        color(ChatColor.DARK_AQUA);
        return this;
    }

    /**
     * Set the text color to DARK_RED
     *
     * @return TextBuilder
     */
    public TextBuilder darkRed() {
        color(ChatColor.DARK_RED);
        return this;
    }

    /**
     * Set the text color to DARK_PURPLE
     *
     * @return TextBuilder
     */
    public TextBuilder purple() {
        color(ChatColor.DARK_PURPLE);
        return this;
    }

    /**
     * Set the text color to GOLD
     *
     * @return TextBuilder
     */
    public TextBuilder gold() {
        color(ChatColor.GOLD);
        return this;
    }

    /**
     * Set the text color to GRAY
     *
     * @return TextBuilder
     */
    public TextBuilder gray() {
        color(ChatColor.GRAY);
        return this;
    }

    /**
     * Set the text color to DARK_GRAY
     *
     * @return TextBuilder
     */
    public TextBuilder darkGray() {
        color(ChatColor.DARK_GRAY);
        return this;
    }

    /**
     * Set the text color to BLUE
     *
     * @return TextBuilder
     */
    public TextBuilder blue() {
        color(ChatColor.BLUE);
        return this;
    }

    /**
     * Set the text color to GREEN
     *
     * @return TextBuilder
     */
    public TextBuilder green() {
        color(ChatColor.GREEN);
        return this;
    }

    /**
     * Set the text color to AQUA
     *
     * @return TextBuilder
     */
    public TextBuilder aqua() {
        color(ChatColor.AQUA);
        return this;
    }

    /**
     * Set the text color to RED
     *
     * @return TextBuilder
     */
    public TextBuilder red() {
        color(ChatColor.RED);
        return this;
    }

    /**
     * Set the text color to LIGHT_PURPLE
     *
     * @return TextBuilder
     */
    public TextBuilder magenta() {
        color(ChatColor.LIGHT_PURPLE);
        return this;
    }

    /**
     * Set the text color to YELLOW
     *
     * @return TextBuilder
     */
    public TextBuilder yellow() {
        color(ChatColor.YELLOW);
        return this;
    }

    /**
     * Set the text color to WHITE
     *
     * @return TextBuilder
     */
    public TextBuilder white() {
        color(ChatColor.WHITE);
        return this;
    }

    /**
     * Add bold formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder bold() {
        this.current.setBold(true);
        return this;
    }

    /**
     * Add italic formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder italic() {
        this.current.setItalic(true);
        return this;
    }

    /**
     * Add underline formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder underline() {
        this.current.setUnderlined(true);
        return this;
    }

    /**
     * Add strikethrough formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder strikethrough() {
        this.current.setStrikethrough(true);
        return this;
    }

    /**
     * Add obfuscated formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder obfuscated() {
        this.current.setObfuscated(true);
        return this;
    }

    /**
     * Disable bold formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder noBold() {
        this.current.setBold(false);
        return this;
    }

    /**
     * Disable italic formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder noItalic() {
        this.current.setItalic(false);
        return this;
    }

    /**
     * Disable underline formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder noUnderline() {
        this.current.setUnderlined(false);
        return this;
    }

    /**
     * Disable strikethrough formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder noStrikethrough() {
        this.current.setStrikethrough(false);
        return this;
    }

    /**
     * Add obfuscated formatting to the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder notObfuscated() {
        this.current.setObfuscated(false);
        return this;
    }

    /**
     * Resets the formatting of the previously appended text
     *
     * @return TextBuilder
     */
    public TextBuilder reset() {
        this.current.setColor(ChatColor.RESET);
        return this;
    }

    /*
     * EVENTS
     */

    /**
     * Adds an open URL click action to the previously appended text
     *
     * @param value Value
     * @return TextBuilder
     */
    public TextBuilder openURL(String value) {
        this.current.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, value));
        return this;
    }

    /**
     * Adds a run command click action to the previously appended text
     *
     * @param value Value
     * @return TextBuilder
     */
    public TextBuilder runCommand(String value) {
        this.current.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, value));
        return this;
    }

    /**
     * Adds a suggest command click action to the previously appended text
     *
     * @param value Value
     * @return TextBuilder
     */
    public TextBuilder suggestCommand(String value) {
        this.current.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value));
        return this;
    }

    /**
     * Adds a text hover action to the previously appended text
     *
     * @param value Value
     * @return TextBuilder
     */
    public TextBuilder hover(String value) {
        BaseComponent[] msg = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', value));
        this.current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg));
        return this;
    }

    /**
     * Adds a text hover action to the previously appended text
     *
     * @param components Value
     * @return TextBuilder
     */
    public TextBuilder hover(BaseComponent... components) {
        this.current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, components));
        return this;
    }

    /**
     * Adds an item description hover action to the previously appended text
     *
     * @param item Value
     * @return TextBuilder
     */
    public TextBuilder hoverItem(ItemStack item) {
        if(item.getItemMeta() == null) return this;
        BaseComponent[] msg = TextComponent.fromLegacyText(item.getItemMeta().getDisplayName());
        this.current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, msg));
        return this;
    }

    /*
     * MISC METHODS
     */

    @Override
    public String toString() {
        List<BaseComponent> comps = new ArrayList<>(this.components);
        if(this.current != null) comps.add(this.current);
        return new Gson().toJson(comps);
    }

    /**
     * Build the BaseComponent list
     *
     * @return BaseComponent
     */
    public BaseComponent[] build() {
        List<BaseComponent> comps = new ArrayList<>(this.components);
        if(this.current != null) comps.add(this.current);
        return comps.toArray(new BaseComponent[0]);
    }

    /**
     * Build the string
     *
     * @return String
     */
    public String buildString() {
        return BaseComponent.toLegacyText(build());
    }

    /**
     * Build a plain string
     *
     * @return String
     */
    public String buildPlainString() {
        return BaseComponent.toPlainText(build());
    }

    public TextBuilder centerText(TextBuilder text) {
        String message = TextComponent.toLegacyText(text.build());

        if(message.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("Text must not be multiline");
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
            } else if(previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontInfo dFI = FontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();

        while(compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return new TextBuilder()
                .text(sb.toString())
                .text(text);
    }

    /**
     * Sends the message to the specified Player, given as CommandSender
     *
     * @param cs CommandSender Player
     */
    public void sendTo(CommandSender cs) {
        if(cs instanceof Player) {
            Player p = (Player) cs;

            BaseComponent[] msg = build();
            p.spigot().sendMessage(msg);
        }
    }

    /**
     * Sends the message to the specified Player
     *
     * @param p Player
     */
    public void sendTo(Player p) {
        if(p == null) return;
        BaseComponent[] msg = build();
        p.spigot().sendMessage(msg);
    }

    /**
     * Sends the message to the specified Players
     *
     * @param p Player list
     */
    public void sendTo(Player... p) {
        for(Player pl : p) {
            sendTo(pl);
        }
    }

    /**
     * Sends the message to the specified Players
     *
     * @param p Player list
     */
    public void sendTo(List<Player> p) {
        for(Player pl : p) {
            sendTo(pl);
        }
    }

    /**
     * Send this message to all players
     */
    public void broadcast() {
        broadcast(player -> true);
    }

    /**
     * Send this message to all players who pass the
     * given predicate.
     *
     * @param filter Filter predicate
     */
    public void broadcast(Predicate<Player> filter) {
        Bukkit.getOnlinePlayers().stream()
            .filter(filter)
            .forEach(this::sendTo);
    }

    /**
     * Create a new TextBuilder with the givem Phrase as origin
     *
     * @param phrase Phrase to use
     * @param params Phrase parameters
     * @return New TextBuilder
     */
    public static TextBuilder phrase(Phrase phrase, Object... params) {
        return phrase.format(params);
    }

    /**
     * Static creation method for initiating a TextBuilder
     *
     * @return TextBuilder
     */
    public static TextBuilder create() {
        return new TextBuilder();
    }

    /**
     * An enum containing dynamic default messages, that can be filled in with variables at runtime,
     * allowing an easy use of global default messages.
     */
    public enum Phrase {

        /**
         * Usage: {..}
         */
        USAGE {
            @Override
            public TextBuilder format(Object... params) {
                return new TextBuilder().text(String.format("Usage: %s", params));
            }
        },

        /**
         * You can't do that buddy
         */
        NO_PERM {
            @Override
            public TextBuilder format(Object... params) {
                return new TextBuilder().text("Woopsies! Sorry can't let you do that you're missing the correct permission").red();
            }
        },

        /**
         * Invalid argument(s) supplied OR Invalid argument(s), please use "{..}"
         */
        INVALID_ARGUMENT {
            @Override
            public TextBuilder format(Object... params) {
                if(params.length > 0) {
                    return new TextBuilder().text(String.format("Invalid argument(s), please use \"/%s\"", params)).red();
                } else {
                    return new TextBuilder().text("Invalid argument(s) supplied").red();
                }
            }
        },

        /**
         * -------------- | {..} | --------------
         */
        HELP_TOP {
            @Override
            public TextBuilder format(Object... params) {
                return new TextBuilder().colorizedText(String.format("&a&m--&b&m--&9&m--&d&m--&5&m--&e&m--&6&m--&c&m--&8< %s &8>&c&m--&6&m--&e&m--&5&m--&d&m--&9&m--&b&m--&a&m--", params));
            }
        };

        protected abstract TextBuilder format(Object... params);
    }
}