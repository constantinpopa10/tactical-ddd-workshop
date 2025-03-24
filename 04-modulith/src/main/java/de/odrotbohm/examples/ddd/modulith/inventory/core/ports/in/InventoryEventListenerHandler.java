package de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in;


import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.ProductAddedEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.OutOfStockEvent;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;

public interface InventoryEventListenerHandler {
    void handleProductAddedEvent(ProductAddedEvent event);

    void handleOrderCompletedEvent(OrderCompletedEvent event);

    void handleOutOfStockEvent(OutOfStockEvent event);
}
