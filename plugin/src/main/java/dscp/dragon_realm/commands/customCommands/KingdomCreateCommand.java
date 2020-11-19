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
        if(!(sender instanceof Player)) throw new CustomCommandException("Sender must be of type player");
        Player player = (Player) sender;
        CommandReturn commandReturn = new CommandReturn(player);

        if(Kingdom.isMemberOfKingdom(player)) throw new CustomCommandException("You are already a member of a Kingdom.");
        if(args[1].length() > 15) throw new CustomCommandException("A Kingdom may not have a name that is greater than 15 characters.");
        if(Kingdom.getKingdomFromName(args[1]) != null)
            throw new CustomCommandException("A Kingdom with this name already exists, please try another name.");
        Kingdom kingdom = Kingdom.createKingdom(args[1], player);
        commandReturn.addReturnMessage(ChatColor.GREEN + "Created Kingdom with the name " + ChatColor.GOLD
                + kingdom.getName());
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.GOLD + " Created the Kingdom of '"
                    + ChatColor.DARK_AQUA + kingdom.getName() + ChatColor.GOLD + "'");
        }

        return commandReturn;
    }

    @Override
    public String getHelp() {
        return new CommandHelpGenerator("/kingdom create [name]", "Create a new Kingdom.")
                .addArgument("name", "name of the Kingdom")
                .generateHelp();
    }
}
