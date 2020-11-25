package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.DragonProtect;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleEditMode extends CustomCommand {
    public ToggleEditMode(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        DragonProtect dp = Dragon_Realm.dragonProtect;
        dp.toggleEditMode(player);
        if(dp.isInEditMode(player)) DragonProtect.sendMessage(player, "you are now in edit mode");
        else DragonProtect.sendMessage(player, "you are now out of edit mode");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
