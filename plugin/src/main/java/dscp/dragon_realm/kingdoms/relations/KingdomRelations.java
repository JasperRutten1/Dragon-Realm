package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingdomRelations implements Serializable {
    private static final long serialVersionUID = -3345511281010404841L;

    private Kingdom kingdom;
    private Map<Kingdom, Relation> relations;
    private KingdomAlliance alliance;
    private List<Kingdom> allianceInvites;

    public KingdomRelations(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.relations = new HashMap<>();
        this.allianceInvites = new ArrayList<>();
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Map<Kingdom, Relation> getRelations() {
        return relations;
    }

    public KingdomAlliance getAlliance() {
        return alliance;
    }

    public List<Kingdom> getAllianceInvites() {
        return allianceInvites;
    }

    public Relation getRelationToKingdom(Kingdom kingdom){
        Relation relation = relations.get(kingdom);
        if (relation == null) return Relation.NEUTRAL;
        if(alliance != null && alliance.getKingdoms().contains(kingdom)) return Relation.ALLIED;
        else return relation;
    }

    public KingdomRelations setRelationToKingdom(Kingdom kingdom, Relation relation){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        if(relation == null) throw new IllegalArgumentException("relation can't be null");
        if(relation != Relation.NEUTRAL){
            relations.put(kingdom, relation);
        }
        else{
            relations.remove(kingdom);
        }
        return this;
    }

    public List<Kingdom> getKingdomsWithRelation(Relation relation){
        List<Kingdom> kingdoms = new ArrayList<>();
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(getRelationToKingdom(kingdom) == relation && !this.kingdom.equals(kingdom)) kingdoms.add(kingdom);
        }
        return kingdoms;
    }

    public KingdomRelations improveRelation(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        Relation relation = getRelationToKingdom(kingdom);
        if(relation == Relation.ALLIED) throw new KingdomException("can not improve relation any further");
        else if(getRelationToKingdom(kingdom) == Relation.FRIENDLY){
            sendAllianceInvite(kingdom);
        }
        else{
            setRelationToKingdom(kingdom, getRelationToKingdom(kingdom).getRelationHigher());
        }
        return this;
    }

    public KingdomRelations worsenRelation(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        Relation relation = getRelationToKingdom(kingdom);
        if(relation == Relation.ENEMY) throw new KingdomException("can not worsen relation any further");
        setRelationToKingdom(kingdom, getRelationToKingdom(kingdom).getRelationLower());
        return this;
    }

    public List<Kingdom> getInvitedKingdoms(){
        List<Kingdom> kingdoms = new ArrayList<>();
        for(Kingdom kingdom : Kingdom.kingdoms){
            if(kingdom.getRelations().allianceInvites.contains(this.kingdom)) kingdoms.add(kingdom);
        }
        return kingdoms;
    }

    public KingdomRelations removeAllInvitations(){
        for(Kingdom kingdom : Kingdom.kingdoms){
            kingdom.getRelations().allianceInvites.remove(this.kingdom);
        }
        return this;
    }

    public KingdomRelations sendAllianceInvite(Kingdom kingdom) throws KingdomException {
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        if(getInvitedKingdoms().size() >= KingdomAlliance.MAXIMUM_INVITES)
            throw new KingdomException("you have send the maximum amount of alliance invitations \n" +
                    "remove invitations bore sending a new one");
        if(alliance != null){
            if(alliance.canBeInvited(kingdom)){
                if(alliance.getKingdoms().size() >= KingdomAlliance.MAXIMUM_MEMBERS)
                    throw new KingdomException("only " + KingdomAlliance.MAXIMUM_MEMBERS +
                            " kingdoms are allowed in a alliance");
                else kingdom.getRelations().allianceInvites.add(this.kingdom);
            }
            else{
                throw new KingdomException("this kingdom can not be invited \n" +
                        "kingdom must be friendly with all current members of the alliance");
            }
        }
        else{
            kingdom.getRelations().allianceInvites.add(this.kingdom);
        }
        return this;
    }

    public KingdomAlliance acceptAllianceInvite(Kingdom kingdom) throws KingdomException {
        if(!allianceInvites.contains(kingdom)) throw new KingdomException("your kingdom is not invited by this kingdom");
        if(kingdom.getRelations().getAlliance() != null){
            KingdomAlliance alliance = kingdom.getRelations().getAlliance();
            assert alliance != null;
            alliance.addKingdomToAlliance(this.kingdom);
            this.alliance = alliance;
        }
        else {
            KingdomAlliance alliance = new KingdomAlliance()
                    .addKingdomToAlliance(this.kingdom)
                    .addKingdomToAlliance(kingdom);
            this.alliance = alliance;
            kingdom.getRelations().alliance = alliance;
        }
        allianceInvites.remove(kingdom);
        kingdom.getRelations().removeAllInvitations();
        return this.alliance;
    }

    public KingdomAlliance leaveAlliance() throws KingdomException {
        if(alliance == null) throw new KingdomException("you are not in an alliance");
        KingdomAlliance alliance = this.alliance;
        alliance.getKingdoms().remove(this.kingdom);
        alliance.sendAllianceBroadcast(ChatColor.DARK_AQUA + kingdom.getName() + ChatColor.GOLD + " has left the alliance");
        if(alliance.getKingdoms().size() <= 1){
            alliance.sendAllianceBroadcast(ChatColor.GOLD + "Alliance disbanded");
            for(Kingdom k : alliance.getKingdoms()){
                k.getRelations().alliance = null;
            }
        }
        this.alliance = null;
        return alliance;
    }
}
