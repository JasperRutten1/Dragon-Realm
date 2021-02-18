package dscp.dragon_realm.kingdoms.wars;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.relations.KingdomAlliance;
import org.bukkit.ChatColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class WarSide implements Serializable {
    private static final long serialVersionUID = 4173064713008653043L;

    private Kingdom kingdom;
    private int warScore;
    private double exhaustion;

    public WarSide(Kingdom kingdom){
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        this.kingdom = kingdom;
        this.warScore = 0;
        this.exhaustion = 0.0;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public double getExhaustion() {
        return exhaustion;
    }

    public int getWarScore() {
        return warScore;
    }

    public ArrayList<Kingdom> getKingdomsOnSide(){
        ArrayList<Kingdom> kingdoms = new ArrayList<>();
        if(kingdom.getRelations().hasAlliance()){
            kingdoms.addAll(kingdom.getRelations().getAlliance().getKingdoms());
        }
        else{
            kingdoms.add(kingdom);
        }
        return kingdoms;
    }

    public boolean isOnSide(Kingdom kingdom){
        if(kingdom.getRelations().hasAlliance()){
            for(Kingdom k : this.kingdom.getRelations().getAlliance().getKingdoms()){
                if(k.equals(kingdom)) return true;
            }
            return false;
        }
        else return this.kingdom.equals(kingdom);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.DARK_AQUA).append(this.kingdom.getName()).append("\n");
        if(getKingdomsOnSide().size() > 1){
            sb.append(ChatColor.GOLD).append("Allies: ").append("\n");
            for(Kingdom kingdom : getKingdomsOnSide()){
                if(!kingdom.equals(this.kingdom)){
                    sb.append(ChatColor.RESET).append(" - ")
                            .append(ChatColor.DARK_AQUA)
                            .append(kingdom.getName())
                            .append("\n");
                }
            }
        }
        else{
            sb.append(ChatColor.GOLD).append("No Allies").append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarSide)) return false;
        WarSide warSide = (WarSide) o;
        return warScore == warSide.warScore && Double.compare(warSide.exhaustion, exhaustion) == 0 && Objects.equals(kingdom, warSide.kingdom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingdom, warScore, exhaustion);
    }
}
