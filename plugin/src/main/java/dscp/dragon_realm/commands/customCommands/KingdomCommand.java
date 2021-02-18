package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.menus.KingdomOverviewMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomCommand extends CustomCommand {

    public KingdomCommand() {
        super("kingdom", Perms.KINGDOM_DEFAULT);
    }

    public String getHelp() {
        return null;
    }

    @Override
    public void parameters(CommandParams params) {

    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom != null)
            new KingdomOverviewMenu(kingdom, null).open(player);
        else throw new CustomCommandException("You are not in a Kingdom.");

    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
