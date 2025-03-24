package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.out.eventpublisher;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpringEventPublisher implements EventPublisher<DomainEvent> {
    private final ApplicationEventPublisher springEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        springEventPublisher.publishEvent(event);
    }
}
