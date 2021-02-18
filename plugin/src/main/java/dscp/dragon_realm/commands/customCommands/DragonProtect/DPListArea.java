package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DPListArea extends CustomCommand {
    public DPListArea() {
        super("dp area list", Perms.DP_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {

    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        runForNonPlayer(player, commandReturn, params);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
       sender.sendMessage(ProtectedArea.getAreaContainer().toString());
    }
}
