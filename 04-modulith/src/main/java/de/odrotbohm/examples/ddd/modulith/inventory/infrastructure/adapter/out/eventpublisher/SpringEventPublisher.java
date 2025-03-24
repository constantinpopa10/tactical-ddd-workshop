package de.odrotbohm.examples.ddd.modulith.inventory.infrastructure.adapter.out.eventpublisher;

import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out.InventoryEventPublisher;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements InventoryEventPublisher<DomainEvent> {
    private final ApplicationEventPublisher springEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        springEventPublisher.publishEvent(event);
    }
}
