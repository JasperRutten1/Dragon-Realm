package dscp.dragon_realm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
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

    public abstract CommandReturn executeCommand(CommandSender sender, String commandName, String[] args);

    public abstract String getHelp();

    public void checkArgs(CommandSender sender, String commandName, String[] args){
        if(sender == null) throw new IllegalArgumentException("sender can't ve null");
        if(commandName == null) throw new IllegalArgumentException("command name can't be null");
        if(args == null) throw new IllegalArgumentException("args can't be null");
    }
}
