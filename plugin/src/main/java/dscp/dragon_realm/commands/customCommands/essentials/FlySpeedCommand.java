package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlySpeedCommand extends CustomCommand {
    public FlySpeedCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        try{
            float speed = Float.parseFloat(args[0])/10;
            if(speed > 10) throw new CustomCommandException("maximum speed is 10");
            player.setFlySpeed(speed);
            commandReturn.addReturnMessage(ChatColor.AQUA + "set fly speed to " + speed);
        }
        catch (NumberFormatException e){
            throw new CustomCommandException("argument must be number");
        }

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
