package dscp.dragon_realm.commands;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class CommandHelpGenerator {
    private String command;
    private String explanation;
    private Map<String, String> arguments;

    public CommandHelpGenerator(String command, String explanation){
        if(command == null) throw new IllegalArgumentException("command can't be null");
        if(explanation == null) throw new IllegalArgumentException("explanation can't be null");
        this.command = command;
        this.explanation = explanation;
        this.arguments = new HashMap<>();
    }

    public CommandHelpGenerator addArgument(String argument, String explanation){
        if(this.arguments.containsKey(argument)) throw new IllegalArgumentException("a command can not have 2 of the same arguments");
        this.arguments.put(argument, explanation);
        return this;
    }

    public String generateHelp(){
        StringBuilder sb = new StringBuilder();

        sb.append(ChatColor.GOLD).append("Dragon Realm help").append("\n")
                .append("\n")
                .append("Command: ").append(ChatColor.DARK_AQUA).append(this.command).append("\n")
                .append(ChatColor.RESET).append(this.explanation)
                .append("\n");

        if(!arguments.isEmpty()){
            sb.append(ChatColor.GOLD).append("Arguments: ").append("\n");
            for(Map.Entry<String, String> entry : arguments.entrySet()){
                sb.append(ChatColor.RED).append(entry.getKey()).append(ChatColor.RESET).append(" - ").append(entry.getValue());
            }
        }

        return sb.toString();
    }
}
