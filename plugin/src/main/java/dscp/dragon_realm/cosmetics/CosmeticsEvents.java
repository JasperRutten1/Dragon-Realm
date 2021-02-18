package dscp.dragon_realm.cosmetics;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CosmeticsEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void inventoryClick(InventoryClickEvent event){
        if(event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;
        if(!Cosmetic.isCosmeticItem(event.getCursor())) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack item = event.getCurrentItem();
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        ItemStack ticket = event.getCursor();
        ItemMeta ticketMeta = ticket.getItemMeta();
        assert ticketMeta != null;

        Material material = Material.getMaterial(ticketMeta.getPersistentDataContainer().get(Cosmetic.materialKey, PersistentDataType.STRING));
        int modelData = ticketMeta.getPersistentDataContainer().get(Cosmetic.modelDataKey, PersistentDataType.INTEGER);

        if(material != item.getType()) return;
        if(itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() == modelData) return;

        itemMeta.setCustomModelData(modelData);
        item.setItemMeta(itemMeta);
        event.setCancelled(true);
        if(ticket.getAmount() > 1){
            ticket.setAmount(ticket.getAmount() - 1);
        }
        else{
            event.getWhoClicked().setItemOnCursor(null);
        }
    }
}
