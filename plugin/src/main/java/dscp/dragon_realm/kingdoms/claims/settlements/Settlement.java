package dscp.dragon_realm.kingdoms.claims.settlements;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.commands.customCommands.KingdomClaimCommand;
import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.claims.KingdomClaim;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomMemberRank;
import dscp.dragon_realm.kingdoms.vault.VaultException;
import org.bukkit.Chunk;

import java.io.Serializable;
import java.util.*;

public class Settlement implements Serializable {
    private static final long serialVersionUID = -8887070975539253124L;

    private Kingdom kingdom;
    private String name;
    private Map<Integer, List<Integer>> coveredChunks;
    private SettlementLevel level;
    private boolean isCapital;
    private KingdomMember governor;
    private Integer centerX;
    private Integer centerZ;

    //resources
    private double food, taxingRate, population, coins;

    public static final int SETTLEMENT_MAX = 4;

    public Settlement(Kingdom kingdom, String name, int level, Chunk centerChunk) throws KingdomException {
        if(kingdom == null) throw new IllegalArgumentException("kingdom can't be null");
        if(name == null) throw new IllegalArgumentException("name can't be null");
        if(SettlementLevel.getLevelFromInt(level) == null)
            throw new IllegalArgumentException("unknown level");

        //check if kingdom has enough coins
        SettlementCosts cost = SettlementCosts.getCost(SettlementLevel.getLevelFromInt(level));
        assert cost != null;
        if(kingdom.getVault().getCurrencyType().toCoins(kingdom.getVault().getDefaultCurrency()) < cost.coins)
            throw new KingdomException("not enough coins");

        //check if there is already a settlement with this name
        for(Kingdom k : Kingdom.kingdoms){
            if(kingdom.getClaim().hasSettlementWithName(name))
                throw new KingdomException("a settlement with this name already exists");
        }

        //check if a kingdom has the maximum amount of settlements
        if(kingdom.getClaim().getSettlements().size() >= SETTLEMENT_MAX)
            throw new KingdomException("already at maximum number of settlements");
        //check if kingdom has claimed all the chunks needed for settlement
        int diameter = 1 + ( 2 * level );
        int x = centerChunk.getX();
        int z = centerChunk.getZ();
        if(!kingdom.getClaim().checkClaimedInRadius(x, z, level))
            throw new KingdomException("kingdom must have all chunks claimed (" + diameter + "x" + diameter + ")");

        this.coveredChunks = new HashMap<>();
        try{
            //add chunks to covered chunks
            for(Chunk c : KingdomClaim.getChunksInRadius(x, z, level)){
                addChunkToCovered(c.getX(), c.getZ());
            }
            //remove coins
            kingdom.getVault().changeDefaultCurrency(-(int)cost.coins);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new KingdomException(e.getMessage());
        }
        System.out.println("4");

        this.kingdom = kingdom;
        this.name = Dragon_Realm_API.capitalizeFirstLetter(name);
        this.level = SettlementLevel.getLevelFromInt(level);
        this.isCapital = false;
        this.centerX = x;
        this.centerZ = z;
    }

    //getters

    public Kingdom getKingdom() {
        return kingdom;
    }

    public String getName() {
        return name;
    }

    public SettlementLevel getLevel() {
        return level;
    }

    public Map<Integer, List<Integer>> getCoveredChunks() {
        return coveredChunks;
    }

    public boolean isCapital() {
        return isCapital;
    }

    public KingdomMember getGovernor() {
        return governor;
    }

    private void addChunkToCovered(int x, int z){
        if(!coveredChunks.containsKey(x)) coveredChunks.put(x, new ArrayList<>());
        if(!coveredChunks.get(x).contains(z)) coveredChunks.get(x).add(z);
    }

    public boolean isCovered(int x, int z){
        if(!coveredChunks.containsKey(x)) return false;
        return coveredChunks.get(x).contains(z);
    }

    public boolean isCovered(Chunk chunk){
        if(chunk == null) throw new IllegalArgumentException("chunk can't be null");
        if(!KingdomClaim.isOverworldChunk(chunk)) return false;
        return isCovered(chunk.getX(), chunk.getZ());
    }

    //center and distance

    public Chunk getCenterChunk(){
        return KingdomClaim.getChunkObject(centerX, centerZ);
    }

    public int getDistance(Settlement settlement){
        return KingdomClaim.distance(this.getCenterChunk(), settlement.getCenterChunk());
    }

    //assign governor

    public void assignGovernor(KingdomMember governor) throws KingdomException {
        if(governor == null) throw new IllegalArgumentException("governor can't be null");
        if(governor.getKingdom().getClaim().isGovernor(governor))
            throw new KingdomException("this member is already a governor of another settlement");
        if(!governor.hasPermission(KingdomMemberRank.NOBEL))
            throw new KingdomException("a governor must have a rank of nobel or higher");
        this.governor = governor;
    }

    public void removeGovernor(KingdomMember governor) throws KingdomException {
        if(governor == null) throw new IllegalArgumentException("governor can't be null");
        if(!governor.equals(this.governor))
            throw new KingdomException("this member is not the current governor of this settlement");
        this.governor = null;
    }

    //level up settlement

    public Settlement levelUp() throws KingdomException {
        if(this.getLevel() == SettlementLevel.getHighestLevel()) throw new KingdomException("this settlement is already at the maximum level");
        SettlementLevel nextLevel = this.level.getNextLevel();
        SettlementCosts settlementCosts = SettlementCosts.getCost(nextLevel);
        assert settlementCosts != null;
        if(kingdom.getVault().getCurrencyType().toCoins(kingdom.getVault().getDefaultCurrency()) < settlementCosts.getCoins()) throw new KingdomException("not enough coins");
        int dia = (nextLevel.getLevel() * 2) + 1;
        if(!kingdom.getClaim().checkClaimedInRadius(centerX, centerZ, nextLevel.level))
            throw new KingdomException("kingdom must have " + dia + " x " + dia + " area claimed around center chunk");
        for(Chunk c : KingdomClaim.getChunksInRadius(centerX, centerZ, nextLevel.getLevel())){
            addChunkToCovered(c.getX(), c.getZ());
        }

        kingdom.getVault().changeDefaultCurrency(-(int)settlementCosts.getCoins());

        this.level = nextLevel;
        return this;
    }

    //resources



    //equals and hashcode override

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Settlement)) return false;
        Settlement that = (Settlement) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(coveredChunks, that.coveredChunks) &&
                level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coveredChunks, level);
    }
}
