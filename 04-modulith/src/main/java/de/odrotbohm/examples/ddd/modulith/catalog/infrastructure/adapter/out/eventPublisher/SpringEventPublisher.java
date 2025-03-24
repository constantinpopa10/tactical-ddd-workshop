package de.odrotbohm.examples.ddd.modulith.catalog.infrastructure.adapter.out.eventPublisher;

import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out.CatalogEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements CatalogEventsPublisher<DomainEvent> {
    private final ApplicationEventPublisher springEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        springEventPublisher.publishEvent(event);
    }
}
