package de.odrotbohm.examples.ddd.modulith.orders.core.ports.in;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderCompletedEventInDTO;

public interface OrderEventHandler
{
    void handle(OrderCompletedEventInDTO event);
}
