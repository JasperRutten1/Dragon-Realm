package dscp.dragon_realm.kingdoms.wars;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.ArrayList;

public class WarManager implements Serializable {
    private static final long serialVersionUID = -2172703462770636271L;
    private Kingdom kingdom;

    private War currentWar;
    private ArrayList<WarStats> stats;

    public WarManager(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.stats = new ArrayList<>();
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public War getCurrentWar() {
        return currentWar;
    }

    public void setCurrentWar(War currentWar) {
        this.currentWar = currentWar;
    }

    public boolean atWar(){
        return currentWar != null;
    }

    public boolean isAtWarWith(Kingdom kingdom){
        if(!atWar()) return false;
        return !(currentWar.getSide(kingdom) == null);
    }

    //declaration

    public void warChecks(Kingdom otherKingdom) throws WarException{
        if(this.kingdom.getRelations().hasAlliance()){
            //checks
            if(this.kingdom.getRelations().alliedWith(otherKingdom)) throw new WarException("Can not go to war With allied kingdom");
            if(isAtWarWith(otherKingdom)) throw new WarException("Your alliance is already at war with this kingdom");
            if(this.kingdom.getRelations().getAlliance().atWar()) throw new WarException("Your alliance is already at war");
            if(otherKingdom.getRelations().hasAlliance()){
                if(otherKingdom.getRelations().getAlliance().atWar()) throw new WarException("The other kingdom is already at war");
            }
            else{
                if(otherKingdom.getRelations().getWarManager().atWar()) throw new WarException("The other kingdom is already at war");
            }
        }
        else{
            //checks
            if(isAtWarWith(otherKingdom)) throw new WarException("Your kingdom is already at war with this kingdom");
            if(otherKingdom.getRelations().hasAlliance()){
                if(otherKingdom.getRelations().getAlliance().atWar()) throw new WarException("The other kingdom is already at war");
            }
            else{
                if(otherKingdom.getRelations().getWarManager().atWar()) throw new WarException("The other kingdom is already at war");
            }
        }
    }

    public void declareWar(Kingdom otherKingdom, War.WarReason reason){
        try{
            warChecks(otherKingdom);

            if(this.kingdom.getRelations().hasAlliance()){
                sendWarProposal(otherKingdom, reason);
            }
            else{
                startWarBetween(this.kingdom, otherKingdom, reason);
            }
        }
        catch (WarException ex){
            kingdom.sendMembersMessage(ChatColor.RED + ex.getMessage());
        }
    }

    public void sendWarProposal(Kingdom target, War.WarReason reason){
        WarProposal proposal = new WarProposal(this.kingdom, target, reason);
        this.kingdom.getRelations().getAlliance().setWarProposal(proposal);
    }

    public static void startWarBetween(Kingdom attacker, Kingdom defender, War.WarReason reason) throws WarException{
        attacker.getRelations().getWarManager().warChecks(defender);
        War war = new War(attacker, defender, reason);
    }

    public static void warCodeSchedule(){
        for(War war : War.getAllWars()){

        }
    }
}
