package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.vault.VaultException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomGiveCoinsCommand extends CustomCommand {
    public KingdomGiveCoinsCommand() {
        super("coins give", "dscp.dr.coins.give");
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("kingdom");
        params.addParameter("amount");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        runForNonPlayer(player, commandReturn, params);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(!params.containsKey("kingdom") || !params.containsKey("amount")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing argument, usage: /coins give [kingdom] [amount]");
            return;
        }

        String name = params.get("kingdom");
        String amount = params.get("amount");

        Kingdom kingdom = Kingdom.getKingdomFromName(name);

        if(kingdom == null) throw new CustomCommandException("Kingdom not found.");
        try{
            int coins = Integer.parseInt(amount);
            kingdom.getVault().addCoins(coins);
        }
        catch (NumberFormatException e){
            throw new CustomCommandException("Could not convert to numbers.");
        }
        catch (VaultException e){
            throw new CustomCommandException(e.getMessage());
        }
        commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully added coins.");
    }
}
