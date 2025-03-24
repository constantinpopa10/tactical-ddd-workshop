package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.in.rest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemDTO {
    private UUID id;
    private UUID productId;
    private String description;
    private long amount;
}
