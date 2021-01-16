package dscp.dragon_realm.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class CustomCommand {
    private String[] commandArgs;
    private String permission;
    private CommandParams parameters;

    public CustomCommand(String commandString, String permission){
        if(commandString == null) throw new IllegalArgumentException("command string can't be null");
        if(permission == null) throw new IllegalArgumentException("Permission can not be null.");

        this.commandArgs = commandString.split(" ");
        this.permission = permission;
        this.parameters = loadParams();
    }

    public CustomCommand(String commandString, Perms perm){
        this(commandString, perm.toString());
    }

    //getters

    public String getPermission(){
        return permission;
    }

    public String[] getCommandArgs() {
        return commandArgs;
    }

    public CommandParams getParameters() {
        return parameters;
    }

    //parameters

    private CommandParams loadParams(){
        CommandParams params = new CommandParams();
        parameters(params);
        return params;
    }

    public abstract void parameters(CommandParams params);

    //command execution

    public void runCommand(CommandSender sender, HashMap<String, String> params){
        if(!sender.hasPermission(permission)){
            sender.sendMessage("you do not have permission to use this command");
            return;
        }

        CommandReturn cr = new CommandReturn(sender);

        try{
            if(sender instanceof Player){
                Player player = (Player) sender;
                runForPlayer(player, cr, params);
            }else{
                runForNonPlayer(sender, cr, params);
            }
        }
        catch (CustomCommandException ex){
            sender.sendMessage(ex.getMessage());
        }

        cr.sendExceptionsOrMessages();
    }

    public abstract void runForPlayer(Player player, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException;

    public abstract void runForNonPlayer(CommandSender sender, CommandReturn commandReturn, HashMap<String, String> params) throws CustomCommandException;

    public enum Perms{
        KINGDOM_DEFAULT("dscp.dr.kingdom.default"),

        ESSENTIALS_STAFF("dscp.dr.essentials.staff"),

        DP_STAFF("dscp.dp.staff"),
        DP_EDIT("dscp.dp.edit"),

        SS_CREATE("dscp.dr.ss.create"),

        PLAYER_DATA("dscp.dr.pd"),

        ADMIN("dscp.dr.admin");

        String perm;

        Perms(String perm){
            this.perm = perm;
        }

        @Override
        public String toString() {
            return perm;
        }
    }

    public class CommandParams {
        private ArrayList<String> parameters;

        public CommandParams(){
            this.parameters = new ArrayList<>();
        }

        public void addParameter(String name){
            this.parameters.add(name);
        }

        public int count(){
            return parameters.size();
        }

        public ArrayList<String> getParameters() {
            return parameters;
        }
    }

}
