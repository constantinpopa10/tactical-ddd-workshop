/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.odrotbohm.examples.ddd.modulith.inventory.tests;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.InsufficientStockException;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model.OutOfStockEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service.InventoryEventListenerHandlerImpl;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service.InventoryServiceImpl;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Oliver Drotbohm
 */

@ApplicationModuleTest(verifyAutomatically = false)
@RequiredArgsConstructor
class InventoryIntegrationTests {

	private final InventoryServiceImpl inventory;
	private final InventoryEventListenerHandlerImpl listener;

	@MockitoBean OrderManagement orders;

	@Test
	void throwsInsufficientStockOnOutOfStock() {

		Product product	= new Product( "Some product", BigDecimal.valueOf(29.99));
		inventory.registerShipment(product.getId(), 0);

		assertThatExceptionOfType(InsufficientStockException.class)
				.isThrownBy(() -> listener.handleOutOfStockEvent(new OutOfStockEvent(product.getId())));
	}
}
