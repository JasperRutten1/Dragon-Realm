package dscp.dragon_realm.commands.customCommands.cosmetics;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.cosmetics.Cosmetic;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GiveCosmeticTicketCommand extends CustomCommand {
    public GiveCosmeticTicketCommand() {
        super("cosmetic ticket", Perms.ADMIN);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("name");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(!params.containsKey("name")) throw new CustomCommandException("must give name");
        Cosmetic cosmetic = Cosmetic.getCosmetic(params.get("name"));
        if(cosmetic == null) throw new CustomCommandException("Could not find cosmetic with this name");

        player.getInventory().addItem(cosmetic.getCosmeticTicket());
        player.sendMessage(ChatColor.GREEN + "Received cosmetic ticket");
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
