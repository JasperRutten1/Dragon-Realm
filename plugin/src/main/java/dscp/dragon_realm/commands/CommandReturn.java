package dscp.dragon_realm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandReturn {
    List<String> returnMessages;
    List<Exception> exceptionList;
    CommandSender sneder;

    /**
     * constructor for a CommandReturn object
     * @param sender the player that this CommandReturn object is bound to
     */
    public CommandReturn(CommandSender sender){
        if(sender == null) throw new IllegalArgumentException("player can't be null");

        this.sneder = sender;
        this.returnMessages = new ArrayList<>();
        this.exceptionList = new ArrayList<>();
    }

    /**
     * Add a return message to the list containing the command return messages
     * @param message the message that will be added to the list
     * @return this object
     */
    public CommandReturn addReturnMessage(String message){
        if(message == null) throw new IllegalArgumentException("message can't be null");
        this.returnMessages.add(message);
        return this;
    }

    /**
     * Add a exception the the list containing all the exceptions thrown while executing the command
     * @param exception the exception that will be added to the list
     * @return this object
     */
    public CommandReturn addException(Exception exception){
        if(exception == null) throw new IllegalArgumentException("exception can't be null");
        this.exceptionList.add(exception);
        return this;
    }

    /**
     * Send the player the messages (strings) in the message reaction list
     * @return this object
     */
    public CommandReturn sendMessages(){
        if(this.returnMessages.isEmpty()) return this;

        for(String message : this.returnMessages){
            sneder.sendMessage(message);
        }
        return this;
    }

    /**
     * Send the player the message from the exceptions thrown executing the command
     * @return this object
     */
    public CommandReturn sendExceptionMessages(){
        if(exceptionList.isEmpty()) return this;
        for(Exception exception : this.exceptionList){
            sneder.sendMessage(ChatColor.RED + exception.getMessage());
        }
        return this;
    }

    /**
     * send the exception messages or the return messages to the player, will send return messages if no exceptions are thrown
     * @return this object
     */
    public CommandReturn sendExceptionsOrMessages(){
        if(this.exceptionList.isEmpty()){
            this.sendMessages();
        }else {
            this.sendExceptionMessages();
        }
        return this;
    }

}
