package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.wars.WarManager;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class KingdomRelations implements Serializable {
    private static final long serialVersionUID = -3345511281010404841L;

    private Kingdom kingdom;
    private Map<Kingdom, Double> relations;
    private KingdomAlliance alliance;
    private Kingdom invitedKingdom;
    private WarManager warManager;

    public KingdomRelations(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.relations = new HashMap<>();
        this.warManager = new WarManager(this.kingdom);
    }

    public void setAlliance(KingdomAlliance alliance) {
        this.alliance = alliance;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public KingdomAlliance getAlliance() {
        return alliance;
    }

    public WarManager getWarManager() {
        return warManager;
    }

    //relations

    public double getRelationScore(Kingdom kingdom){
        if(relations.containsKey(kingdom)){
            return relations.get(kingdom);
        }
        else return 0.0;
    }

    public void improveRelationScore(Kingdom kingdom, double score){
        double currentScore = getRelationScore(kingdom);
        relations.put(kingdom, Math.min(currentScore + Math.abs(score), Relation.getHighestRelation().neededScore + Relation.BOUND) );
    }

    public void worsenRelationScore(Kingdom kingdom, double score){
        double currentScore = getRelationScore(kingdom);
        relations.put(kingdom, Math.max(currentScore - Math.abs(score), Relation.getLowestRelation().neededScore - Relation.BOUND) );
    }

    public Relation getRelation(Kingdom kingdom){
        if(!relations.containsKey(kingdom)) return Relation.NEUTRAL;
        else return Relation.getRelationFromScore(relations.get(kingdom));
    }

    //alliance

    public boolean hasAlliance(){
        return this.alliance != null;
    }

    public boolean alliedWith(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't tbe null");
        if(hasAlliance()){
            if(this.kingdom.equals(kingdom)) return false;
            return alliance.getKingdoms().contains(kingdom);
        }
        else return false;
    }

    public void createAllianceWith(Kingdom kingdom){
        if(hasAlliance()) return;
        KingdomAlliance alliance = new KingdomAlliance();
        alliance.addKingdom(this.getKingdom());
        alliance.addKingdom(kingdom);
        alliance.bindAlliance();
    }

    public void leaveAlliance(){
        this.alliance.removeKingdom(this.kingdom);
    }

    public boolean hasInvitedKingdom(){
        if(hasAlliance()){
            return alliance.hasInvitedKingdom();
        }
        else{
            return invitedKingdom != null;
        }
    }

    public void setInvitedKingdom(Kingdom invitedKingdom) {
        this.invitedKingdom = invitedKingdom;
    }

    public void inviteKingdom(Kingdom kingdom){
        if(hasAlliance()){
            if(alliance.hasInvitedKingdom()) return;
            else alliance.setInvitedKingdom(kingdom);
            kingdom.sendMembersMessage("You have a new Alliance invitation from the "
                    + ChatColor.DARK_AQUA + alliance.getName() + " alliance");
        }
        else{
            if(hasInvitedKingdom()) return;
            else setInvitedKingdom(kingdom);
            kingdom.sendMembersMessage("You have a new Alliance invitation from the "
                    + ChatColor.DARK_AQUA + this.kingdom.getName() + " Kingdom");
        }
    }
}
