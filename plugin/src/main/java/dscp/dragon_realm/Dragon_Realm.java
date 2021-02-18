package dscp.dragon_realm;

import dscp.dragon_realm.NPCs.merchants.MerchantEvents;
import dscp.dragon_realm.advancedParticles.AdvancedParticles;
import dscp.dragon_realm.bounty.BountyContainer;
import dscp.dragon_realm.commands.DRCommands;
import dscp.dragon_realm.cosmetics.CosmeticsEvents;
import dscp.dragon_realm.currency.CurrencyEvents;
import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.CustomEnchantsCraftingRecipes;
import dscp.dragon_realm.customEnchants.events.EnchantsEvents;
import dscp.dragon_realm.dataContainer.PlayerDataContainer;
import dscp.dragon_realm.discord.DiscordWebhook;
import dscp.dragon_realm.discord.ToDiscordEvents;
import dscp.dragon_realm.dragonProtect.areaProtect.DragonProtect;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.active.SpiritSwordActiveAbility;
import dscp.dragon_realm.specialWeapons.spiritSwords.abilities.passive.SpiritSwordPassiveAbility;
import dscp.dragon_realm.specialWeapons.spiritSwords.events.SpiritSwordEventManager;
import dscp.dragon_realm.test.DiamondMineEvent;
import dscp.dragon_realm.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Dragon_Realm extends JavaPlugin {

    public static Dragon_Realm instance;
    public static DragonProtect dragonProtect;

    //kingdom

    //event objects
    EnchantsEvents enchantsEvents;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Dragon Realm Plugin is starting!");

        // register enchantments
        CustomEnchants.register();

        //register recipes
        CustomEnchantsCraftingRecipes.loadCraftingRecipes();

        //check data folder
        File dataFolder = getDataFolder();
        if(!dataFolder.exists())
            if(!dataFolder.mkdir())
                System.out.println("could not create folder");

        // initialising objects
        Kingdom.loadKingdoms(new File(getDataFolder(), "kingdoms"));
        SpiritSword.loadSoulBindMap();
        SpiritSwordPassiveAbility.start();
        SpiritSwordActiveAbility.start();
        DragonProtect.onEnable();

        // initialising event objects
        try{
            //kingdom
            enchantsEvents = new EnchantsEvents();
        }
        catch(Exception e){
            System.out.println("exception in loading events");
        }

        // initialising event listeners
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(Reflection.init(), this);
        manager.registerEvents(enchantsEvents, this);
        manager.registerEvents(new ToDiscordEvents(), this);
        manager.registerEvents(new DiamondMineEvent(), this);
        manager.registerEvents(new CurrencyEvents(), this);
        manager.registerEvents(new CosmeticsEvents(), this);
        manager.registerEvents(new MerchantEvents(), this);
        SpiritSwordEventManager.registerEvents();
        BountyContainer.initialise();
    }

    public static void startUpDiscordEmbed(){
        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> {
            try{
                DiscordWebhook startUpWebhook = new DiscordWebhook(ToDiscordEvents.WEBHOOK_LINK);
                startUpWebhook.setUsername("Dragon-Realm");
                startUpWebhook.setContent("**Server starting up** \nip: " + Bukkit.getServer().getIp());
                startUpWebhook.execute();
            }
            catch (IOException e){
                System.out.println("Exception in sending discord webhook, trying again");
                startUpDiscordEmbed();
            }

        }, 20L);
    }

    @Override
    public void onDisable() {
        Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms"));
        SpiritSword.saveSoulBindMap();
        PlayerDataContainer.unloadAllPlayerData();
        BountyContainer.saveContainer();
        DragonProtect.onDisable();
    }

    public static Dragon_Realm getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            DRCommands.handleCommand(sender, command.getName(), args);
            if(command.getName().equals("test") && sender instanceof Player){
                Player player = (Player) sender;
                AdvancedParticles.particleSphere(player, Particle.FLAME, 2, 40);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("exception in command");
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
