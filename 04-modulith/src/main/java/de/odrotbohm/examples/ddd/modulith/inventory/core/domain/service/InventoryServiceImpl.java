/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service;


import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InventoryItemAggregate;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.OutOfStockEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.InventoryService;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.dto.command.InventoryOrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out.InventoryEventPublisher;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.out.InventoryItemPersistence;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final InventoryItemPersistence inventoryItemPersistenceAdapter;
	private final InventoryEventPublisher<DomainEvent> eventPublisher;

	/**
	 * Returns the amount of items currently available for the given ProductIdentifier.
	 *
	 * @param productIdentifier must not be {@literal null}.
	 * @return
	 */
	@Override
	public long getStockFor(UUID productIdentifier) {

		Assert.notNull(productIdentifier, "Product identifier must not be null!");

		return inventoryItemPersistenceAdapter.findByProductId(productIdentifier) //
				.map(InventoryItemAggregate::getAmount) //
				.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Returns whether we have an {@link InventoryItemAggregate} for the given {@link UUID}.
	 *
	 * @param productIdentifier must not be {@literal null}.
	 * @return
	 */
	@Override
	public boolean hasItemFor(UUID productIdentifier) {

		Assert.notNull(productIdentifier, "Product identifier must not be null!");
		return inventoryItemPersistenceAdapter.findByProductId(productIdentifier).isPresent();
	}

	/**
	 * Registers a shipment of the given given amount of the given ProductIdentifier.
	 *
	 * @param productId must not be {@literal null}.
	 * @param amount
	 * @return
	 */
	@Override
	public InventoryItemAggregate registerShipment(UUID productId, long amount) {

		Assert.notNull(productId, "Product must not be null!");

		log.info("Registering shipment of {} {}.", amount, productId);

		InventoryItemAggregate item = inventoryItemPersistenceAdapter.findByProductId(productId)
				.map(it -> it.refillBy(amount))
				.orElseGet(() -> new InventoryItemAggregate(productId, amount));

		return inventoryItemPersistenceAdapter.save(item);
	}

	/**
	 * Updates the stock for all line items contained in the given {@link OrderAggregate}.
	 *
	 * @param inventoryOrderInRequestDTO must not be {@literal null}.
	 */
	@Override
	public void updateStockFor(InventoryOrderInCommandDTO inventoryOrderInRequestDTO) {

		inventoryOrderInRequestDTO.getLineItems().stream().forEach(it -> {

			var productId = it.getProductId();
			InventoryItemAggregate item = inventoryItemPersistenceAdapter.findByProductId(productId).orElseThrow(
					() -> new IllegalStateException(String.format("No InventoryItem found for product %s!", productId)));

			log.info("About to reduce stock for {} by {}.", productId, item.getAmount());

			Optional<OutOfStockEvent> event = item.reduceStockBy(it.getAmount());
            event.ifPresent(eventPublisher::publish);

			inventoryItemPersistenceAdapter.save(item);
		});
	}
}
