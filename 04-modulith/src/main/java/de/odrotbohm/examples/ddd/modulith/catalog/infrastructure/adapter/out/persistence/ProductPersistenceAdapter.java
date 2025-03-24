package de.odrotbohm.examples.ddd.modulith.catalog.infrastructure.adapter.out.persistence;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;
import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out.ProductPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistence {

    private final ProductRepository repository;

    @Override
    public Product findById(UUID id) {
        Optional<ProductTable> productTable = repository.findById(id);
        if (productTable.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        return new Product(productTable.get().getId(), productTable.get().getName(), productTable.get().getPrice());
    }

    @Override
    public void save(Product product) {
        ProductTable productTable = new ProductTable(product.getId(), product.getName(), product.getPrice());
        repository.save(productTable);
    }
}

