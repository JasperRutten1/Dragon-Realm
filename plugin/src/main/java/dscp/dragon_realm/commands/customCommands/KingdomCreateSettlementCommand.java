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

import java.util.HashMap;

public class KingdomCreateSettlementCommand extends CustomCommand {

    public KingdomCreateSettlementCommand() {
        super("settlement create", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("name");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("name")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing argument, usage: /settlement create [name]");
        }

        String name = params.get("name");

        try{
            Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
            if(kingdom == null) throw new CustomCommandException("You are not part of a Kingdom.");
            KingdomMember member = kingdom.getMembers().getMember(player);
            assert member != null;
            if(!member.hasPermission(KingdomMemberRank.ROYAL))
                throw new CustomCommandException("You must have the rank of Royal or higher.");
            if(kingdom.getClaim().getSettlements().size() >= Settlement.SETTLEMENT_MAX)
                throw new CustomCommandException("You already have the maximum amount of settlements.");
            Settlement newSettlement = new Settlement(kingdom, name, 1, player.getLocation().getChunk());
            for(Settlement settlement : kingdom.getClaim().getSettlements()){
                if(settlement.getDistance(newSettlement) < 11)
                    throw new CustomCommandException("This settlement is too close to an already existing settlement, ensure you are 11+ chunks away from an existing settlement, then try again." +
                            "\ndistance to closest settlement: " + settlement.getDistance(newSettlement));
            }
            kingdom.getClaim().addSettlement(newSettlement);
            commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully created new settlement '" + newSettlement.getName() + "'");
        }catch(KingdomException ex){
            player.sendMessage(ex.getMessage());
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
