package dscp.dragon_realm.specialWeapons.spiritSwords.abilities;

public class AbilityException extends Exception{
    AbilityException(String message, Exception e){
        super(message, e);
    }

    public AbilityException(String message){
        super(message);
    }
}
