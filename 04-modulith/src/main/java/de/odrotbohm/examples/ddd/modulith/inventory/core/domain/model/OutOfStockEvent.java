package de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model;


import org.jmolecules.event.types.DomainEvent;

import java.util.UUID;

public record OutOfStockEvent(UUID productId) implements DomainEvent {}
