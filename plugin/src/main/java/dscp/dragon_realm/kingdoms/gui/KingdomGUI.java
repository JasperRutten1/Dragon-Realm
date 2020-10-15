package dscp.dragon_realm.kingdoms.gui;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import dscp.dragon_realm.kingdoms.members.KingdomRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class KingdomGUI {

    public static final Map<UUID, Inventory> kingdomMembersGUIMap = new HashMap<>();
    public static final Map<UUID, Inventory> memberGUIMap = new HashMap<>();

    // GUI's
    public static Inventory kingdomMembersGUI(Player player) throws KingdomException {
        Kingdom kingdom = Kingdom.getKingdom(player);
        if(kingdom == null) throw new KingdomException("you are not part of a kingdom");
        int size = Math.min(54, 18 + ((int) Math.ceil(kingdom.getMembers().size() / 9.0) * 9));
        Inventory gui = Bukkit.createInventory(null, size, kingdom.getName() + " Members");
        List<KingdomMember> membersNotKing = new ArrayList<>();
        //put members in arraylist
        for(Map.Entry<UUID, KingdomMember> entry : kingdom.getMembers().entrySet()){
            if(!kingdom.getKing().getPlayer().getUniqueId().equals(entry.getKey())){
                membersNotKing.add(entry.getValue());
            }
        }
        // put items in inventory
        gui.setItem(4, getMemberSkull(kingdom.getKing()));
        gui.setItem(size - 1, createItem(Material.BARRIER, ChatColor.RED + "Return",  ChatColor.GRAY+ "return to kingdom overview"));
        for(int i = 9, n = 0 ; i < size - 9 && n < membersNotKing.size(); i++, n++){
            gui.setItem(i, getMemberSkull(membersNotKing.get(n)));
        }

        kingdomMembersGUIMap.put(player.getUniqueId(), gui);
        return gui;
    }

    public static Inventory memberGUI(Player player, KingdomMember memberToView) throws KingdomException {
        Inventory gui = Bukkit.createInventory(null, 27, memberToView.getPlayer().getName());

        Kingdom kingdom = Kingdom.getKingdom(player);
        if(kingdom == null) throw new KingdomException("you are not in a kingdom");
        if(!kingdom.isMemberOfKingdom(memberToView.getPlayer())) throw new KingdomException("you are not in the same kingdom as the member you want to view");
        KingdomMember memberViewing = kingdom.getMember(player);

        gui.setItem(4, getMemberSkull(memberToView));
        gui.setItem(13, createItem(Material.EMERALD, memberToView.getRank().getName(),
                ChatColor.GRAY + "the rank of this member \n" + ChatColor.GRAY + "in the kingdom"));
        gui.setItem(26, createItem(Material.BARRIER, ChatColor.RED + "Return", ChatColor.GRAY + "return to member overview"));

        if(memberViewing.getRank() == KingdomRank.KING){
            if(memberToView.getRank() == KingdomRank.PEASANT){
                gui.setItem(11, createItem(Material.GRAY_CONCRETE, ChatColor.GRAY + "Demote",
                        ChatColor.GRAY + "member already has lowest rank"));
            }
            else{
                gui.setItem(11, createItem(Material.RED_CONCRETE, ChatColor.RED + "Demote",
                        ChatColor.GRAY + "demote this member to " + memberToView.getRank().getRankLower().getName()));
            }

            if(memberToView.getRank() == KingdomRank.NOBEL){
                gui.setItem(15, createItem(Material.GOLD_BLOCK, ChatColor.GOLD + "Crown",
                        ChatColor.GRAY + "crown this member as the" + ChatColor.GRAY + "new king of this kingdom"));
            }
            else{
                gui.setItem(15, createItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Promote",
                        ChatColor.GRAY + "promote this member to " + memberToView.getRank().getRankHigher().getName()));
            }
        }
        else if(memberViewing.getRank() == KingdomRank.NOBEL || memberViewing.getRank() == KingdomRank.DUKE){
            if(memberToView.getRank() == KingdomRank.PEASANT){
                gui.setItem(11, createItem(Material.GRAY_CONCRETE, ChatColor.GRAY + "Demote",
                        ChatColor.GRAY + "member already has lowest rank"));
            }
            else{
                gui.setItem(11, createItem(Material.RED_CONCRETE, ChatColor.RED + "Demote",
                        ChatColor.GRAY + "demote this member to " + memberToView.getRank().getRankLower().getName()));
            }
            if(memberToView.getRank().hasRankOrHigher(memberViewing.getRank().getRankLower())){
                gui.setItem(15, createItem(Material.GRAY_CONCRETE, ChatColor.GRAY + "Promote",
                        ChatColor.GRAY + "you can not promote this member"));
            }
            else{
                gui.setItem(15, createItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Promote",
                        ChatColor.GRAY + "promote this member to " + memberToView.getRank().getRankHigher().getName()));
            }
        }

        memberGUIMap.put(player.getUniqueId(), gui);
        return gui;
    }

    // method's
    private static ItemStack createItem(Material material, String name, String lore) throws KingdomException {
        if(material == null) throw new KingdomException("material can't be null");
        if(name == null) throw new KingdomException("name can't be null");
        if(lore == null) throw new KingdomException("lore can't be null");

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.split("\n")));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack getMemberSkull(KingdomMember member){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        assert meta != null;

        meta.setOwningPlayer(member.getPlayer());
        meta.setDisplayName(member.getPlayer().getName());
        List<String> lore = new ArrayList<>();
        lore.add(member.getRank().getName());
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}
