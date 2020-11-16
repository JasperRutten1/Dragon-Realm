package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandHelpGenerator;
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

public class KingdomClaimCommand extends CustomCommand {
    public KingdomClaimCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        if(!player.getWorld().getName().equals("world"))
            throw new CommandException("this command can only be performed in the overworld");
        Kingdom kingdom = Kingdom.getKingdomFromPlayer(player);
        if(kingdom == null)
            throw new CommandException("you are not part of a kingdom");
        KingdomMember member = kingdom.getMembers().getMember(player);
        assert member != null;
        if(!member.hasPermission(KingdomMemberRank.KNIGHT))
            throw new CommandException("you need to have the rank of Knight or higher to perform this command");
        Chunk chunk = player.getLocation().getChunk();
        if(args.length == 1){
            if(KingdomClaim.isClaimed(chunk.getX(), chunk.getZ()))
                throw new CommandException("chunk is already claimed");
            kingdom.getClaim().claimChunk(chunk.getX(), chunk.getZ());
            commandReturn.addReturnMessage(ChatColor.GREEN + "successfully claimed chunk");
        }
        else if(args.length == 2){
            int r = 0;
            int count = 0;
            try{
                r = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e){
                throw new CustomCommandException("2nd argument must be a number");
            }
            for(Chunk c : KingdomClaim.getChunksInRadius(chunk.getX(), chunk.getZ(), r)){
                if(kingdom.getClaim().claimChunk(c.getX(), c.getZ())) count++;
            }
            commandReturn.addReturnMessage(ChatColor.GREEN + "claimed " + count + " chunks");
        }


        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom claim", "claim a chunk for your kingdom")
                .generateHelp();
    }
}
