package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.Kingdom;

import java.io.Serializable;
import java.util.*;

public class KingdomAlliance implements Serializable {
    private static final long serialVersionUID = 6986846034456483371L;

    private List<Kingdom> kingdoms;
    private String name;
    private Map<Kingdom, Long> joinTimeMap;
    private Kingdom invitedKingdom;

    public static int MAXIMUM_MEMBERS = 4;

    public KingdomAlliance(){
        this.kingdoms = new ArrayList<>();
        this.joinTimeMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public List<Kingdom> getKingdoms() {
        return kingdoms;
    }

    //kingdoms

    public void addKingdom(Kingdom kingdom){
        if(hasMaxMembers()) return;
        if(kingdoms.contains(kingdom)) return;
        kingdoms.add(kingdom);
        this.joinTimeMap.put(kingdom, System.currentTimeMillis());
    }

    public boolean hasMaxMembers(){
        return kingdoms.size() >= MAXIMUM_MEMBERS;
    }

    public void removeKingdom(Kingdom kingdom){
        kingdoms.remove(kingdom);
        if(kingdoms.size() <= 1){
            removeAlliance();
        }
    }

    public void bindAlliance(){
        for(Kingdom kingdom : kingdoms){
            if(!kingdom.getRelations().getAlliance().equals(this)){
                kingdom.getRelations().setAlliance(this);
            }
        }
    }

    public void removeAlliance(){
        for(Kingdom kingdom : kingdoms){
            kingdom.getRelations().setAlliance(null);
        }
        kingdoms = null;
    }

    //invites

    public boolean hasInvitedKingdom(){
        return invitedKingdom != null;
    }

    public void setInvitedKingdom(Kingdom invitedKingdom) {
        this.invitedKingdom = invitedKingdom;
    }

    public void removeInvitedKingdom(){
        this.invitedKingdom = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KingdomAlliance)) return false;
        KingdomAlliance that = (KingdomAlliance) o;
        return Objects.equals(kingdoms, that.kingdoms) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingdoms, name);
    }
}
