package dscp.dragon_realm;

import dscp.dragon_realm.customEnchants.CustomEnchants;
import dscp.dragon_realm.customEnchants.CustomEnchantsCraftingRecipes;
import dscp.dragon_realm.customEnchants.events.EnchantsEvents;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.gui.KingdomGUI;
import dscp.dragon_realm.kingdoms.gui.KingdomGUIEvents;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class Dragon_Realm extends JavaPlugin {

    //kingdom

    //event objects
    EnchantsEvents enchantsEvents;
    KingdomGUIEvents guiEvents;

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
        try {
            Kingdom.loadKingdoms(new File(getDataFolder(), "kingdoms.dat"));
        } catch (KingdomException e) {
            e.printStackTrace();
        }

        // initialising event objects
        try{
            //kingdom
            enchantsEvents = new EnchantsEvents();
            guiEvents = new KingdomGUIEvents();
        }
        catch(Exception e){
            System.out.println("exception in loading events");
        }

        // initialising event listeners
        getServer().getPluginManager().registerEvents(enchantsEvents, this);
        getServer().getPluginManager().registerEvents(guiEvents, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            if(sender instanceof Player){
                Player player = (Player) sender;
                try{
                    switch (command.getName()) {
                        case "kingdom":
                        case "k":
                            try {
                                switch (args[0]) {
                                    case "create":
                                        try{
                                            Kingdom.createKingdom(player, args[1]);
                                            Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms.dat"));
                                        }
                                        catch (ArrayIndexOutOfBoundsException e){
                                            player.sendMessage(ChatColor.RED + "missing arguments, please give a name (/kingdom create [name])");

                                        }
                                        break;

                                    case "claim":
                                            Kingdom.claimChunk(player);
                                            Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms.dat"));
                                        break;

                                    case "capital":
                                        try{
                                            switch (args[1]){
                                                case "create":
                                                    Kingdom.createCapital(player);
                                                    Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms.dat"));
                                                    break;
                                            }
                                        }
                                        catch (ArrayIndexOutOfBoundsException e){
                                            player.sendMessage(ChatColor.GOLD + "usage: /kingdom capital [create]");
                                        }
                                        break;

                                    case "map":
                                        Kingdom.showMap(player);
                                        break;

                                    case "invite":
                                        Player invited = Dragon_Realm_API.getPlayerFromName(args[1]);
                                        if(invited == null) throw new KingdomException("could not find player with this name");
                                        Kingdom.invitePlayerToKingdom(player, invited);
                                        Kingdom.saveKingdoms(new File(getDataFolder(), "kingdoms.dat"));
                                        break;

                                    case "accept":
                                        Kingdom.acceptInvite(player, args[1]);
                                        break;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException e){
                                player.sendMessage(ChatColor.GOLD + "usage: /kingdom [create / claim / capital]");
                            }
                            break;

                        case "help":
                            player.sendMessage("not implemented");
                            break;

                        case "test":
                            switch (args[0]){
                                case "kingdom":
                                    Kingdom.test(player);
                                    break;
                                case "gui":
                                    player.openInventory(KingdomGUI.kingdomMembersGUI(player));
                            }
                            break;

                        case "book":
                            try{
                                CustomEnchants.getEnchantedBook(player, args[0], Integer.parseInt(args[1]));
                            }
                            catch (NumberFormatException e){
                                player.sendMessage(ChatColor.RED + "amount (argument 2) must be a number");
                            }


                        case "feed":
                            player.setFoodLevel(20);
                            break;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    player.sendMessage("missing arguments, for more info use: /dr help");
                }

            }
        }
        catch (Exception e){
            System.out.println("exception in command");
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
