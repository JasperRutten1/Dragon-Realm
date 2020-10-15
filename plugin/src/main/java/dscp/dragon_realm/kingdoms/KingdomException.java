package dscp.dragon_realm.kingdoms;

public class KingdomException extends Exception{
    public KingdomException(String message, Exception e){
        super(message, e);
    }

    public KingdomException(String message){
        super(message);
    }
}
