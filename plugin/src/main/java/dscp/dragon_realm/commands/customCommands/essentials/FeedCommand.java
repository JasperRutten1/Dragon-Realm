package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends CustomCommand {
    public FeedCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.-");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        player.setSaturation(20);
        player.setFoodLevel(20);

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
