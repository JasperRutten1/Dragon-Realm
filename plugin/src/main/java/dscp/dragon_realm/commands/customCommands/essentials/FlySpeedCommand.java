package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FlySpeedCommand extends CustomCommand {
    public FlySpeedCommand() {
        super("flyspeed", Perms.ESSENTIALS_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("speed");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        try{
            if(params.containsKey("speed")){
                float speed = Float.parseFloat(params.get("speed"))/10;
                if(speed > 10) throw new CustomCommandException("Maximum speed is 10.");
                player.setFlySpeed(speed);
                commandReturn.addReturnMessage(ChatColor.AQUA + "Flight speed successfully set to " + speed);
            }
            else commandReturn.addReturnMessage(ChatColor.AQUA + "Your current flightspeed is " + player.getFlySpeed());
        }
        catch (NumberFormatException e){
            throw new CustomCommandException("Argument must be a number.");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
