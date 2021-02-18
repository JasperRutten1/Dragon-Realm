package dscp.dragon_realm.kingdoms.wars;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.relations.KingdomAlliance;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarProposal implements Serializable {
    private static final long serialVersionUID = -2122314280477819154L;

    private Kingdom attacker;
    private Kingdom defender;
    private HashMap<Kingdom, Boolean> votes;
    private War.WarReason reason;

    public WarProposal(Kingdom attacker, Kingdom defender, War.WarReason reason){
        if(attacker == null) throw new IllegalArgumentException("attacker can't be null");
        if(defender == null) throw new IllegalArgumentException("defender can't be null");

        this.defender = defender;
        this.attacker = attacker;
        this.votes = new HashMap<>();
        this.reason = reason;
        votes.put(attacker, true);
    }

    public Kingdom getAttacker() {
        return attacker;
    }

    public Kingdom getDefender() {
        return defender;
    }

    public HashMap<Kingdom, Boolean> getVotes() {
        return votes;
    }

    public War.WarReason getReason() {
        return reason;
    }

    public boolean getVoteResult(){
        List<Kingdom> allianceKingdoms = attacker.getRelations().getAlliance().getKingdoms();
        int allianceSize = allianceKingdoms.size(), yesVotes = 0;
        for(Kingdom k : allianceKingdoms){
            if(votes.containsKey(k)){
                if(votes.get(k)){
                    yesVotes++;
                }
            }
        }
        return yesVotes >= Math.ceil((double) allianceSize / 2);
    }

    public void vote(Kingdom kingdom, boolean vote){
        if(!attacker.getRelations().getAlliance().getKingdoms().contains(kingdom))
            throw new IllegalArgumentException("only allied kingdoms can vote on war proposal");
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        votes.put(kingdom, vote);
    }

    public boolean hasVoted(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        return votes.containsKey(kingdom);
    }

    public void startWar(){
        try{
            WarManager.startWarBetween(attacker, defender, reason);
        }
        catch (WarException ex){
            attacker.getRelations().getAlliance().sendKingdomsMembersMessage(ChatColor.RED + ex.getMessage());
        }
        attacker.getRelations().getAlliance().setWarProposal(null);
    }
}
