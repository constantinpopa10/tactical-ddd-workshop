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
package de.odrotbohm.examples.ddd.modulith.catalog.tests;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;
import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.ProductAddedEvent;
import de.odrotbohm.examples.ddd.modulith.catalog.core.ports.in.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oliver Drotbohm
 */
@ApplicationModuleTest(verifyAutomatically = false)
@RequiredArgsConstructor
class CatalogIntegrationTests {

	private final ProductService productService;
	private final ApplicationContext context;

	@Test
	void bootstrapsCatalogOnly() {

		assertThat(productService).isNotNull();

//		assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
//				.isThrownBy(() -> context.getBean(InventoryServiceImpl.class));
	}

	@Test
	void publishesEventOnProductCreation(Scenario scenario) {

		var product = new Product("Some product", BigDecimal.valueOf(29.99));

		scenario.stimulate(() -> productService.save(product))
				.andWaitForEventOfType(ProductAddedEvent.class)
				.matchingMappedValue(ProductAddedEvent::productId, product.getId())
				.toArrive();
	}
}
