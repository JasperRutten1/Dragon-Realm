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

import java.util.HashMap;

public class KingdomVaultCoinsCommand extends CustomCommand {
    public KingdomVaultCoinsCommand() {
        super("vault", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {

    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("You are not in a Kingdom.");
        commandReturn.addReturnMessage(ChatColor.GREEN + "You currently have ⛃"
                + kingdom.getVault().getCoins() + " coins.");
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
