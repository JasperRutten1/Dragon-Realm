package dscp.dragon_realm.commands.customCommands;

import dscp.dragon_realm.commands.CommandHelpGenerator;
import dscp.dragon_realm.commands.CommandReturn;
import dscp.dragon_realm.commands.CustomCommand;
import dscp.dragon_realm.commands.CustomCommandException;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingdomCreateCommand extends CustomCommand {
    public KingdomCreateCommand(String permission) {
        super(permission);
    }

    @Override
    public CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException {
        if(!(sender instanceof Player)) throw new CustomCommandException("sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        if(Kingdom.isMemberOfKingdom(player)) throw new CustomCommandException("you are already a member of a kingdom");
        if(args[1].length() > 15) throw new CustomCommandException("a kingdom name can only be 15 character long");
        if(Kingdom.getKingdomFromName(args[1]) != null)
            throw new CustomCommandException("a kingdom with this name already exists");
        Kingdom kingdom = Kingdom.createKingdom(args[1], player);
        commandReturn.addReturnMessage(ChatColor.GREEN + "created kingdom with name " + ChatColor.GOLD
                + kingdom.getName());
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + " created the kingdom of '"
                    + ChatColor.DARK_AQUA + kingdom.getName() + ChatColor.GOLD + "'");
        }

        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom create [name]", "create a new kingdom")
                .addArgument("name", "name of the kingdom")
                .generateHelp();
    }
}
