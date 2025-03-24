package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out;

import org.jmolecules.event.types.DomainEvent;

public interface EventPublisher<T extends DomainEvent> {
    void publish(T event);
}
