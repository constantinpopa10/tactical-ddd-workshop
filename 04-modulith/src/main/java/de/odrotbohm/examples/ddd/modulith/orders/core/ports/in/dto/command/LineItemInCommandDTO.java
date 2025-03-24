package de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemInCommandDTO {
    private UUID id;
    private UUID productId;
    private String description;
    private long amount;
}
