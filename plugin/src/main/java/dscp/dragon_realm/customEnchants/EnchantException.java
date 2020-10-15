package dscp.dragon_realm.customEnchants;

public class EnchantException extends Exception{
    public EnchantException(String message, Exception e){
        super(message, e);
    }

    public EnchantException(String message){
        super(message);
    }
}
