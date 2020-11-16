package dscp.dragon_realm.container;

import dscp.dragon_realm.builders.ItemStackBuilder;
import dscp.dragon_realm.interfaces.Containable;
import dscp.dragon_realm.utils.GroupedList;
import dscp.dragon_realm.utils.Reflection;
import dscp.dragon_realm.utils.SoundEffect;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API class for making containers that support multiple pages
 */
public class PageContainer implements Listener {

    final private Container container;
    final private int[] fillerSlots;
    private GroupedList<ContainerSlot> items;
    @Getter private int currentPage = 0;

    public PageContainer(Container container, int[] fillerSlots, List<ContainerSlot> items) {
        this.container = container;
        this.fillerSlots = fillerSlots;
        this.items = new GroupedList<>(items, fillerSlots.length);
    }

    /**
     * Render the container with the items on the current page
     */
    protected void rerender() {
        int nextArrowSlot = container.getSize() - 1;
        int prevArrowSlot = nextArrowSlot - 8;

        container.modify(bp -> {
            // Previous arrow
            if(currentPage == 0) {
                bp.slot(prevArrowSlot).setReturn();
            } else {
                bp.slot(prevArrowSlot)
                    .item(getPreviousArrow())
                    .handler(e -> previousPage())
                    .clickSound();
            }

            // Next arrow
            if(currentPage >= items.getGroupCount() - 1) {
                bp.slot(nextArrowSlot).set(new ContainerSlot());
            } else {
                bp.slot(nextArrowSlot)
                    .item(getNextArrow())
                    .handler(e -> nextPage())
                    .clickSound();
            }

            // Content
            List<ContainerSlot> pageItems = items.getGroup(currentPage);

            if(pageItems == null) return;
            for(int i = 0; i < fillerSlots.length; i++) {
                if(i >= pageItems.size()) {
                    bp.slot(fillerSlots[i]).set(new ContainerSlot());
                } else {
                    bp.slot(fillerSlots[i]).set(pageItems.get(i));
                }
            }
        });

        // Update title
        int count = items.getGroupCount();

        if(count == 0) count = 1;

        String title = container.title + " (Page " + (currentPage + 1) + "/" + count + ")";
        Reflection.updateWindowTitle(container.getViewer(), title);
    }

    /**
     * Open the previous page
     */
    public void previousPage() {
        if(currentPage == 0) return;
        SoundEffect.CLICK.play(container.getViewer());
        currentPage--;
        rerender();
    }

    /**
     * Open the next page
     */
    public void nextPage() {
        if(currentPage >= items.getGroupCount() - 1) return;
        SoundEffect.CLICK.play(container.getViewer());
        currentPage++;
        rerender();
    }

    /**
     * Set the current page the user is viewing
     * @param slot
     */
    public void setCurrentPage(int slot) {
        if(slot < 0 || slot >= items.getGroupCount())
            throw new IndexOutOfBoundsException("Unknown page index");

        currentPage = slot;
        rerender();
    }

    /**
     * Set new contents for this page container
     *
     * @param items Items
     */
    public <T extends Containable> void setContents(List<T> items) {
        this.items = new GroupedList<>(items.stream().map(Containable::toContainerSlot).collect(Collectors.toList()), fillerSlots.length);

        if(currentPage >= this.items.getGroupCount()) {
            this.currentPage = this.items.getGroupCount() - 1;
        }

        rerender();
    }

    /**
     * Returns the item used for the Previous Page button
     *
     * @return ItemStack
     */
    public static ItemStack getPreviousArrow() {
        return new ItemStackBuilder(Material.ARROW).name("&e" + "LEFT ARROW" + " Previous").build();
    }

    /**
     * Returns the item used for the Next Page button
     *
     * @return ItemStack
     */
    public static ItemStack getNextArrow() {
        return new ItemStackBuilder(Material.ARROW).name("&eNext " + "RIGHT ARROW").build();
    }

}
