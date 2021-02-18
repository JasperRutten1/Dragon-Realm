package dscp.dragon_realm.kingdoms.bank;

public class VaultException extends Exception{
    public VaultException(String message, Exception e){
        super(message, e);
    }

    public VaultException(String message){
        super(message);
    }
}
