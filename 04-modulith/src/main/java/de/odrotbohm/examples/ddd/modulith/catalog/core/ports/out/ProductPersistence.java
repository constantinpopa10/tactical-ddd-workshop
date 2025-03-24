package de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;

import java.util.UUID;

public interface ProductPersistence {
    Product findById(UUID id);


    void save(Product product);
}
