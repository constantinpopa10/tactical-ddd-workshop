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
package de.odrotbohm.examples.ddd.modulith.orders.core.domain.service;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderCompletedEventInDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderEventHandler;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.EmailSender;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.OrderCompletedEventOutDTO;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
class OrderEventHandlerImpl implements OrderEventHandler {

	private @Setter boolean fail = false;
	private final EmailSender emailSender;
	private final ModelMapper modelMapper;

	@Override
	public void handle(OrderCompletedEventInDTO event) {
		OrderCompletedEventOutDTO dto = modelMapper.map(event, OrderCompletedEventOutDTO.class);
		emailSender.sendEmail(dto);
	}

//	@Override
//	public void handle(OrderCompletedEvent event) {
//		emailSender.sendEmail(event);
//	}
}
