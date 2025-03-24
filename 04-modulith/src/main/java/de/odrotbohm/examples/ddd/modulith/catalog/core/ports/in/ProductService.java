package de.odrotbohm.examples.ddd.modulith.catalog.core.ports.in;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;

import java.util.UUID;

public interface ProductService {
    Product load(UUID id);

    void save(Product product);
}
