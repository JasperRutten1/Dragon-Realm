package dscp.dragon_realm;

import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.CustomEnchantsCraftingRecipes;
import dscp.dragon_realm.customEnchants.events.EnchantsEvents;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
        Kingdom.SaveKingdoms(new File(getDataFolder(), "kingdoms"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            if(sender instanceof Player){
                Player player = (Player) sender;

                switch (command.getName()){
                    case "kingdom":

                        switch (args[0]){
                            case "create":
                                Kingdom.createKingdom(args[1], player);
                                break;
                        }
                        break;
                    case "test":

                        switch (args[0]){
                            case "kingdoms":
                                player.sendMessage(Kingdom.kingdoms.size() + "");
                                break;
                        }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            sender.sendMessage(ChatColor.RED + "missing arguments");
        }
        catch (KingdomException e){
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("exception in command");
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
