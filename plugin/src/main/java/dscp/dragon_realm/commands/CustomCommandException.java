package dscp.dragon_realm.commands;

public class CustomCommandException extends Exception{
    public CustomCommandException(String message, Exception e){
        super(message, e);
    }
    public CustomCommandException(String message){
        super(message);
    }
}
