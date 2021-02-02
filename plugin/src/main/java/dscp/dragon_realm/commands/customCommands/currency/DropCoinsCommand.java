package dscp.dragon_realm.commands.customCommands.currency;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.currency.DroppedCoins;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DropCoinsCommand extends CustomCommand {
    public DropCoinsCommand() {
        super("currency drop", Perms.ADMIN);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("amount");
        params.addParameter("x");
        params.addParameter("y");
        params.addParameter("z");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        runForNonPlayer(player, commandReturn, params);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(sender instanceof BlockCommandSender){
            dropCoins(((BlockCommandSender) sender).getBlock().getLocation(), params);
        }else if(sender instanceof Player){
            dropCoins(((Player) sender).getLocation().add(-0.5, 0,-0.5), params);
            commandReturn.addReturnMessage(ChatColor.GREEN + "Dropped coins");
        }
        else{
            throw new CustomCommandException("Player Command / CommandBlock");
        }
    }

    private void dropCoins(Location loc, HashMap<String, String> params) throws CustomCommandException{
        if(!params.containsKey("amount")) throw new CustomCommandException("Must specify amount");
        Location location;
        if(params.containsKey("x")){
            if(params.containsKey("y") && params.containsKey("z")){
                double x,y,z;
                try{
                    x = Double.parseDouble(params.get("x"));
                    y = Double.parseDouble(params.get("y"));
                    z = Double.parseDouble(params.get("z"));
                    location = new Location(loc.getWorld(), x , y, z);
                }
                catch (NumberFormatException ex){
                    throw new CustomCommandException("Location variables must be numbers");
                }
            }
            else{
                throw new CustomCommandException("Missing location variables");
            }
        }
        else{
            location = loc;
        }
        int amount;
        try{
            amount = Integer.parseInt(params.get("amount"));
        }
        catch(NumberFormatException ex){
            throw new CustomCommandException("Amount must be number");
        }
        DroppedCoins.dropCoinsNaturally(location, amount);
    }
}
