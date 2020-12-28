package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomCreateCommand extends CustomCommand {
    public KingdomCreateCommand() {
        super("kingdom create", "dscp.dr.kingdom.create");
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("name");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("name")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing argument, usage: /kingdom create [name]");
            return;
        }

        String name = params.get("name");

        if(Kingdom.isMemberOfKingdom(player)) throw new CustomCommandException("You are already a member of a Kingdom.");
        if(name.length() > 15) throw new CustomCommandException("A Kingdom may not have a name that is greater than 15 characters.");
        if(Kingdom.getKingdomFromName(name) != null)
            throw new CustomCommandException("A Kingdom with this name already exists, please try another name.");
        try{
            Kingdom kingdom = Kingdom.createKingdom(name, player);
            commandReturn.addReturnMessage(ChatColor.GREEN + "Created Kingdom with the name " + ChatColor.GOLD
                    + kingdom.getName());
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + " Created the Kingdom of '"
                        + ChatColor.DARK_AQUA + kingdom.getName() + ChatColor.GOLD + "'");
            }
        }catch(KingdomException ex){
            player.sendMessage(ex.getMessage());
        }

    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
