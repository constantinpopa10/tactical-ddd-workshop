/*
 * Copyright 2017-2021 the original author or authors.
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
package de.odrotbohm.examples.ddd.modulith.orders.tests;

import de.odrotbohm.examples.ddd.modulith.catalog.core.domain.model.Product;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.out.emailsender.EmailSenderNoOp;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.events.core.EventPublicationRegistry;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Oliver Drotbohm
 */
@Value
@ApplicationModuleTest(verifyAutomatically = false)
class EmailNotificationTest {

	OrderManagement orders;
	EventPublicationRegistry registry;
	EmailSenderNoOp emails;

	@BeforeEach
	void setUp() {
		emails.setFail(false);
	}

	@Test
	void completingAnOrderUpdatesInventory(Scenario scenario) throws Exception {
		Product product	= new Product( "Some product", BigDecimal.valueOf(29.99));

		OrderInCommandDTO order = getDummyOrderInRequestDTO();

		scenario.stimulate(() -> orders.complete(order))
				.customize(it -> it.pollDelay(1200, TimeUnit.MILLISECONDS))
				.andWaitForEventOfType(OrderCompletedEvent.class)
				.toArriveAndVerify(__ -> {
					assertThat(registry.findIncompletePublications()).isEmpty();
				});
	}

	private static OrderInCommandDTO getDummyOrderInRequestDTO() {
		List<LineItemInCommandDTO> lineItems = Arrays.asList(
				new LineItemInCommandDTO(UUID.randomUUID(), UUID.randomUUID(), "description", 2L));

		OrderInCommandDTO order = new OrderInCommandDTO();
		order.setId(UUID.randomUUID());
		order.setStatus(OrderInCommandDTO.Status.SUBMITTED);
		order.setLineItems(lineItems);
		return order;
	}


	@Test
	@DirtiesContext
	void systemCrashDuringTransactionalListenerExecutionKeepsPublicationRegistration(Scenario scenario) throws Exception {

		emails.setFail(true);
		Product product	= new Product( "Some product", BigDecimal.valueOf(29.99));

		OrderInCommandDTO order = getDummyOrderInRequestDTO();

		scenario.stimulate(() -> orders.complete(order))
				.customize(it -> it.pollDelay(1200, TimeUnit.MILLISECONDS))
				.andWaitForEventOfType(OrderCompletedEvent.class)
				.toArriveAndVerify(__ -> {
					assertThat(registry.findIncompletePublications().size()).isEqualTo(1);
				});
	}
}
