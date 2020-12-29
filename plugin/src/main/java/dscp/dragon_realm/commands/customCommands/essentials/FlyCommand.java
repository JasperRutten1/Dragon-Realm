package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlyCommand extends CustomCommand {
    public FlyCommand() {
        super("fly", Perms.ESSENTIALS_STAFF);
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
                commandReturn.addReturnMessage(ChatColor.RED + "could not find player with this name");
                return;
            }
            else{
                if(target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR){
                    commandReturn.addReturnMessage(ChatColor.RED + "The targeted player is in gamemode "
                            + player.getGameMode().name());
                    return;
                }
                if(target.getAllowFlight()){
                    target.setAllowFlight(false);
                    commandReturn.addReturnMessage(ChatColor.AQUA + "Targeted player flight disabled");
                }
                else{
                    target.setAllowFlight(true);
                    commandReturn.addReturnMessage(ChatColor.AQUA + "Targeted player flight enabled");
                }
            }
        }
        else{
            if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR){
                commandReturn.addReturnMessage(ChatColor.RED + "This command can not be performed in "
                        + player.getGameMode().name());
                return;
            }
            if(player.getAllowFlight()){
                player.setAllowFlight(false);
                commandReturn.addReturnMessage(ChatColor.AQUA + "Flight disabled");
            }
            else{
                player.setAllowFlight(true);
                commandReturn.addReturnMessage(ChatColor.AQUA + "Flight enabled");
            }
        }

    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

    }
}
