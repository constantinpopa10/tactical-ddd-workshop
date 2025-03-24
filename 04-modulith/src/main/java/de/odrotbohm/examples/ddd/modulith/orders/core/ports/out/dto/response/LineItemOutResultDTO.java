package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemOutResultDTO {
    private UUID id;
    private UUID productId;
    private String description;
    private long amount;
}
