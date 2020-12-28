package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlinkActive extends ActiveAbility{
    public static final int RANGE = 10;

    public BlinkActive() {
        super("Blink", 5001, SpiritElement.VOID, 10, 60000);
    }

    @Override
    public void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {
        if(hasCooldown(player)){
            player.sendMessage(ChatColor.GRAY + "in cooldown");
            return;
        }

        List<Block> targets = player.getLastTwoTargetBlocks(null, RANGE);
        if(targets.isEmpty() || targets.size() < 2) return;
        Block target = targets.get(0);

        player.teleport(target.getLocation().add(0.5, 0.5, 0.5));
        setCooldown(player);
    }

    @Override
    public void leftClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void other(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta) {

    }

    @Override
    public void repeatingCode(Player player) {

    }

    @Override
    public void abilityInfo(BookBuilder.BookPageBuilder pageBuilder) {
        pageBuilder.addLine("This ability bends space time around the holder displacing them to another location.")
                .addBlankLine()
                .addLine("The holder does not actually move, but reality does.");
    }
}
