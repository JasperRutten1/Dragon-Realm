package dscp.dragon_realm.container;

import dscp.dragon_realm.interfaces.Containable;
import dscp.dragon_realm.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Represents a single slot in a container
 */
public final class ContainerSlot implements Containable {

    /** A completely empty representation of a ContainerSlot */
    public static final ContainerSlot VOID = new ContainerSlot(new ItemStack(Material.AIR));

    protected ItemStack item;
    protected Consumer<InventoryClickEvent> handler;
    protected SoundEffect clickSound;

    protected ContainerSlot() {
        this.item = new ItemStack(Material.AIR);
        this.handler = null;
        this.clickSound = null;
    }

    public ContainerSlot(ItemStack item) {
        this.item = item;
        this.handler = null;
        this.clickSound = null;
    }

    public ContainerSlot(ItemStack item, Consumer<InventoryClickEvent> handler) {
        this.item = item;
        this.handler = handler;
        this.clickSound = null;
    }

    public ContainerSlot(ItemStack item, Consumer<InventoryClickEvent> handler, SoundEffect clickSound) {
        this.item = item;
        this.handler = handler;
        this.clickSound = clickSound;
    }

    public ContainerSlot(ItemStack item, Consumer<InventoryClickEvent> handler, Sound clickSound) {
        this.item = item;
        this.handler = handler;
        this.clickSound = new SoundEffect(clickSound);
    }

    public ContainerSlot(ItemStack item, SoundEffect clickSound) {
        this.item = item;
        this.handler = null;
        this.clickSound = clickSound;
    }

    public ContainerSlot(ItemStack item, Sound clickSound) {
        this.item = item;
        this.handler = null;
        this.clickSound = new SoundEffect(clickSound);
    }

    public ContainerSlot(ContainerSlot slot) {
        this.item = slot.getItem().clone();
        this.handler = slot.getHandler();
        this.clickSound = slot.getClickSound();
    }

    public ItemStack getItem() {
        return item;
    }

    public Consumer<InventoryClickEvent> getHandler() {
        return handler;
    }

    public SoundEffect getClickSound() {
        return clickSound;
    }

    @Override
    public ContainerSlot toContainerSlot() {
        return this;
    }
}
