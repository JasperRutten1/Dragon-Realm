package dscp.dragon_realm.utils.AdvancedInventory;

import dscp.dragon_realm.Dragon_Realm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AdvancedInventory implements Listener {
    private static final long serialVersionUID = -4899562749456506226L;

    private HashMap<Integer, ItemStack> inventory;
    private Integer rows;
    private String name;
    private UUID id;

    public AdvancedInventory(int rows, String name){
        this.inventory = createEmpty(rows);
        this.rows = rows;
        this.name = name;
        this.id = UUID.randomUUID();

        initialise();
    }

    private void initialise(){
        Dragon_Realm.instance.getServer().getPluginManager().registerEvents(this, Dragon_Realm.instance);
    }

    public AdvancedInventory setItems(Integer index, ItemStack items){
        inventory.put(index, items);
        return this;
    }

    public Inventory buildInventory(){
        Inventory inventory = Bukkit.createInventory(null, this.rows, this.name);
        for(Map.Entry<Integer, ItemStack> entry : this.inventory.entrySet()){
            inventory.setItem(entry.getKey(), entry.getValue());
        }
        return inventory;
    }

    public void open(Player viewer){
        Inventory inventory = buildInventory();
        viewer.openInventory(inventory);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvancedInventory)) return false;
        AdvancedInventory that = (AdvancedInventory) o;
        return inventory.equals(that.inventory) && rows.equals(that.rows) && name.equals(that.name) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventory, rows, name, id);
    }
}
