package dscp.dragon_realm.commands.customCommands.bounty;

import dscp.dragon_realm.bounty.Bounty;
import dscp.dragon_realm.bounty.BountyContainer;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.currency.PlayerWallet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlaceBountyCommand extends CustomCommand {
    private static final int minAmount = 500; // amount in coins

    public PlaceBountyCommand() {
        super("bounty place", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
        params.addParameter("amount");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        try{
            if(params.get("target") == null) throw new CustomCommandException("Must give target");
            if(params.get("amount") == null) throw new CustomCommandException("Must enter amount");

            Player target = Bukkit.getPlayer(params.get("target"));
            if(target == null) throw new CustomCommandException("Could not find player with this name");

            if(player.equals(target)) throw new CustomCommandException("Can't put a bounty on yourself");

            int amount = Integer.parseInt(params.get("amount"));

            PlayerWallet wallet = PlayerWallet.getWalletFromPlayer(player);

            if(wallet.getCurrencyType().toCoins(amount) < minAmount) throw new CustomCommandException("Minimum bounty value is " + minAmount + " coins");

            wallet.hasEnoughDefault(amount, wallet.getCurrencyType());
            Bounty bounty = new Bounty(target, player, wallet.getCurrencyType().toCoins(amount));

            BountyContainer.placeBounty(bounty);
            wallet.changeDefaultCurrency(-wallet.getCurrencyType().toCoins(amount));

            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.RESET + " placed a bounty on "
                    + ChatColor.GOLD + target.getName() + ChatColor.RESET + " , amount: "
                    + amount + wallet.getCurrencyType().getName());
        }
        catch (NumberFormatException ex){
            throw new CustomCommandException("Amount must be a number");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
