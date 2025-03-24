package de.odrotbohm.examples.ddd.modulith.catalog.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductTable, UUID> {}
