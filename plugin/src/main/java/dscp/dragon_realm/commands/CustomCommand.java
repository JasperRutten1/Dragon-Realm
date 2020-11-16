package dscp.dragon_realm.commands;

import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class CustomCommand {
    private String permission;

    public CustomCommand(String permission){
        if(permission == null) throw new IllegalArgumentException("permission can't be null");

        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }

    public CommandReturn executeCommand(CommandSender sender, String commandName, String[] args){
        checkArgs(sender, commandName, args);
        try {
            return runCommandCode(sender, commandName, args);
        }catch (Exception e){
            e.printStackTrace();
            return new CommandReturn(sender).addException(e);
        }
    }

    public abstract CommandReturn runCommandCode(CommandSender sender, String commandName, String[] args) throws KingdomException, CustomCommandException;

    public abstract String getHelp();

    public void checkArgs(CommandSender sender, String commandName, String[] args){
        if(sender == null) throw new IllegalArgumentException("sender can't ve null");
        if(commandName == null) throw new IllegalArgumentException("command name can't be null");
        if(args == null) throw new IllegalArgumentException("args can't be null");
    }
}
