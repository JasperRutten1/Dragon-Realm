package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomInviteAcceptCommand extends CustomCommand {

    public KingdomInviteAcceptCommand() {
        super("kingdom inviteaccept", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("kingdom");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("kingdom")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing argument, usage: /kingdom inviteaccept [kingdom]");
            return;
        }

        try{
            Kingdom kingdom = Kingdom.getKingdomFromName(params.get("kingdom"));
            if(kingdom == null) throw new CustomCommandException("Could not find this Kingdom.");
            kingdom.inviteAcceptation(player);
            commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully joined Kingdom.");
            kingdom.sendMembersMessage(ChatColor.GOLD + "player '" + ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + "' joined the Kingdom.");
        }
        catch (KingdomException ex){
            player.sendMessage(ex.getMessage());
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
