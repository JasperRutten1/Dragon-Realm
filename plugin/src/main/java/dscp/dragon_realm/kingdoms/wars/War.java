package dscp.dragon_realm.kingdoms.wars;

import dscp.dragon_realm.kingdoms.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class War implements Serializable {
    private static final long serialVersionUID = -3756911838843813543L;

    private WarReason reason;
    private WarSide attackers;
    private WarSide defenders;

    public static double GRACE_PERIOD = 1800000; //30 minutes
    private long gracePeriodStart;

    public War(Kingdom attacker, Kingdom defender, WarReason reason) throws WarException{
        if(attacker == null) throw new IllegalArgumentException("kingdoms must both be non null");
        if(defender == null) throw new IllegalArgumentException("kingdoms must both be non null");
        if(reason == null) throw new IllegalArgumentException("reason can't be null");

        this.attackers = new WarSide(attacker);
        this.defenders = new WarSide(defender);
        this.reason = reason;

        for(Kingdom kingdom : getInvolvedKingdoms()){
            if(kingdom.getRelations().getWarManager().atWar()) throw new WarException("exception in creating war object");
            kingdom.getRelations().getWarManager().setCurrentWar(this);
        }
    }

    public static ArrayList<War> getAllWars(){
        ArrayList<War> wars = new ArrayList<>();
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getRelations().getWarManager().atWar() && !wars.contains(kingdom.getRelations().getWarManager().getCurrentWar())){
                wars.add(kingdom.getRelations().getWarManager().getCurrentWar());
            }
        }
        return wars;
    }

    public ArrayList<Kingdom> getInvolvedKingdoms(){
        ArrayList<Kingdom> involvedKingdoms = new ArrayList<>();
        involvedKingdoms.addAll(attackers.getKingdomsOnSide());
        involvedKingdoms.addAll(defenders.getKingdomsOnSide());
        return involvedKingdoms;
    }

    //sides

    public WarSide getAttackers() {
        return attackers;
    }

    public WarSide getDefenders() {
        return defenders;
    }

    public WarSide getSide(Kingdom kingdom){
        if(attackers.isOnSide(kingdom)) return attackers;
        else if(defenders.isOnSide(kingdom)) return defenders;
        else return null;
    }

    public enum WarReason {
        TERRITORY("territory", "A war for territory");

        private String name;
        private String message;

        WarReason(String name, String message){
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }
    }

    public void warStartMessage(){
        defenders.getKingdom().sendMembersMessage(ChatColor.GOLD + "War has been declared against our kingdom");
        for(Kingdom k : defenders.getKingdomsOnSide()){
            if(!k.equals(defenders.getKingdom()))
                k.sendMembersMessage(ChatColor.GOLD + "War has been declared against our ally");
        }

        for(Kingdom k : attackers.getKingdomsOnSide()){
            k.sendMembersMessage(ChatColor.GOLD + "We have declared war!");
        }
        Bukkit.broadcastMessage(toString());

        Bukkit.broadcastMessage("The grace period of " + ((int) (GRACE_PERIOD/60000)) + " minutes has begun." );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("============ War Declaration ============").append("\n");
        sb.append(ChatColor.GOLD).append("Attackers: ").append("\n");
        sb.append(attackers.toString());
        sb.append(ChatColor.GOLD).append("War goal: ").append("\n");
        sb.append(reason.message).append("\n");
        sb.append(ChatColor.GOLD).append("Defenders: ").append("\n");
        sb.append(defenders.toString());
        sb.append("=========================================");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof War)) return false;
        War war = (War) o;
        return reason == war.reason && Objects.equals(attackers, war.attackers) && Objects.equals(defenders, war.defenders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, attackers, defenders);
    }
}
