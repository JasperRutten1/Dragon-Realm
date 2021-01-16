package dscp.dragon_realm.commands.customCommands.PlayerData;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.dataContainer.DataContainerDataType;
import dscp.dragon_realm.dataContainer.ObjectData;
import dscp.dragon_realm.dataContainer.PlayerDataContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MinedCommand extends CustomCommand {
    public MinedCommand() {
        super("pd mined", Perms.PLAYER_DATA);
    }

    @Override
    public void parameters(CommandParams params) {
        params.addParameter("type");
    }

    @Override
    public void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        PlayerDataContainer dataContainer = PlayerDataContainer.getPlayerData(player.getUniqueId());
        if(dataContainer == null) return;

        ObjectData<Integer> diamondObjectData =
                dataContainer.loadObjectDataFromContainer(DataContainerDataType.IntegerType, "diamonds-mined");
        int diamonds;
        if(diamondObjectData == null) diamonds = 0;
        else diamonds = diamondObjectData.getObject();

        player.sendMessage("diamonds mine: " + diamonds);
    }

    @Override
    public void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException {
        throw new CustomCommandException("player command");
    }
}
