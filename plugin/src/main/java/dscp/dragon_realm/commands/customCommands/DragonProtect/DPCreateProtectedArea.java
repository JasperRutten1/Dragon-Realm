package dscp.dragon_realm.commands.customCommands.DragonProtect;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dragonProtect.areaProtect.ProtectedArea;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DPCreateProtectedArea extends CustomCommand {
    public DPCreateProtectedArea() {
        super("dp area create", Perms.DP_STAFF);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("name");
        params.addParameter("range");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        if(!params.containsKey("name")) throw new CustomCommandException("must enter name");
        if(!params.containsKey("range")) throw new CustomCommandException("must enter range");

        String name = Dragon_Realm_API.capitalizeFirstLetter(params.get("name"));
        int range = 0;
        try{
            range = Integer.parseInt(params.get("range"));
        }
        catch (NumberFormatException ex){
            throw new CustomCommandException("range must be number");
        }

        ProtectedArea areWithName = ProtectedArea.getArea(name);
        if(areWithName != null) throw new CustomCommandException("a protected area with this name already exists");

        Chunk chunk = player.getLocation().getChunk();
        ProtectedArea area = ProtectedArea.getArea(chunk);

        if(area == null){
            ProtectedArea newArea = new ProtectedArea(name, chunk, range);
            if(newArea.overlappingAreas().size() > 0)
                throw new CustomCommandException("area is overlapping with other area");
            ProtectedArea.addArea(newArea);
            player.sendMessage("Created new area " + newArea.getName());
        }
        else{
            ProtectedArea newSubArea = new ProtectedArea(name, chunk, range, area);
            if(!newSubArea.isFullyInsideOf(area)) throw new CustomCommandException("sub area must be fully inside parent area");
            if(newSubArea.overlapsWithSubs(area)) throw new CustomCommandException("sub area is overlapping with other sub areas");
            area.addSubArea(newSubArea);
            player.sendMessage("Created new sub area of " + area.getName() + ", " + newSubArea.getName());
        }
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
