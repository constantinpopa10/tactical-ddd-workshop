package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.in.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID Id;
    private List<LineItemDTO> lineItems;
    private Status status;

    public enum Status {
        SUBMITTED, COMPLETED;
    }
}
