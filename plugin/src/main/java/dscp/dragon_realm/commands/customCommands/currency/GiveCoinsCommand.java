package dscp.dragon_realm.commands.customCommands.currency;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.currency.PlayerWallet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GiveCoinsCommand extends CustomCommand {

    public GiveCoinsCommand() {
        super("currency give", Perms.ADMIN);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
        params.addParameter("amount");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        runForNonPlayer(player, commandReturn, params);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        try{
            if(params.get("target") == null) throw new CustomCommandException("Must give target");
            if(params.get("amount") == null) throw new CustomCommandException("Must enter amount");

            Player target = Bukkit.getPlayer(params.get("target"));
            if(target == null) throw new CustomCommandException("Could not find player with this name");

            int amount = Integer.parseInt(params.get("amount"));

            PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(target);
            wallet.changeDefaultCurrency(amount);
            commandReturn.addReturnMessage(amount + " of coins added for " + target.getName());
        }
        catch (NumberFormatException ex){
            throw new CustomCommandException("amount must be a number");
        }
    }
}
