package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.OrderCompletedEventOutDTO;

public interface EmailSender
{
    void sendEmail(OrderCompletedEventOutDTO event);
}
