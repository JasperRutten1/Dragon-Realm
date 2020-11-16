package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.claims.settlements.Settlement;
import dscp.dragon_realm.kingdoms.claims.settlements.SettlementCosts;
import dscp.dragon_realm.kingdoms.claims.settlements.SettlementLevel;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomLevelUpSettlement extends CustomCommand {
    public KingdomLevelUpSettlement(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null)
            throw new CustomCommandException("you are not part of a kingdom");
        KingdomMember member = kingdom.getMembers().getMember(player);
        assert member != null;
        Settlement settlement = kingdom.getClaim().getSettlement(args[2]);
        if(settlement == null)
            throw new CustomCommandException("could not find settlement with this name in your kingdom");
        if(!(member.equals(settlement.getGovernor()) || member.hasPermission(KingdomMemberRank.ROYAL)))
            throw new CustomCommandException("only the governor of this settlement or a member with the " +
                    "rank of royal or higher can level up this settlement");
        if(settlement.getLevel() == SettlementLevel.getHighestLevel())
            throw new CustomCommandException("settlement is already at maximum level");
        SettlementLevel nextLevel = settlement.getLevel().getNextLevel();
        SettlementCosts cost = SettlementCosts.getCost(nextLevel);
        assert  cost != null;
        if(kingdom.getVault().getCoins() < cost.getCoins())
            throw new CustomCommandException("not enough coins, you need " + cost.getCoins()
                    + " and your kingdom only has " + kingdom.getVault().getCoins());
        settlement.levelUp();
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully leveled up settlement "
                + ChatColor.DARK_AQUA + settlement.getName() + ChatColor.GREEN + " to "
                + ChatColor.DARK_AQUA + settlement.getLevel().getName());

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
