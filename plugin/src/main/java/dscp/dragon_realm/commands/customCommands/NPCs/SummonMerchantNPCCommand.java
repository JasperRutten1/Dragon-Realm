package dscp.dragon_realm.commands.customCommands.NPCs;

import dscp.dragon_realm.NPCs.merchants.Merchant;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SummonMerchantNPCCommand extends CustomCommand {
    public SummonMerchantNPCCommand() {
        super("npc merchant", Perms.ADMIN);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("id");
        params.addParameter("noAi");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(!params.containsKey("id")) throw new CustomCommandException("must give merchant type id");
        int type;
        try{
            type = Integer.parseInt(params.get("id"));
        }
        catch (NumberFormatException ex){
            throw new CustomCommandException("Type ID must be number");
        }
        boolean noAi = false;
        if(params.containsKey("noAi")){
            String ans = params.get("noAi");
            if(ans.equalsIgnoreCase("true") || ans.equalsIgnoreCase("false")){
                if(ans.equalsIgnoreCase("true")){
                    noAi = true;
                }
            }
            else{
                throw new CustomCommandException("noAi must be true or false");
            }
        }

        Merchant.summonMerchant(player.getLocation(), type, noAi);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
