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

public class KingdomGiveCoinsCommand extends CustomCommand {
    public KingdomGiveCoinsCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromName(args[2]);

        if(kingdom == null) throw new CustomCommandException("kingdom not found");
        try{
            int coins = Integer.parseInt(args[3]);
            kingdom.getVault().addCoins(coins);
        }
        catch (NumberFormatException e){
            throw new CustomCommandException("could not convert to numbers");
        }
        catch (VaultException e){
            throw new CustomCommandException(e.getMessage());
        }
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully added coins");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
