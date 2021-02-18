package dscp.dragon_realm.commands.customCommands.menus;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.menus.PlayerInfoMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerMenuCommand extends CustomCommand {
    public PlayerMenuCommand() {
        super("menu", Perms.KINGDOM_DEFAULT);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        Player target;
        if(params.containsKey("target")){
            target = Bukkit.getPlayer(params.get("target"));
            if(target == null) throw new CustomCommandException("Could not find player with this name");
        }else{
            target = player;
        }

        new PlayerInfoMenu(target, null).open(player);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
