package dscp.dragon_realm;

import dscp.dragon_realm.commands.DRCommands;
import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.CustomEnchantsCraftingRecipes;
import dscp.dragon_realm.customEnchants.events.EnchantsEvents;
import dscp.dragon_realm.discord.DiscordWebhook;
import dscp.dragon_realm.discord.ToDiscordEvents;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.claims.settlements.resources.SettlementFarmLand;
import dscp.dragon_realm.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Dragon_Realm extends JavaPlugin {

    public static Dragon_Realm instance;

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

        // initialising event objects
        try{
            //kingdom
            enchantsEvents = new EnchantsEvents();
        }
        catch(Exception e){
            System.out.println("exception in loading events");
        }

        // initialising event listeners
        getServer().getPluginManager().registerEvents(Reflection.init(), this);
        getServer().getPluginManager().registerEvents(enchantsEvents, this);
        getServer().getPluginManager().registerEvents(new ToDiscordEvents(), this);

        try{
            DiscordWebhook startUpWebhook = new DiscordWebhook(ToDiscordEvents.WEBHOOK_LINK);
            startUpWebhook.setUsername("Dragon-Realm");
            startUpWebhook.setContent("**Server starting up** \nip: " + Bukkit.getServer().getIp());
            startUpWebhook.execute();
        }
        catch (IOException e){
            System.out.println("Exception in sending discord webhook");
        }
    }

    @Override
    public void onDisable() {
        Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms"));
        try{
            DiscordWebhook startUpWebhook = new DiscordWebhook(ToDiscordEvents.WEBHOOK_LINK);
            startUpWebhook.setUsername("Dragon-Realm");
            startUpWebhook.setContent("**Server shutting down**");
            startUpWebhook.execute();
        }
        catch (IOException e){
            System.out.println("Exception in sending discord webhook");
        }
    }

    public static Dragon_Realm getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            DRCommands.handleCommand(sender, command.getName(), args);
            if(command.getName().equals("test")){
                for(Kingdom kingdom : Kingdom.kingdoms){
                    sender.sendMessage(kingdom.toString());
                }
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    player.sendMessage("farmlands found: " +
                            SettlementFarmLand.getFarmLandBlocksInChunk(player.getLocation().getChunk()).size());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("exception in command");
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms"));
        Kingdom.moveRemovedKingdoms(new File(getDataFolder(), "kingdoms"), new File(getDataFolder(), "removed_kingdoms"));
        return true;
    }
}
