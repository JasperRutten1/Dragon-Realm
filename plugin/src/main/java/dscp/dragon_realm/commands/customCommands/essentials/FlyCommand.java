package dscp.dragon_realm.commands.customCommands.essentials;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand extends CustomCommand {
    public FlyCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR){
            commandReturn.addReturnMessage(ChatColor.RED + "this command can not be preformed in "
                    + player.getGameMode().name());
            return commandReturn;
        }
        if(player.getAllowFlight()){
            player.setAllowFlight(false);
            commandReturn.addReturnMessage(ChatColor.AQUA + "flying disabled");
        }
        else{
            player.setAllowFlight(true);
            commandReturn.addReturnMessage(ChatColor.AQUA + "you can now fly");
        }

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
