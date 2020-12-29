package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.Dragon_Realm_API;
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

public class AssignChunkToProtectedZone extends CustomCommand {
    public AssignChunkToProtectedZone() {
        super("dp assign", Perms.DP_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("zone");
        params.addParameter("radius");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("zone")){
            commandReturn.addReturnMessage(ChatColor.RED + "missing arguments, usage: /dp assign [zone name] [(opt) radius]");
            return;
        }

        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pz = dp.getZone(Dragon_Realm_API.capitalizeFirstLetter(params.get("zone")));
        if(pz == null) throw new CustomCommandException("could not find zone with this name");
        Chunk chunk = player.getLocation().getChunk();
        if(params.containsKey("range")){
            //range given
            try{
                Integer radius = Integer.parseInt(params.get("radius"));
                if(radius > 15) throw new CustomCommandException("the maximum radius is 15");
                int count = 0, fails = 0;
                for(Chunk c : KingdomClaim.getChunksInRadius(chunk, radius)){
                    if(dp.getZone(c) == null){
                        pz.addChunkToZone(c);
                        count++;
                    }
                    else fails++;
                }
                commandReturn.addReturnMessage(ChatColor.GREEN + "successfully assigned " + ChatColor.DARK_AQUA + count
                        + ChatColor.GREEN + " chunks to zone " + ChatColor.DARK_AQUA + pz.getName());
                if(fails > 0) commandReturn.addReturnMessage(ChatColor.RED + "failed to assign " + ChatColor.DARK_RED
                        + fails + ChatColor.RED + " chunks");
            }
            catch (NumberFormatException ex){
                throw new CustomCommandException("range must be an number");
            }
        }
        else{
            //no range given
            if(dp.getZone(chunk) != null) throw new CustomCommandException("this chunk is already assigned to another zone");
            pz.addChunkToZone(chunk);
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
