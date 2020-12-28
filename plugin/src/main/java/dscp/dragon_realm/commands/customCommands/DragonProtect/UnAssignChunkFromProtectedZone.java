package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.dragonProtect.ProtectedZone;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class UnAssignChunkFromProtectedZone extends CustomCommand {
    public UnAssignChunkFromProtectedZone() {
        super("dp unassign", Perms.DP_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("zone");
        params.addParameter("radius");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("zone")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing arguments, usage: /dp unassign [(opt) radius]");
            return;
        }

        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pz = dp.getZone(params.get("zone"));
        Chunk chunk = player.getLocation().getChunk();

        if(pz == null) throw new CustomCommandException("could not find zone with this name");
        if(params.containsKey("radius")){
            try{
                int radius = Integer.parseInt(params.get("radius"));
                int count = 0;
                for(Chunk c : KingdomClaim.getChunksInRadius(chunk, radius)){
                    if(pz.removeChunkFromZone(c)) count++;
                }
                commandReturn.addReturnMessage(ChatColor.GREEN + "removed " + ChatColor.DARK_AQUA + count
                        + ChatColor.GREEN + " chunks from zone");
            }
            catch (NumberFormatException ex){
                throw new CustomCommandException("radius must be a number");
            }
        }
        else{
            if(pz.removeChunkFromZone(chunk)){
                commandReturn.addReturnMessage(ChatColor.GREEN + "removed chunk from zone");
            }
            else throw new CustomCommandException("can not remove chunk, chunk is not assigned to zone");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
