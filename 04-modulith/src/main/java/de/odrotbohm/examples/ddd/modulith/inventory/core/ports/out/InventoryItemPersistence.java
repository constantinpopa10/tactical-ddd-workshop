package de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out;

import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InventoryItemAggregate;

import java.util.Optional;
import java.util.UUID;

public interface InventoryItemPersistence {

    Optional<InventoryItemAggregate> findByProductId(UUID productIdentifier);

    InventoryItemAggregate save(InventoryItemAggregate aggregate);

}
