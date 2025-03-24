package de.odrotbohm.examples.ddd.modulith.inventory.infrastructure.adapter.out.persistence;

import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InventoryItemAggregate;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out.InventoryItemPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryItemPersistenceAdapter implements InventoryItemPersistence {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public Optional<InventoryItemAggregate> findByProductId(UUID productIdentifier) {
        return inventoryItemRepository.findByProductId(productIdentifier)
                .map(this::mapToDomain);
    }

    @Override
    public InventoryItemAggregate save(InventoryItemAggregate aggregate) {
        InventoryItemTable inventoryItem = mapToPersistence(aggregate);
        inventoryItemRepository.save(inventoryItem);
        return aggregate;
    }

    private InventoryItemAggregate mapToDomain(InventoryItemTable inventoryItem) {
        return new InventoryItemAggregate(
                new InventoryItemAggregate.InventoryItemIdentifier(inventoryItem.getId().itemId()),
                inventoryItem.getProductId(),
                inventoryItem.getAmount()
        );
    }

    private InventoryItemTable mapToPersistence(InventoryItemAggregate aggregate) {
        return new InventoryItemTable(
                new InventoryItemTable.InventoryItemIdentifier(aggregate.getId().itemId()),
                aggregate.getProductId(),
                aggregate.getAmount()
        );
    }
}