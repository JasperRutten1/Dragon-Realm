package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomInviteAcceptCommand extends CustomCommand {

    public KingdomInviteAcceptCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn executeCommand(CommandSender sender, String commandName, String[] args) {
        checkArgs(sender, commandName, args);

        if(!(sender instanceof Player)) throw new IllegalArgumentException("sender must be of type player");

        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        // run command code
        try{
            Kingdom kingdom = Kingdom.getKingdomFromName(args[1]);
            if(kingdom == null) throw new CustomCommandException("could not find this kingdom");
            kingdom.inviteAcceptation(player);
            commandReturn.addReturnMessage(ChatColor.GREEN + "successfully joined kingdom");
            kingdom.sendMembersMessage(ChatColor.GOLD + "player '" + ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + "' joined the kingdom");
        }
        catch (Exception e){
            commandReturn.addException(e);
        }
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("kingdom inviteaccept [kingdom]", "accept an invitation from a kingdom")
                .addArgument("kingdom", "the name of the kingdom you want to accept the invitation from")
                .generateHelp();
    }
}
