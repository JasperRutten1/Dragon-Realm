package dscp.dragon_realm.utils.AdvancedInventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AdvancedInventory {
    private HashMap<Integer, ItemStack> inventory;
    private Integer rows;

    public AdvancedInventory(int rows){
        this.inventory = createEmpty(rows);
        this.rows = rows;
    }

    public InventoryItemSlot getItemIn()

    public void setItems(int row, int col, ItemStack items){

    }

    public void open(Player viewer){

    }

    public static class InventoryItemSlot implements Serializable {
        private static final long serialVersionUID = 5759208062906162147L;

        private ItemStack items;

        public InventoryItemSlot(ItemStack items){
            if(items == null){
                this.items = new ItemStack(Material.AIR, 1);
            }
            else{
                this.items = items;
            }
        }
    }

    private static HashMap<Integer, ItemStack> createEmpty(int rows){
        HashMap<Integer, ItemStack> inventory = new HashMap<>();
        for(int i = 0 ; i < rows ; i++){
            for(int j = 0 ; j < 9 ; j++){
                int k = i * j;
                inventory.put(k, new ItemStack(Material.AIR));
            }
        }
        return inventory;
    }

}
