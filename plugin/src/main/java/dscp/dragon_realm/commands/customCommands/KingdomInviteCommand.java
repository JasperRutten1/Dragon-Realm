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

public class KingdomInviteCommand extends CustomCommand {

    public KingdomInviteCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //get kingdom of inviting player
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("Player is not part of a Kingdom.");

        //get player that will be invited
        Player toInvite = Dragon_Realm_API.getPlayerFromName(args[1]);
        if(toInvite == null) throw new CustomCommandException("Could not find player with name: " + args[1]);

        //check if player is part of kingdom
        if(Kingdom.isMemberOfKingdom(toInvite)) throw new CustomCommandException("This player is already part of a Kingdom.");

        kingdom.invitePlayerToKingdom(player, toInvite);

        commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully invited player to your Kingdom.");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom invite [player]", "Invite a player to yor Kingdom.")
                .addArgument("player", "The name of the player you want to invite.")
                .generateHelp();
    }
}
