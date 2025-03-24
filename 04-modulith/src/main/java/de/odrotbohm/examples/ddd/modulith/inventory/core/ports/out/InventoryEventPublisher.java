package de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out;

import org.jmolecules.event.types.DomainEvent;

public interface InventoryEventPublisher<T extends DomainEvent> {
    void publish(T event);
}
