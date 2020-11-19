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

public class KingdomInviteAcceptCommand extends CustomCommand {

    public KingdomInviteAcceptCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromName(args[1]);
        if(kingdom == null) throw new CustomCommandException("Could not find this Kingdom.");
        kingdom.inviteAcceptation(player);
        commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully joined Kingdom.");
        kingdom.sendMembersMessage(ChatColor.GOLD + "player '" + ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + "' joined the Kingdom.");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom inviteaccept [kingdom]", "Accept an invitation from a Kingdom.")
                .addArgument("kingdom", "The name of the Kingdom you want to accept the invitation from.")
                .generateHelp();
    }
}
