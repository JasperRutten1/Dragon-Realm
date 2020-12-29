package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class HealCommand extends CustomCommand {
    public HealCommand() {
        super("heal", Perms.ESSENTIALS_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(params.containsKey("target")){
            Player target = Bukkit.getPlayer(params.get("target"));
            if(target == null){
                commandReturn.addReturnMessage(ChatColor.RED + "Could not find player with this name");
                return;
            }
            else{
                target.setHealth(20);
                target.setSaturation(20);
                target.setFoodLevel(20);
                commandReturn.addReturnMessage(ChatColor.GOLD + "Healed " + target.getName());
            }
        }
        else{
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            commandReturn.addReturnMessage(ChatColor.GOLD + "Healed");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
