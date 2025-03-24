package de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOrderInCommandDTO {
    private UUID Id;
    private List<InventoryLineItemInCommandDTO> lineItems;
    private Status status;

    public enum Status {
        SUBMITTED, COMPLETED;
    }
}
