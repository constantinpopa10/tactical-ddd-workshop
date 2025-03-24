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
package de.odrotbohm.examples.ddd.modulith.inventory.infrastructure.adapter.in.eventListener;


import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.ProductAddedEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.OutOfStockEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.ports.in.InventoryEventListenerHandler;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@Transactional
@Component
@RequiredArgsConstructor
class InventoryListener {

	private final InventoryEventListenerHandler inventoryEventListenerHandler;

	/**
	 * Initializes the Inventory with a stock of zero for the Product just added.
	 *
	 * @param event
	 */
	@ApplicationModuleListener
	void onProductAdded(ProductAddedEvent event) {

		inventoryEventListenerHandler.handleProductAddedEvent(event);
	}

	/**
	 * Updates the inventory based on the products referred to by the line items in the Order that was just completed.
	 *
	 * @param event
	 */
	@ApplicationModuleListener
	void onOrderCompleted(OrderCompletedEvent event) {

		log.info("Received completed order {}. Triggering stock update for line items.", event.orderIdentifier());

		inventoryEventListenerHandler.handleOrderCompletedEvent(event);
	}

	@EventListener
	void onOutOfStock(OutOfStockEvent event) {
		inventoryEventListenerHandler.handleOutOfStockEvent(event);
	}
}
