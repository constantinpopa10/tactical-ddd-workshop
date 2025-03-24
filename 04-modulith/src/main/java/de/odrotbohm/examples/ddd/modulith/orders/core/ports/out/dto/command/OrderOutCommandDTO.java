package de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOutCommandDTO {
    private UUID Id;
    private List<LineItemOutCommandDTO> lineItems;
    private Status status;

    public enum Status {
        SUBMITTED, COMPLETED;
    }
}
