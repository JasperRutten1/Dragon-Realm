package dscp.dragon_realm.commands.customCommands.currency;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.currency.PlayerWallet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CheckCurrencyCommand extends CustomCommand {
    public CheckCurrencyCommand() {
        super("currency check", Perms.ADMIN);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        Player target;
        if(params.get("target") == null){
            target = player;
        }
        else{
            target = Bukkit.getPlayer(params.get("target"));
            if(target == null) throw new CustomCommandException("Target not found");
        }

        commandReturn.addReturnMessage(PlayerWallet.getWalletFromPlayer(target).getDefaultCurrency() + " coins");
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

    }
}
