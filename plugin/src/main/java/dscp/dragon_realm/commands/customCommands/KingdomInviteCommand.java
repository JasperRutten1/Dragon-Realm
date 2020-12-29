package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomInviteCommand extends CustomCommand {

    public KingdomInviteCommand() {
        super("kingdom invite", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("target")){
            commandReturn.addReturnMessage(ChatColor.RED + "missing arguments, usage: /kingdom invite [player name]");
            return;
        }

        String target = params.get("target");

        //get kingdom of inviting player
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("Player is not part of a Kingdom.");

        //get player that will be invited
        Player toInvite = Dragon_Realm_API.getPlayerFromName(target);
        if(toInvite == null) throw new CustomCommandException("Could not find player with name: " + target);

        //check if player is part of kingdom
        if(Kingdom.isMemberOfKingdom(toInvite)) throw new CustomCommandException("This player is already part of a Kingdom.");

        kingdom.invitePlayerToKingdom(player, toInvite);

        commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully invited player to your Kingdom.");
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
