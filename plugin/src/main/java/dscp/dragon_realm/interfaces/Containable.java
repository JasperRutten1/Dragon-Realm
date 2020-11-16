package dscp.dragon_realm.interfaces;

import dscp.dragon_realm.container.ContainerSlot;

/**
 * Applied to classes that can be represented in a Container.
 */
public interface Containable {

    /**
     * Represent this instance as a container slot item
     *
     * @return ContainerSlot
     */
    ContainerSlot toContainerSlot();

}
