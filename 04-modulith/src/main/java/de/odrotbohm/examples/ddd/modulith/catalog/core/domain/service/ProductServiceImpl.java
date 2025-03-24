package de.odrotbohm.examples.ddd.modulith.catalog.core.domain.service;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;
import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.in.ProductService;
import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out.CatalogEventsPublisher;
import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.out.ProductPersistence;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductPersistence productPersistence;
    private final CatalogEventsPublisher<DomainEvent> eventsPublisher;


    @Override
    public Product load(UUID id) {
        return productPersistence.findById(id);
    }

    @Override
    public void save(Product product) {
        productPersistence.save(product);
        eventsPublisher.publish(product.getEvent());
    }
}
