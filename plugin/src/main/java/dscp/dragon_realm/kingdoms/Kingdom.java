package dscp.dragon_realm.kingdoms;

import dscp.dragon_realm.Dragon_Realm_API;
import dscp.dragon_realm.kingdoms.claims.Capital;
import dscp.dragon_realm.kingdoms.claims.ChunkCoordinates;
import dscp.dragon_realm.kingdoms.claims.ClaimedLand;
import dscp.dragon_realm.kingdoms.members.KingdomInvite;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomRank;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Kingdom implements Serializable {
    private static final long serialVersionUID = 4633922922867195577L;

    private String name;
    private Map<UUID, KingdomMember> members;
    private static Kingdoms kingdoms;
    private Map<UUID, KingdomInvite> inviteMap = new HashMap<>();

    private ClaimedLand claimedLand;
    private Capital capital;

    private double power;
    private double maxPower;
    private double coins;
    private double draconic_essence;

    private static final int startingPower = 2000;

    // constructor

    public Kingdom(String name, KingdomMember creator) throws KingdomException {
        if(name == null) throw new KingdomException("name can not be null");
        if(creator == null) throw new KingdomException("creator can't be null");

        String cleanName = "";
        cleanName = cleanName + name.charAt(0);
        cleanName = cleanName.toUpperCase();
        name = name.toLowerCase();
        cleanName = cleanName + name.substring(1);

        if(kingdomExists(cleanName)) throw new KingdomException("a kingdom with this name already exists");

        this.name = cleanName;
        this.members = new HashMap<>();
        this.members.put(creator.getPlayer().getUniqueId(), creator);
        this.maxPower = Kingdom.startingPower;
        this.power = this.maxPower;
        this.claimedLand = new ClaimedLand();
    }

    public static void initiateKingdomsIfNull(){
        if(kingdoms == null) kingdoms = new Kingdoms();
    }

    // getters

    public String getName() {
        return name;
    }
    public double getPower() {
        return power;
    }
    public Map<UUID, KingdomMember> getMembers() {
        return members;
    }
    public static Kingdoms getKingdoms() {
        return kingdoms;
    }
    public Capital getCapital() {
        return capital;
    }
    public ClaimedLand getClaimedLand() {
        return claimedLand;
    }

    // kingdom

    public KingdomMember getKing(){
        final KingdomMember[] king = {null};
        this.members.forEach( (u, m) -> {
            if(m.getRank() == KingdomRank.KING) king[0] = m;
        });
        return king[0];
    }
    public static KingdomMember getKing(Kingdom kingdom){
        return kingdom.getKing();
    }

    public static void createKingdom(Player player, String name) throws KingdomException {
        if(name == null) throw new KingdomException("name can not be null");
        initiateKingdomsIfNull();

        //check if the player is already part of another kingdom and if name already exists
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.isMemberOfKingdom(player)) throw new KingdomException("you are already part of another kingdom");
            if(kingdom.getName().equals(name)) throw new KingdomException("a kingdom with this name already exists");
        }

        KingdomMember king = new KingdomMember(player, KingdomRank.KING);

        player.sendMessage(ChatColor.GREEN + "Started new kingdom: " + name);
        kingdoms.add(new Kingdom(name, king));
    }

    public static Kingdom getKingdom(OfflinePlayer player){
        initiateKingdomsIfNull();
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.members.containsKey(player.getUniqueId())) return kingdom;
        }
        return null;
    }
    public static Kingdom getKingdom(String name){
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(name.toLowerCase().equals(kingdom.getName().toLowerCase())) return kingdom;
        }
        return null;
    }

    public static boolean kingdomExists(String name){
        return getKingdom(name) != null;
    }

    public void broadcastToOnline(String message){
        for(Map.Entry<UUID, KingdomMember> entry : members.entrySet()){
            if(entry.getValue().getPlayer().isOnline()){
                entry.getValue().getPlayer().sendMessage(message);
            }
        }
    }


    // members

    public boolean isMemberOfKingdom(Player player){
        return members.containsKey(player.getUniqueId());
    }
    public static boolean isMemberOfKingdom(Player player, Kingdom kingdom){
        return kingdom.isMemberOfKingdom(player);
    }

    public KingdomMember getMember(OfflinePlayer player){
        for(Map.Entry<UUID, KingdomMember> entry : members.entrySet()){
            if(player.getUniqueId().equals(entry.getKey())) return entry.getValue();
        }
        return null;
    }

    public void addMember(Player player) throws KingdomException {
        KingdomMember member = new KingdomMember(player);
        //check if member is already part of a kingdom
        if(isMemberOfKingdom(player)) throw new KingdomException("player is already part of a kingdom");

        members.put(player.getUniqueId(), member);
    }

    public static void invitePlayerToKingdom(Player invitor, Player invited) throws KingdomException {
        if(invited == null) throw new KingdomException("invited player can't be null");
        if(invitor == null) throw new KingdomException("the player that is inviting can't be null");
        if(!playerIsPartOfKingdom(invitor)) throw new KingdomException("you are not part of a kingdom");
        if(playerIsPartOfKingdom(invited)) throw new KingdomException("player is already part of a kingdom");

        Kingdom kingdom = Kingdom.getKingdom(invitor);
        assert kingdom != null;

        if(!kingdom.getMember(invitor).hasRankOrHigher(KingdomRank.KNIGHT)) throw new KingdomException("you do not have permission to do this");
        if(kingdom.inviteMap.get(invited.getUniqueId()) != null){
            KingdomInvite invite = kingdom.inviteMap.get(invited.getUniqueId());
            if(invite.getKingdom() ==  kingdom && invite.isValid(System.currentTimeMillis())){
                throw new KingdomException("this player is already invited to your kingdom, wait for the invite to expire before sending a new one");
            }
        }

        kingdom.inviteMap.put(invited.getUniqueId(), new KingdomInvite(kingdom, System.currentTimeMillis() + 60000));
        invited.sendMessage(ChatColor.GREEN + invitor.getName() + ChatColor.RESET + " has invited you to join " + ChatColor.GOLD + kingdom.getName());

        TextComponent message = new TextComponent("click here to accept invitation");
        message.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kingdom accept " + kingdom.getName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Accept the invite to join " + kingdom.getName()).color(net.md_5.bungee.api.ChatColor.GRAY).create()));
        invited.spigot().sendMessage(message);

        invited.sendMessage(ChatColor.GRAY + "invite will expire in 60 seconds");

        invitor.sendMessage(ChatColor.GREEN + "invited player to kingdom");
    }

    public static void acceptInvite(Player invited, String kingdomName) throws KingdomException {
        if(invited == null) throw new KingdomException("invited player can't be null");
        if(kingdomName == null) throw new KingdomException("the name of the kingdom can't be null");
        if(playerIsPartOfKingdom(invited)) throw new KingdomException("you are already part of a kingdom");

        Kingdom kingdom = getKingdom(kingdomName);
        if(kingdom == null) throw new KingdomException("could not find kingdom, the kingdom might have been removed");

        KingdomInvite invite = kingdom.inviteMap.get(invited.getUniqueId());
        if(invite == null) throw new KingdomException("you where not invited to this kingdom");
        if(!invite.isValid(System.currentTimeMillis())) throw new KingdomException("invite is no longer valid");

        kingdom.addMember(invited);
        invited.sendMessage(ChatColor.GREEN + "you joined the kingdom");
        kingdom.broadcastToOnline(ChatColor.GREEN + invited.getName() + " joined the kingdom");
    }

    public static boolean playerIsPartOfKingdom(Player player){
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.getMembers().containsKey(player.getUniqueId())) return true;
        }
        return false;
    }

    // claims

    public void claimChunk(Chunk chunk) throws KingdomException {
        if(chunk == null) throw new KingdomException("chunk can't be null");
        if(this.capital == null) throw new KingdomException("this kingdom does not yet have a capital, to create a capital use: /kingdom capital create");
        if(power < 150) throw new KingdomException("your kingdom needs at least 150 power to claim a chunk");
        if(maxPower < 50) throw new KingdomException("your maximum power is to low");
        if(claimedLand.containsChunk(chunk)) throw new KingdomException("this chunk is already claimed by your kingdom");

        // check if chunk is already claimed and if in range of capital
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.claimedLand.containsChunk(chunk)) throw new KingdomException("this chunk is already claimed by another kingdom");
        }
        if(Dragon_Realm_API.distanceBetweenChunks(this.capital.getCenterChunk(), new ChunkCoordinates(chunk.getX(), chunk.getZ())) > 25)
            throw new KingdomException("to far away from capital, max distance is 25 chunks");

        System.out.println(Dragon_Realm_API.distanceBetweenChunks(this.capital.getCenterChunk(), new ChunkCoordinates(chunk.getX(), chunk.getZ())));

        this.claimedLand.getChunkList().add(new ChunkCoordinates(chunk.getX(), chunk.getZ()));
    }
    public static void claimChunk(Player player) throws KingdomException {
        if(player == null) throw new KingdomException("player can't be null");
        Kingdom kingdom = Kingdom.getKingdom(player);
        if(kingdom == null) throw new KingdomException("you are not in a kingdom");
        KingdomMember member = kingdom.getMember(player);
        if(!member.getRank().hasRankOrHigher(KingdomRank.KNIGHT)) throw new KingdomException("you do not have permission to do this");

        kingdom.claimChunk(player.getLocation().getChunk());
        player.sendMessage(ChatColor.GREEN + "claimed chunk for your kingdom");
    }

    //capital

    public void createCapital(Chunk centerChunk) throws KingdomException {
        if(centerChunk == null) throw new KingdomException("center chunk can't be null");
        if(this.capital != null) throw new KingdomException("this kingdom already has a capital");
        if(!centerChunk.getWorld().getEnvironment().equals(World.Environment.NORMAL)) throw new KingdomException("capital must be in overworld");

        // check if far enough away from other capitals
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.getCapital() != null &&
                    Dragon_Realm_API.distanceBetweenChunks(kingdom.getCapital().getCenterChunk(), new ChunkCoordinates(centerChunk.getX(), centerChunk.getZ())) <= 30)
                throw new KingdomException("capital must be at least 30 chunks away from other capitals");
        }

        this.capital = new Capital(centerChunk);
        this.claimedLand.getChunkList().addAll(this.capital.getChunkList());
    }
    public static void createCapital(Player player) throws KingdomException {
        if(player == null) throw new KingdomException("player can't be null");
        Kingdom kingdom = Kingdom.getKingdom(player);
        if(kingdom == null) throw new KingdomException("you are not part of a kingdom");
        KingdomMember member = kingdom.getMember(player);
        if(!member.hasRankOrHigher(KingdomRank.NOBEL)) throw new KingdomException("you are not high enough rank to do this");

        kingdom.createCapital(player.getLocation().getChunk());
        player.sendMessage(ChatColor.GREEN + "created capital. (capital is 5 x 5 chunks)");
    }

    // map

    public static void showMap(Player player){
        Chunk chunk = player.getLocation().getChunk();
        for(int z = chunk.getZ() - 4 ; z <= chunk.getZ() + 4 ; z++){
            StringBuilder line = new StringBuilder();
            for(int x = chunk.getX() - 4 ; x <= chunk.getX() + 4 ; x++){
                line.append(getColorForMap(new ChunkCoordinates(x , z)));
            }
            player.sendMessage(line.toString());
        }
    }

    private static String getColorForMap(ChunkCoordinates cc){
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            if(kingdom.capital.getChunkList().contains(cc)) return ChatColor.DARK_PURPLE + "X";
            // settlements
            if(kingdom.claimedLand.getChunkList().contains(cc)) return ChatColor.GREEN + "X";
        }
        return ChatColor.GRAY + "X";
    }

    // power

    public void updateMaxPower(){

    }

    // equals and toHash

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kingdom kingdom = (Kingdom) o;
        return name.toLowerCase().equals(kingdom.name.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, members);
    }

    // test command

    public static void test(Player player){
        initiateKingdomsIfNull();
        if(kingdoms.getKingdomsList().isEmpty()) player.sendMessage(ChatColor.GREEN + "no kingdoms");
        for(Kingdom kingdom : kingdoms.getKingdomsList()){
            player.sendMessage(kingdom.name);
            player.sendMessage("king: " + kingdom.getKing().getPlayer().getName());
            player.sendMessage("capital: " + (kingdom.getCapital() == null ? "no capital" :
                    "(" + kingdom.getCapital().getCenterChunk().getX() + " : " + kingdom.getCapital().getCenterChunk().getZ() + ")"));
            player.sendMessage("claimed chunks: " + kingdom.getClaimedLand().getChunkList().size());
        }
    }

    // load/save kingdoms

    public static void loadKingdoms(File file) throws KingdomException {
        Object o = Dragon_Realm_API.load(file);
        if(o == null){
            kingdoms = null;
        }
        else {
            if(!(o instanceof Kingdoms)) throw new KingdomException("object is not of type Kingdoms");
            kingdoms = (Kingdoms) o;
        }

    }
    public static void saveKingdoms(File file) {
        Dragon_Realm_API.save(kingdoms, file);
    }
}
