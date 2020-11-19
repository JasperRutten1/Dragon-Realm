package dscp.dragon_realm.container;

import dscp.dragon_realm.Dragon_Realm;
import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.builders.LoreBuilder;
import dscp.dragon_realm.interfaces.Containable;
import dscp.dragon_realm.utils.SoundEffect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Container implements Listener {

    @Getter public Player viewer;
    @Getter private boolean opened;
    @Getter private int size;
    @Getter private InventoryType type;

    protected Map<Integer, ContainerSlot> slotData;
    protected Consumer<InventoryClickEvent> globalHandler;
    protected Inventory inventory;
    protected String title;

    @Getter(AccessLevel.PACKAGE)
    protected ContainerSlot returnSlot;

    @Getter private PageContainer pagination;
    private Holder holder;

    /*
     * ---------------------------------------------------------------------------------
     * Constructor definitions
     * ---------------------------------------------------------------------------------
     */

    public Container(String title, int rows) {
        if(rows > 6) {
            throw new IllegalArgumentException("Cannot have more than 6 rows");
        }

        this.size = 9 * rows;
        this.type = InventoryType.CHEST;
        this.slotData = new HashMap<>();
        this.holder = new Holder(this);
        this.title = title;

        initialize();
        setReturnClose();
    }

    public Container(String title, InventoryType type) {
        this.size = 9;
        this.type = type;
        this.slotData = new HashMap<>();
        this.holder = new Holder(this);
        this.title = title;

        initialize();
        setReturnClose();
    }

    // Initialize essential details
    private void initialize() {
        if(type == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(holder, size, ChatColor.translateAlternateColorCodes('&', title));
        } else {
            this.inventory = Bukkit.createInventory(holder, type, ChatColor.translateAlternateColorCodes('&', title));
        }

        Dragon_Realm.getInstance().getServer().getPluginManager().registerEvents(this, Dragon_Realm.getInstance());
    }

    /*
     * ---------------------------------------------------------------------------------
     * Container methods
     * ---------------------------------------------------------------------------------
     */

    /**
     * Set the return slot to the given container slot
     *
     * @param slot Container slot
     * @return self
     */
    public Container setReturnSlot(ContainerSlot slot) {
        this.returnSlot = slot;
        return this;
    }

    /**
     * Set the return slot to a return arrow
     *
     * @param title Return target
     * @param handler Click handler
     * @return self
     */
    public Container setReturnArrow(String title, Consumer<InventoryClickEvent> handler) {
        this.returnSlot = new ContainerSlot(
                new ItemStackBuilder(Material.ARROW).name("&e" + " LEFT ARROW " + " " + title).build(),
                handler
        );
        return this;
    }

    /**
     * Set the return slot to a close button
     *
     * @return self
     */
    public Container setReturnClose() {
        this.returnSlot = new ContainerSlot(
            new ItemStackBuilder(Material.ARROW)
                .name("&cClose Menu")
                .lore(new LoreBuilder()
                    .blank()
                    .line("Click here to exit this menu")
                )
                .build(),
            e -> e.getWhoClicked().closeInventory(),
            SoundEffect.CLICK
        );
        return this;
    }

    /**
     * Set the return slot to nothing
     *
     * @return self
     */
    public Container setReturnVoid() {
        setReturnSlot(ContainerSlot.VOID);
        return this;
    }

    /**
     * Configure pagination for this Container
     *
     * @param fillerSlots The indexes of the slots that should be filled by pagination
     * @param items The items that will be spread out over the pagination
     * @return self
     */
    public <T extends Containable> Container setPagination(int[] fillerSlots, List<T> items) {
        this.pagination = new PageContainer(this, fillerSlots, items.stream().map(Containable::toContainerSlot).collect(Collectors.toList()));
        return this;
    }

    /**
     * Open this container for the given player
     *
     * @param viewer Viewer
     * @return self
     */
    public Container open(Player viewer) {
        if(opened) throw new IllegalArgumentException("Container already opened");

        if(inventory == null) {
            initialize();
        }

        this.viewer = viewer;
        this.opened = true;

        // Initialize the slots
        onOpen(viewer, new Blueprint(this));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Dragon_Realm.getInstance(), () -> {
            viewer.openInventory(inventory);

            if(pagination != null) pagination.rerender();

            onOpened(viewer);
        }, 0);

        return this;
    }

    /**
     * Create a pagination where items will be spread over
     * the <tt>fillerSlots</tt>. The page can be switched by arrows placed
     * in the bottom row.
     *
     * @param viewer Viewer
     * @param fillerSlots Slots to fill
     * @param items Items to fill in
     * @return self
     */
    @Deprecated
    protected Container pagination(Player viewer, int[] fillerSlots, List<ContainerSlot> items) {
        if(opened) throw new IllegalArgumentException("Container already opened");
        this.viewer = viewer;
        this.opened = true;

        if(type != InventoryType.CHEST) {
            throw new UnsupportedOperationException("Pagination only works with chest menus");
        }

        this.inventory = Bukkit.createInventory(holder, size, ChatColor.translateAlternateColorCodes('&', title));

        // Initialize the slots
        onOpen(viewer, new Blueprint(this));

        // Open the inventory
        viewer.openInventory(inventory);

        return this;
    }

    /**
     * Close and delete this container
     */
    public void close() {
        if(inventory == null) return;

        HandlerList.unregisterAll(this);

        onClose(viewer);

        new ArrayList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory);

        this.inventory = null;
        this.viewer = null;
        this.opened = false;
    }

    /**
     * Modify the container with a new blueprint.
     *
     * @param modifier Modifier
     */
    public void modify(Consumer<Blueprint> modifier) {
        modifier.accept(new Blueprint(this));
    }

    /*
     * ---------------------------------------------------------------------------------
     * Event handlers
     * ---------------------------------------------------------------------------------
     */

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(getFromInventory(e.getInventory()) == this) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Dragon_Realm.getInstance(), this::close, 10);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(e.getPlayer().equals(viewer)) {
            this.close();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(getFromInventory(e.getInventory()) == this && e.getWhoClicked() instanceof Player) {
            e.setCancelled(true);

            int slot = e.getRawSlot();

            if(slot >= 0 && slot < size) {
                ContainerSlot cSlot = slotData.getOrDefault(slot, null);
                if(cSlot == null) return;

                if(cSlot.clickSound != null) cSlot.clickSound.play(viewer);

                if(cSlot.handler != null) cSlot.handler.accept(e);

                if(globalHandler != null) {
                    globalHandler.accept(e);
                }
            }
        }
    }

    /*
     * ---------------------------------------------------------------------------------
     * Abstract methods
     * ---------------------------------------------------------------------------------
     */

    protected abstract void onOpen(Player viewer, Blueprint bp);

    protected abstract void onClose(Player viewer);

    protected void onOpened(Player viewer) {
    }

    /*
     * ---------------------------------------------------------------------------------
     * Static methods
     * ---------------------------------------------------------------------------------
     */

    /**
     * Returns the container currently being displayed to the specified player
     *
     * @param player Player
     * @return Container
     */
    public static Container getCurrent(Player player) {
        return getFromInventory(player.getOpenInventory().getTopInventory());
    }

    /**
     * Returns the container associated with the specified inventory
     *
     * @param inv Inventory
     * @return Container, or null when inventory is invalid
     */
    public static Container getFromInventory(Inventory inv) {
        if(!(inv.getHolder() instanceof Holder)) return null;

        return ((Holder) inv.getHolder()).getContainer();
    }

    /*
     * ---------------------------------------------------------------------------------
     * Internal classes
     * ---------------------------------------------------------------------------------
     */

    /**
     * A custom inventory holder implementation, used to identify a container
     */
    @Getter
    @AllArgsConstructor
    private static class Holder implements InventoryHolder {

        private Container container;

        @Override
        public Inventory getInventory() {
            return container.inventory;
        }
    }
}
