package dscp.dragon_realm.kingdoms.gui;

import dscp.dragon_realm.kingdoms.Kingdom;
import dscp.dragon_realm.kingdoms.KingdomException;
import dscp.dragon_realm.kingdoms.members.KingdomMember;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class KingdomGUIEvents implements Listener {



    @EventHandler()
    public void inventoryClickEvent(InventoryClickEvent event) throws KingdomException {
        if(event.isShiftClick()){
            event.setCancelled(true);
            return;
        }
        if(event.getInventory().getHolder() != null) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;

        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory gui = event.getInventory();

        // kingdom members overview GUI
        if(KingdomGUI.kingdomMembersGUIMap.containsValue(event.getClickedInventory())){
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getType().equals(Material.AIR)) return;

            // player head
            if(clickedItem.getType().equals(Material.PLAYER_HEAD)){
                SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                if(meta == null) return;

                OfflinePlayer clickedPlayer = meta.getOwningPlayer();
                Kingdom kingdom = Kingdom.getKingdom((Player) clickedPlayer);
                if(kingdom == null) return;
                KingdomMember member = kingdom.getMember(clickedPlayer);
                if(member == null) return;

                player.openInventory(KingdomGUI.memberGUI(player, member));
            }

            // barrier
            else if(clickedItem.getType().equals(Material.BARRIER)){
                player.sendMessage("not implemented");
            }
        }

        // member stats GUI
        if(KingdomGUI.memberGUIMap.containsValue(event.getClickedInventory())){
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getType().equals(Material.AIR)) return;

            // barrier
            if(clickedItem.getType().equals(Material.BARRIER)){
                player.openInventory(KingdomGUI.kingdomMembersGUI(player));
            }

            // green concrete (promote)
            else if(clickedItem.getType().equals(Material.GREEN_CONCRETE)){
                ItemStack skull = gui.getItem(4);
                assert skull != null;
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                assert meta != null;
                if(!canPromoteMember(player, meta.getOwningPlayer())) return;
            }
        }

        event.setCancelled(true);
    }

    private boolean canPromoteMember(OfflinePlayer promoting, OfflinePlayer toPromote){
        Kingdom kingdom = Kingdom.getKingdom(promoting);
        if(kingdom == null) return false;
        if(!kingdom.getMembers().containsKey(toPromote.getUniqueId())) return false;

        KingdomMember m1 = kingdom.getMember(promoting);
        KingdomMember m2 = kingdom.getMember(toPromote);

        return (m1.hasRankOrHigher(m2.getRank().getRankHigher()));
    }

}
