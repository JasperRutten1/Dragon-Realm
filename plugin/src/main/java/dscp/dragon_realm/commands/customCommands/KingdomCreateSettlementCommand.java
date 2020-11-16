package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomCreateSettlementCommand extends CustomCommand {

    public KingdomCreateSettlementCommand(String permission) {
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
            throw new CustomCommandException("you must have the rank of royal or higher");
        if(kingdom.getClaim().getSettlements().size() >= Settlement.SETTLEMENT_MAX)
            throw new CustomCommandException("you already have the maximum amount of settlements");
        Settlement newSettlement = new Settlement(kingdom, args[2], 1, player.getLocation().getChunk());
        for(Settlement settlement : kingdom.getClaim().getSettlements()){
            if(settlement.getDistance(newSettlement) < 11)
                throw new CustomCommandException("to close to other settlement, must be at least 11 chunks away " +
                        "\ndistance to closest settlement: " + settlement.getDistance(newSettlement));
        }
        kingdom.getClaim().addSettlement(newSettlement);
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully created new settlement '" + newSettlement.getName() + "'");

        //return
        return commandReturn;
    }


    @Override
    public String getHelp() {
        return null;
    }
}
