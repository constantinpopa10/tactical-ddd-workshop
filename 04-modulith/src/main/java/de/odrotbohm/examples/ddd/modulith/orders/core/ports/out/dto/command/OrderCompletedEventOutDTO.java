package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEventOutDTO {
    private UUID orderIdentifier;
}
