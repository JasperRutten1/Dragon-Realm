package dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.builders.BookBuilder;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritElement;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import dscp.dragon_realm.utils.BookDescription;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ActiveAbility implements Listener, Runnable, BookDescription {
    private String name;
    private int ID;
    private SpiritElement element;
    private int weight;
    private long cooldown;
    private long repeatDelay;
    private boolean hasRepeatingCode;

    private Map<UUID, Long> cooldownMap;

    public ActiveAbility(String name, int ID, SpiritElement element, int weight, long cooldown){
        this.name = name;
        this.ID = ID;
        this.element = element;
        this.weight = weight;
        this.cooldown = cooldown;
        this.cooldownMap = new HashMap<>();
    }

    public ActiveAbility(String name, int ID, SpiritElement element, int weight, long cooldown, long repeatDelay){
        this(name, ID, element, weight, cooldown);
        this.repeatDelay = repeatDelay;
        this.hasRepeatingCode = true;
    }

    public SpiritElement getElement() {
        return element;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getRepeatDelay() {
        return repeatDelay;
    }

    public boolean hasRepeatingCode() {
        return hasRepeatingCode;
    }

    public void setCooldown(Player player){
        cooldownMap.put(player.getUniqueId(), this.cooldown + System.currentTimeMillis());
    }

    public boolean hasCooldown(Player player){
        if(cooldownMap.containsKey(player.getUniqueId())){
            return cooldownMap.get(player.getUniqueId()) > System.currentTimeMillis();
        }
        else return false;
    }

    @EventHandler
    public void onRightClickWithSpiritSword(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack holdingItem = player.getInventory().getItemInMainHand();
        ItemMeta meta = holdingItem.getItemMeta();

        if(meta == null) return;
        if(!(SpiritSword.isSpiritSword(holdingItem))) return;
        if(!(SpiritSword.isOwner(meta, player))) return;

        ProtectedZone pz = Dragon_Realm.dragonProtect.getZone(player.getLocation().getChunk());
        if(pz != null){
            DragonProtect.sendMessage(player, "you can't use abilities here");
            return;
        }

        SpiritSwordActiveAbility ability = SpiritSword.getActiveAbility(meta);
        if(ability == null) return;
        if(ability != SpiritSwordActiveAbility.getAbilityFromID(this.ID)) return;

        if(event.getAction() == Action.RIGHT_CLICK_AIR){
            rightClick(event, player, holdingItem, meta);
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            rightClick(event, player, holdingItem, meta);
        }
        else if(event.getAction() == Action.LEFT_CLICK_AIR){
            leftClick(event, player, holdingItem, meta);
        }
        else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            leftClick(event, player, holdingItem, meta);
        }
        else{
            other(event, player, holdingItem, meta);
        }

        holdingItem.setItemMeta(meta);
    }

    public abstract void rightClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta);

    public abstract void leftClick(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta);

    public abstract void other(PlayerInteractEvent event, Player player, ItemStack sword, ItemMeta meta);

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            repeatingCode(player);
        }
    }

    public abstract void repeatingCode(Player player);

    public static void endCooldown(){
        for(Player player : Bukkit.getOnlinePlayers()){
            for(SpiritSwordActiveAbility ssaa : SpiritSwordActiveAbility.values()){
                if(ssaa.ability.cooldownMap.containsKey(player.getUniqueId())
                        && ssaa.ability.cooldownMap.get(player.getUniqueId()) <=System.currentTimeMillis()){
                    ssaa.ability.cooldownMap.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + ssaa.ability.getName() + ": cooldown ended");
                }
            }
        }
    }

    @Override
    public String bookPage() {
         BookBuilder.BookPageBuilder builder = new BookBuilder.BookPageBuilder()
                .addLine(this.getElement().getChatColor() + this.getName())
                .addBlankLine();
         abilityInfo(builder);
         builder.addBlankLine()
                 .addLine("cooldown: " + ((int) this.cooldown/1000) + "seconds");
         return builder.build();
    }

    public abstract void abilityInfo(BookBuilder.BookPageBuilder pageBuilder);
}
