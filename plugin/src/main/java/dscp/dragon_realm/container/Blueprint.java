package dscp.dragon_realm.container;

import dscp.dragon_realm.interfaces.Containable;
import dscp.dragon_realm.utils.SoundEffect;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Modifies items inside of container
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Blueprint {

    final private Container container;
    private int selectedSlot;

    /**
     * Select the given slot
     *
     * @param slot Slot number
     * @return self
     */
    public Blueprint slot(int slot) {
        selectedSlot = slot;

        ContainerSlot existing = container.slotData.get(slot);
        container.slotData.putIfAbsent(slot, (existing == null) ? new ContainerSlot() : existing);

        return this;
    }

    /**
     * Clear the selected slot
     *
     * @return self
     */
    public Blueprint clear() {
        container.inventory.setItem(selectedSlot, new ItemStack(Material.AIR));
        container.slotData.remove(selectedSlot);
        return this;
    }

    /**
     * Set a slot
     *
     * @param slotItem ItemStack
     * @return ContainerBuild
     */
    public Blueprint item(ItemStack slotItem) {
        container.inventory.setItem(selectedSlot, slotItem);
        container.slotData.get(selectedSlot).item = slotItem;
        return this;
    }

    /**
     * Set a slot
     *
     * @param slot Container slot
     * @return self
     */
    public Blueprint set(ContainerSlot slot) {
        container.inventory.setItem(selectedSlot, slot.item);

        ContainerSlot cSlot = container.slotData.get(selectedSlot);
        cSlot.item = slot.item;
        cSlot.handler = slot.handler;
        cSlot.clickSound = slot.clickSound;
        return this;
    }

    /**
     * Set a slot
     *
     * @param slot Containable instance
     * @return self
     */
    public Blueprint set(Containable slot) {
        return set(slot.toContainerSlot());
    }

    /**
     * Place the return item in the selected slot
     *
     * @return self
     */
    public Blueprint setReturn() {
        if(container.returnSlot != null) set(container.returnSlot);
        return this;
    }

    /**
     * Set a slot
     *
     * @param onClick OnClickHandler
     * @return ContainerBuild
     */
    public Blueprint handler(Consumer<InventoryClickEvent> onClick) {
        container.slotData.get(selectedSlot).handler = onClick;
        return this;
    }

    /**
     * Set the click sound of the specified slot
     *
     * @param sound Sound
     * @return self
     */
    public Blueprint sound(SoundEffect sound) {
        container.slotData.get(selectedSlot).clickSound = sound;
        return this;
    }

    /**
     * When true, plays UI_BUTTON_CLICK
     *
     * @return self
     */
    public Blueprint clickSound() {
        return sound(SoundEffect.CLICK);
    }

    /**
     * Set the global click handler that is used for all items. This method
     * does not use or alter the 'current slot'.
     *
     * @param onClick OnClickHandler
     * @return self
     */
    public Blueprint globalHandler(Consumer<InventoryClickEvent> onClick) {
        container.globalHandler = onClick;
        return this;
    }

}
