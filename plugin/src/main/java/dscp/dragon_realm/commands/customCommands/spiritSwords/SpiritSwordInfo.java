package dscp.dragon_realm.commands.customCommands.spiritSwords;

import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.specialWeapons.spiritSwords.SpiritSword;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpiritSwordInfo extends CustomCommand {

    public SpiritSwordInfo(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player.");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        //code
        player.openBook(SpiritSword.getInfoBook());

        //return
        return commandReturn;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
