package dscp.dragon_realm.kingdoms.wars;

public class WarException extends Exception{
    public WarException(String message, Exception e){
        super(message, e);
    }

    public WarException(String message){
        super(message);
    }
}
