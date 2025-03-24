package de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInResultDTO {
    private UUID Id;
    private List<LineItemInCommandDTO> lineItems;
    private Status status;

    public enum Status {
        SUBMITTED, COMPLETED;
    }
}
