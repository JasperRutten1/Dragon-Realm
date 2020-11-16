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

public class KingdomUnclaimCommand extends CustomCommand {
    public KingdomUnclaimCommand(String permission) {
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
        Chunk chunk = player.getLocation().getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        if(!chunk.getWorld().equals(KingdomClaim.WORLD))
            throw new CustomCommandException("this command can only be preformed in the overworld");
        if(!kingdom.equals(KingdomClaim.claimedBy(x, z)))
            throw new CustomCommandException("this chunk is not claimed by your kingdom");
        KingdomMember member = kingdom.getMembers().getMember(player);
        assert member != null;
        if(!member.hasPermission(KingdomMemberRank.KNIGHT))
            throw new CustomCommandException("you must have the rank of knight or higher to do this");
        if(kingdom.getClaim().isCoveredBySettlement(x, z))
            throw new CustomCommandException("this chunk is covered by a settlement, remove the settlement first");
        kingdom.getClaim().removeClaim(x, z);
        commandReturn.addReturnMessage(ChatColor.GREEN + "successfully removed claim on chunk");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
