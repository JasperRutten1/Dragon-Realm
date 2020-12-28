package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive.PassiveAbility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WaterPressurePassive extends PassiveAbility {

    public WaterPressurePassive() {
        super("Water Pressure", 1001, 2, SpiritElement.WATER, 10);
    }

    @Override
    public void abilityCode(Player player, ItemStack sword, ItemMeta swordMeta) {
        Block block = player.getLocation().getBlock();
        if(!(block.getType() == Material.WATER)) return;
        if(player.hasPotionEffect(PotionEffectType.REGENERATION)) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0, false, false));
    }

    @Override
    public void abilityInfo(BookBuilder.BookPageBuilder pageBuilder) {
        pageBuilder.addLine("This ability converts the water pressure exerted on the holder in to energy that will heal the holder")
                .addBlankLine()
                .addLine("This ability does not stack with regeneration");
    }
}
