package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomAssignGovernorCommand extends CustomCommand {

    public KingdomAssignGovernorCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("you are not part of a kingdom");
        KingdomMember member = kingdom.getMembers().getMember(player);
        assert member != null;
        if(!member.hasPermission(KingdomMemberRank.ROYAL))
            throw new CustomCommandException("only a royal can assign a governor to a settlement");
        Settlement settlement = kingdom.getClaim().getSettlement(args[2]);
        if(settlement == null) throw new CustomCommandException("could not find settlement with this name");
        KingdomMember governor = kingdom.getMembers().getMember(Dragon_Realm_API.getPlayerFromName(args[3]));
        if(governor == null) throw new CustomCommandException("could not find kingdom member with this name");
        if(!governor.hasPermission(KingdomMemberRank.NOBEL))
            throw new CustomCommandException("governors must have the rank of nobel or higher");
        if(kingdom.getClaim().isGovernor(governor))
            throw new CustomCommandException("this kingdom member is already governor of a settlement");
        settlement.assignGovernor(governor);
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully assigned " + ChatColor.DARK_AQUA
                + governor.getPlayer().getName() + ChatColor.GREEN + " as governor of "
                + ChatColor.DARK_AQUA + settlement.getName());

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
