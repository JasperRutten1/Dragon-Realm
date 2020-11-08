package dscp.dragon_realm;

import dscp.dragon_realm.commands.DRCommands;
import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.CustomEnchantsCraftingRecipes;
import dscp.dragon_realm.customEnchants.events.EnchantsEvents;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Dragon_Realm extends JavaPlugin {

    //kingdom

    //event objects
    EnchantsEvents enchantsEvents;

    @Override
    public void onEnable() {
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
        getServer().getPluginManager().registerEvents(enchantsEvents, this);
    }

    @Override
    public void onDisable() {
        Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            DRCommands.handleCommand(sender, command.getName(), args);
            if(command.getName().equals("test")){
                for(Kingdom kingdom : Kingdom.kingdoms){
                    sender.sendMessage(Objects.requireNonNull(kingdom.getMembers().getKing().getPlayer().getName()));
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
