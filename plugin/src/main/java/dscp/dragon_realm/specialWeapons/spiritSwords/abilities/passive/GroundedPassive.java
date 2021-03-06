package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GroundedPassive extends PassiveAbility{
    public GroundedPassive() {
        super("Grounded", 3001, 2, SpiritElement.EARTH, 5);
    }

    @Override
    public void abilityCode(Player player, ItemStack sword, ItemMeta swordMeta) {
        Block block = player.getLocation().add(0, -1, 0).getBlock();
        if(block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK){
            if(!player.isSneaking()) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0, false, false));
        }
    }

    @Override
    public void abilityInfo(BookBuilder.BookPageBuilder pageBuilder) {
        pageBuilder.addLine("This ability channels a portion of incoming damage dealt to the holder in to the ground.")
                .addBlankLine()
                .addLine("This ability does not stack with resistance.")
                .addBlankLine()
                .addLine("Activate this ability by crouching over a dirt block.");
    }
}
