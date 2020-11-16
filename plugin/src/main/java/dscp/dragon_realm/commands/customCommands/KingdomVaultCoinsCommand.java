package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomVaultCoinsCommand extends CustomCommand {
    public KingdomVaultCoinsCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("yo are not in a kingdom");
        commandReturn.addReturnMessage(ChatColor.GREEN + "you currently have "
                + kingdom.getVault().getCoins() + " coins");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
