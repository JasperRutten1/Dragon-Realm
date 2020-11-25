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

public class UnAssignChunkFromProtectedZone extends CustomCommand {
    public UnAssignChunkFromProtectedZone(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        DragonProtect dp = Dragon_Realm.dragonProtect;
        ProtectedZone pz = dp.getZone(args[1]);
        Chunk chunk = player.getLocation().getChunk();

        if(pz == null) throw new CustomCommandException("could not find zone with this name");
        if(args.length > 2){
            try{
                int radius = Integer.parseInt(args[2]);
                int count = 0;
                for(Chunk c : KingdomClaim.getChunksInRadius(chunk, radius)){
                    if(pz.removeChunkFromZone(c)) count++;
                }
                commandReturn.addReturnMessage(ChatColor.GREEN + "removed " + ChatColor.DARK_AQUA + count
                        + ChatColor.GREEN + " chunks from zone");
            }
            catch (NumberFormatException ex){
                throw new CustomCommandException("2nd argument must be a number");
            }
        }
        else{
            if(pz.removeChunkFromZone(chunk)){
                commandReturn.addReturnMessage(ChatColor.GREEN + "removed chunk from zone");
            }
            else throw new CustomCommandException("can not remove chunk, chunk is not assigned to zone");
        }

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
