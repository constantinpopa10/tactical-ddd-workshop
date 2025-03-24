package de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out;

import org.jmolecules.event.types.DomainEvent;

public interface CatalogEventsPublisher<T extends DomainEvent> {
    void publish(T event);
}
