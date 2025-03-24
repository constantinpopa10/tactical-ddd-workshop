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

import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result.OrderInResultDTO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@Transactional
@ApplicationModuleTest(verifyAutomatically = false)
@RequiredArgsConstructor
class OrderManagementIntegrationTests {

	private final OrderManagement orders;

	@Test
	void completingAnOrderPublishesOrderCompletedEvent(Scenario scenario) throws Exception {
		OrderInCommandDTO order = getDummyOrderInRequestDTO();

		scenario.stimulate(() -> orders.complete(order))
				.andWaitForStateChange(() -> orders.findOrder(order.getId()).getStatus(), OrderInResultDTO.Status.COMPLETED::equals)
				.andExpect(OrderCompletedEvent.class)
				.matchingMappedValue(OrderCompletedEvent::orderIdentifier, order.getId())
				.toArrive();
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

}
