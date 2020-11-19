package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.menus.KingdomOverviewMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomCommand extends CustomCommand {

    public KingdomCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;

        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom != null)
            new KingdomOverviewMenu(kingdom).open(player);
        else throw new CustomCommandException("You are not in a Kingdom.");

        return new CommandReturn(player);
    }

    @Override
    public String getHelp() {
        return null;
    }

}
