package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.AbilityException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class PassiveAbility implements Runnable, Listener {
    private String name;
    private int ID;
    private long repeatDelay;
    private SpiritElement element;
    private int weight;

    public PassiveAbility(String name, int ID, long repeatDelay, SpiritElement element, int weight){
        this.name = name;
        this.ID = ID;
        this.repeatDelay = repeatDelay;
        this.element = element;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public long getRepeatDelay() {
        return repeatDelay;
    }

    public SpiritElement getElement() {
        return element;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for(Player player : players){
            playerCode(player);
        }
    }

    private void playerCode(Player player){
        //check if player is holding spirit sword
        if(!SpiritSword.isSpiritSword(player.getInventory().getItemInMainHand()))
            return;

        ItemStack sword = player.getInventory().getItemInMainHand();
        ItemMeta swordMeta = sword.getItemMeta();
        if(swordMeta == null)
            return;

        //check if player is owner of spirit sword
        if(!SpiritSword.isOwner(swordMeta, player))
            return;
        //check if sword is level 3 or higher
        if(!(SpiritSword.getLevel(swordMeta) >= 3))
            return;
        //check if has passive ability
        if(!(SpiritSword.hasPassiveAbility(swordMeta, SpiritSwordPassiveAbility.getAbilityFromID(this.ID))))
            return;

        //run ability code
        abilityCode(player, sword, swordMeta);

        //set meta back to sword in case of changes
        sword.setItemMeta(swordMeta);
    }

    /**
     * the code the ability will run
     * @param player the holder of the sword
     * @param sword the sword item
     * @param swordMeta the meta data of the sword item
     */
    public abstract void abilityCode(Player player, ItemStack sword, ItemMeta swordMeta);
}
