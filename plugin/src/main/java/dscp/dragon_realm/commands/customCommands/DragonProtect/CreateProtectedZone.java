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

public class CreateProtectedZone extends CustomCommand {

    public CreateProtectedZone(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        DragonProtect dp = Dragon_Realm.dragonProtect;
        if(dp.zoneExists(args[2])) throw new CustomCommandException("a zone with this name already exists");
        dp.createNewZone(args[2]);
        commandReturn.addReturnMessage(ChatColor.GREEN + "created new protected zone \nassign chunks to it with /dp assign [zone name] [radius]");

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
