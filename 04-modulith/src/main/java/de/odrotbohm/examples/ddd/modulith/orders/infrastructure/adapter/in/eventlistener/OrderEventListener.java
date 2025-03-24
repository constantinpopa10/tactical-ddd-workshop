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
package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.in.eventlistener;

import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderCompletedEvent;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderCompletedEventInDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.modelmapper.ModelMapper;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@PrimaryAdapter
@Slf4j
@Component
@RequiredArgsConstructor
class OrderEventListener {
	private final ModelMapper modelMapper;
	private final OrderEventHandler orderEventHandler;

	/**
	 * Sends out an email to the customer who placed the order on their completion.
	 *
	 * @param event
	 */
	@ApplicationModuleListener
	void on(OrderCompletedEvent event) {
		OrderCompletedEventInDTO dto = modelMapper.map(event, OrderCompletedEventInDTO.class);
		orderEventHandler.handle(dto);
	}
}
