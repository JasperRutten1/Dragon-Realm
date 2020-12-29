package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CreateProtectedZone extends CustomCommand {

    public CreateProtectedZone() {
        super("dp zone create", Perms.DP_STAFF);
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
            commandReturn.addReturnMessage(ChatColor.RED + "Missing arguments, usage: /dr zone create [name]");
            return;
        }

        String name = params.get("name");

        DragonProtect dp = Dragon_Realm.dragonProtect;
        if(dp.zoneExists(name)) throw new CustomCommandException("a zone with this name already exists");
        dp.createNewZone(name);
        commandReturn.addReturnMessage(ChatColor.GREEN + "created new protected zone \nassign chunks to it with /dp assign [zone name] [radius]");
    }
}
