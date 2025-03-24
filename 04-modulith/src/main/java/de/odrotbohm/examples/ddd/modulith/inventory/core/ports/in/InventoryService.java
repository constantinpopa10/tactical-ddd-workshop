package de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in;


import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InventoryItemAggregate;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.dto.command.InventoryOrderInCommandDTO;

import java.util.UUID;

public interface InventoryService {

    /**
     * Returns the amount of items currently available for the given ProductIdentifier.
     *
     * @param productIdentifier must not be {@literal null}.
     * @return
     */
    long getStockFor(UUID productIdentifier) ;

    /**
     * Returns whether we have an {@link InventoryItemAggregate} for the given {@link UUID}.
     *
     * @param productIdentifier must not be {@literal null}.
     * @return
     */
    boolean hasItemFor(UUID productIdentifier) ;

    /**
     * Registers a shipment of the given given amount of the given ProductIdentifier.
     *
     * @param productId must not be {@literal null}.
     * @param amount
     * @return
     */

    InventoryItemAggregate registerShipment(UUID productId, long amount);

    /**
     * Updates the stock for all line items contained in the given {@link InventoryOrderInCommandDTO}.
     *
     * @param inventoryOrderInRequestDTO must not be {@literal null}.
     */
    public void updateStockFor(InventoryOrderInCommandDTO inventoryOrderInRequestDTO) ;
}
