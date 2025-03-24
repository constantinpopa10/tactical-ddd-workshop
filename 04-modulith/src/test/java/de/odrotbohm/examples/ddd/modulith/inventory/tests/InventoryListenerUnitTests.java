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
import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.ProductAddedEvent;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service.InventoryEventListenerHandlerImpl;
import de.odrotbohm.examples.ddd.modulith.inventory.core.domain.service.InventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryListenerUnitTests {

	@InjectMocks
	InventoryEventListenerHandlerImpl listener;
	@Mock
    InventoryServiceImpl inventory;

	@Test
	void registersShipmentForNewProduct() {

		// Given
		Product product	= new Product( "Some product", BigDecimal.valueOf(29.99));

		var event = new ProductAddedEvent(product.getId());

		when(inventory.hasItemFor(product.getId())).thenReturn(false);

		// When
		listener.handleProductAddedEvent(event);

		// Then
		verify(inventory).registerShipment(product.getId(), 0);
	}
}
