package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RemoveProtectedZone extends CustomCommand {
    public RemoveProtectedZone() {
        super("dp zone remove", Perms.DP_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("name");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        runForNonPlayer(player, commandReturn, params);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {

        if(!params.containsKey("name")){
            commandReturn.addReturnMessage(ChatColor.RED + "Missing arguments, usage: /dp zone remove");
            return;
        }

        String name = params.get("name");

        DragonProtect dp = Dragon_Realm.dragonProtect;
        if(!dp.zoneExists(name)) throw new CustomCommandException("could not find zone with this name");
        dp.removeZone(name);
        commandReturn.addReturnMessage(ChatColor.GREEN + "removed zone");
    }
}
