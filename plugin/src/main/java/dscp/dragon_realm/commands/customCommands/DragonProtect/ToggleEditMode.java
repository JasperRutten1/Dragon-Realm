package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ToggleEditMode extends CustomCommand {
    public ToggleEditMode() {
        super("dp edit", Perms.DP_EDIT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        DragonProtect dp = Dragon_Realm.dragonProtect;

        if(params.containsKey("target")){
            Player target = Bukkit.getPlayer(params.get("target"));
            if(target == null){
                commandReturn.addReturnMessage(ChatColor.RED + "Could not find this player");
                return;
            }
            dp.toggleEditMode(target);
            if(dp.isInEditMode(target)) DragonProtect.sendMessage(player, "Player is now in edit mode");
            else DragonProtect.sendMessage(target, "Player is now out of edit mode");
        }else{
            dp.toggleEditMode(player);
            if(dp.isInEditMode(player)) DragonProtect.sendMessage(player, "you are now in edit mode");
            else DragonProtect.sendMessage(player, "you are now out of edit mode");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
