package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KingdomUnclaimCommand extends CustomCommand {
    public KingdomUnclaimCommand() {
        super("kingdom unclaim", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {

    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null) throw new CustomCommandException("You are not part of a Kingdom.");
        Chunk chunk = player.getLocation().getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        if(!chunk.getWorld().equals(KingdomClaim.WORLD))
            throw new CustomCommandException("This command can only be preformed in the Overworld.");
        if(!kingdom.equals(KingdomClaim.claimedBy(x, z)))
            throw new CustomCommandException("This chunk is not claimed by your Kingdom.");
        KingdomMember member = kingdom.getMembers().getMember(player);
        assert member != null;
        if(!member.hasPermission(KingdomMemberRank.KNIGHT))
            throw new CustomCommandException("You must have the rank of Knight or higher to do this.");
        if(kingdom.getClaim().isCoveredBySettlement(x, z))
            throw new CustomCommandException("This chunk is covered by a settlement, remove the settlement before trying again.");
        kingdom.getClaim().removeClaim(x, z);
        commandReturn.addReturnMessage(ChatColor.GREEN + "Successfully removed claim on chunk.");
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
