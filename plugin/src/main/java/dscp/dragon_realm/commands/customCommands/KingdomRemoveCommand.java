package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomRemoveCommand extends CustomCommand {

    public KingdomRemoveCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CommandException("you are not a member of a kingdom");
        if(!kingdom.getMembers().getKing().getPlayerUUID().equals(player.getUniqueId()))
            throw new CommandException("only the king can remove the kingdom");

        Kingdom.removeKingdom(player).sendMembersMessage(ChatColor.GOLD + "this kingdom has been removed");
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully removed kingdom");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom remove", "removes a kingdom, can only be done by the king")
                .generateHelp();
    }
}
