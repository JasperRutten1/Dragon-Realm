package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive.PassiveAbility;
import dscp.dragon_realm.utils.weightMap.WeightMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public enum SpiritSwordActiveAbility {
    AQUA_LAUNCH(new AquaLaunchActive()),

    WIND_PULL(new WindPullActive()),

    GROUND_SLAM(new GroundSlamActive()),



    BLINK(new BlinkActive()),
    STAR_CALL(new StarCallActive());

    ActiveAbility ability;

    /**
     * ability id's:
     * water 1000-1999
     * air 2000-2999
     * earth 3000-3999
     * fire 4000-4999
     * void 5000-5999
     * light 6000-6999
     */

    SpiritSwordActiveAbility(ActiveAbility ability){
        this.ability = ability;
    }

    public ActiveAbility getAbility() {
        return ability;
    }

    public static void start(){
        Plugin plugin = Dragon_Realm.instance;
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for(SpiritSwordActiveAbility ssaa : values()){
            ActiveAbility activeAbility = ssaa.getAbility();
            //set up runnables
            if(activeAbility.hasRepeatingCode()){
                scheduler.scheduleSyncRepeatingTask(plugin, activeAbility, 0, activeAbility.getRepeatDelay());
            }
            //register event listeners
            pluginManager.registerEvents(activeAbility, plugin);
        }
        //cooldown message
        scheduler.scheduleSyncRepeatingTask(plugin, ActiveAbility::endCooldown, 1, 2);
    }

    public static SpiritSwordActiveAbility getAbilityFromID(int id){
        for(SpiritSwordActiveAbility ssaa : values()){
            if(ssaa.getAbility().getID() == id) return ssaa;
        }
        return null;
    }

    public static SpiritSwordActiveAbility getRandomAbility(SpiritElement element){
        //create list of abilities
        List<SpiritSwordActiveAbility> abilitiesWithElement = new ArrayList<>();
        for(SpiritSwordActiveAbility ssaa : values()){
            if(ssaa.getAbility().getElement() == element) abilitiesWithElement.add(ssaa);
        }

        //create weight map
        WeightMap<SpiritSwordActiveAbility> weightMap = new WeightMap<>();
        for(SpiritSwordActiveAbility ssaa: abilitiesWithElement){
            weightMap.addItem(ssaa, ssaa.getAbility().getWeight());
        }

        return weightMap.getRandom();
    }
}
