package dscp.dragon_realm.kingdoms.relations;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KingdomAlliance implements Serializable {
    private static final long serialVersionUID = 6986846034456483371L;

    private List<Kingdom> kingdoms;
    private String name;

    public static int MAXIMUM_MEMBERS = 3;
    public static int MAXIMUM_INVITES = 3;

    public KingdomAlliance(){
        this.kingdoms = new ArrayList<>();
    }

    public List<Kingdom> getKingdoms() {
        return kingdoms;
    }

    public boolean canBeInvited(Kingdom kingdom){
        for(Kingdom k : kingdoms){
            if(k.getRelations().getRelationToKingdom(kingdom) != Relation.FRIENDLY) return false;
        }
        return true;
    }

    public KingdomAlliance addKingdomToAlliance(Kingdom kingdom) throws KingdomException {
        if(kingdoms.size() >= MAXIMUM_MEMBERS)
            throw new KingdomException("only " + MAXIMUM_MEMBERS + " kingdoms are allowed in a alliance");
        if(canBeInvited(kingdom)){
            kingdoms.add(kingdom);
        }
        else throw new KingdomException("This kingdom can not be invited to join. " +
                "\nall kingdoms of the alliance must be friendly with this kingdom");
        return this;
    }

    public void sendAllianceMessage(String message){
        for(Kingdom kingdom : kingdoms){
            kingdom.sendMembersMessage(message);
        }
    }

    public void sendAllianceBroadcast(String message){
        for(Kingdom kingdom : kingdoms){
            kingdom.sendMembersMessage("&l[&3Alliance&f]&r " + message);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Members: \n");
        for(Kingdom kingdom : kingdoms){
            sb.append(" - ").append(kingdom.getName()).append("\n");
        }

        return sb.toString();
    }
}
