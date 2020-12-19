package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.utils.weightMap.WeightMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public enum SpiritSwordPassiveAbility {
    WATER_PRESSURE(new WaterPressurePassive()),

    Item_COLLECTOR(new ItemCollectorPassive()),

    GROUND_REGEN(new GroundedPassive()),



    WORMHOLE(new WormHolePassive());

    /**
     * ability id's:
     * water 1000-1999
     * air 2000-2999
     * earth 3000-3999
     * fire 4000-4999
     * void 5000-5999
     * light 600-6999
     */

    PassiveAbility ability;

    SpiritSwordPassiveAbility(PassiveAbility ability){
        this.ability = ability;
    }

    public PassiveAbility getAbility() {
        return ability;
    }


    public static void start(){
        Plugin plugin = Dragon_Realm.instance;
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(SpiritSwordPassiveAbility sspa : values()){
            PassiveAbility passiveAbility = sspa.getAbility();
            //set up runnables
            if(passiveAbility.getRepeatDelay() > 0){
                scheduler.scheduleSyncRepeatingTask(plugin, passiveAbility, 0, passiveAbility.getRepeatDelay());
            }
            //set up event listeners
            pluginManager.registerEvents(passiveAbility, plugin);
        }
    }

    public static SpiritSwordPassiveAbility getRandomAbility(SpiritElement element){
        //create a list of all abilities with the given element
        List<SpiritSwordPassiveAbility> elementalAbilities = new ArrayList<>();
        for(SpiritSwordPassiveAbility sspa : values()){
            if(element == sspa.ability.getElement()) elementalAbilities.add(sspa);
        }

        //get random from list
        WeightMap<SpiritSwordPassiveAbility> weightMap = new WeightMap<>();
        for(SpiritSwordPassiveAbility sspa : elementalAbilities){
            weightMap.addItem(sspa, sspa.getAbility().getWeight());
        }
        return weightMap.getRandom();
    }

    public static SpiritSwordPassiveAbility getAbilityFromID(int ID){
        for(SpiritSwordPassiveAbility sspa : values()){
            if(sspa.ability.getID() == ID) return sspa;
        }
        return null;
    }
}
