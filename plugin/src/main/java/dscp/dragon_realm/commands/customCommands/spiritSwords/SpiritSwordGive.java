package dscp.dragon_realm.commands.customCommands.spiritSwords;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpiritSwordGive extends CustomCommand {
    public SpiritSwordGive() {
        super("ss give", Perms.SS_CREATE);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("target");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(params.containsKey("target")){
            giveTargetSword(commandReturn, params);
        }else{
            SpiritSword.createNewSpiritSword(player);
            commandReturn.addReturnMessage("you received a spirit sword");
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(params.containsKey("target")){
            giveTargetSword(commandReturn, params);
        }else{
            throw new CustomCommandException("can not give item to non player");
        }
    }

    private void giveTargetSword(CommandReturn commandReturn, HashMap<String, String> params) {
        Player target = Bukkit.getPlayer(params.get("target"));
        if(target == null){
            commandReturn.addReturnMessage(ChatColor.RED + "could not find player with this name");
            return;
        }
        SpiritSword.createNewSpiritSword(target);
        commandReturn.addReturnMessage(target.getName() + " received a spirit sword");
        target.sendMessage("you received a spirit sword");
    }
}
