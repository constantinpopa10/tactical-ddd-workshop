package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.OrderOutCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.response.OrderOutResultDTO;

import java.util.Optional;
import java.util.UUID;

public interface OrderPersistence {
    void save(OrderOutCommandDTO order);

    Optional<OrderOutResultDTO> findById(UUID identifier);
}

