package de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model;

import org.jmolecules.event.types.DomainEvent;

import java.util.UUID;

public record ProductAddedEvent(UUID productId) implements DomainEvent {
}
