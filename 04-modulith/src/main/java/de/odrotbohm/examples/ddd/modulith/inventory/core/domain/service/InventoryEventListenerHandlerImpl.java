package de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service;


import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.ProductAddedEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InsufficientStockException;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.OutOfStockEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.InventoryEventListenerHandler;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.InventoryService;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.dto.command.InventoryLineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.dto.command.InventoryOrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result.OrderInResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InventoryEventListenerHandlerImpl implements InventoryEventListenerHandler {

    private final InventoryService inventory;
    private final OrderManagement orders;
    private final ModelMapper modelMapper;

    /**
     * Initializes the Inventory with a stock of zero for the Product just added.
     *
     * @param event
     */
    @Override
    public void handleProductAddedEvent(ProductAddedEvent event) {

        var productId = event.productId();

        if (!inventory.hasItemFor(productId)) {

            inventory.registerShipment(productId, 0);

            log.info("Created inventory item for product {} and zero stock!", productId);
        }
    }

    /**
     * Updates the inventory based on the products referred to by the line items in the Order that was just completed.
     *
     * @param event
     */
    @Override
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {

        log.info("Received completed order {}. Triggering stock update for line items.", event.orderIdentifier());
        OrderInResultDTO orderInResultDTO = orders.findOrder(event.orderIdentifier());

        InventoryOrderInCommandDTO inventoryInCommandDTO = getInventoryOrderInCommandDTO(orderInResultDTO);
        inventory.updateStockFor(inventoryInCommandDTO);
    }

    private InventoryOrderInCommandDTO getInventoryOrderInCommandDTO(OrderInResultDTO orderInResultDTO) {
        modelMapper.typeMap(OrderInResultDTO.class, InventoryOrderInCommandDTO.class)
                .addMappings(mapper -> mapper.using(ctx -> InventoryOrderInCommandDTO.Status.valueOf(((OrderInResultDTO.Status) ctx.getSource()).name()))
                        .map(OrderInResultDTO::getStatus, InventoryOrderInCommandDTO::setStatus));
        modelMapper.typeMap(LineItemInCommandDTO.class, InventoryLineItemInCommandDTO.class);
        InventoryOrderInCommandDTO inventoryOrderInCommandDTO = modelMapper.map(orderInResultDTO, InventoryOrderInCommandDTO.class);
        return inventoryOrderInCommandDTO;
    }

    @Override
    public void handleOutOfStockEvent(OutOfStockEvent event) throws InsufficientStockException {

        var productId = event.productId();
        var stock = inventory.getStockFor(productId);

        log.info("Product {} out of stock! Current overdraw: {}.", productId, stock);

        throw new InsufficientStockException(productId, stock);
    }
}
