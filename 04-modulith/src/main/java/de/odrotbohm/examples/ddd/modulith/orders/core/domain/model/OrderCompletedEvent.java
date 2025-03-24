package de.odrotbohm.examples.ddd.modulith.orders.core.domain.model;

import org.jmolecules.event.types.DomainEvent;

import java.util.UUID;

public record OrderCompletedEvent(UUID orderIdentifier) implements DomainEvent {}

