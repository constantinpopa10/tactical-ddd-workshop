/*

 * Copyright 2017-2022 the original author or authors.
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

import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.LineItemEntity;
import de.odrotbohm.examples.ddd.modulith.orders.core.domain.model.OrderAggregate;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result.LineItemInResultDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result.OrderInResultDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.EventPublisher;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.OrderPersistence;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.LineItemOutCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.OrderOutCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.response.LineItemOutResultDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.response.OrderOutResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.event.types.DomainEvent;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class OrderManagementImpl implements OrderManagement {

	private final OrderPersistence ordersPersistence;
	private final OrderProperties orderProperties;
	private final EventPublisher<DomainEvent> eventPublisher;
	private final ModelMapper modelMapper;


	/*
	 * (non-Javadoc)
	 * @see de.odrotbohm.examples.ddd.moduliths.orders.OrderManagement#createOrder()
	 */
	@Override
	public OrderInResultDTO createOrderDefault() {
		log.info(orderProperties.getOrderSystem());
		OrderAggregate orderAggregate = new OrderAggregate();
		this.createOrder(orderAggregate);
		OrderInResultDTO orderInResultDTO = getOrderInResultDTO(orderAggregate);
		return orderInResultDTO;
	}

	/*
	 * (non-Javadoc)
	 * @see de.odrotbohm.examples.ddd.moduliths.orders.OrderManagement#findOrder(de.odrotbohm.examples.ddd.moduliths.orders.Order.OrderIdentifier)
	 */
	@Override
	public OrderInResultDTO findOrder(UUID identifier) {
		OrderOutResultDTO orderOutResultDTO = ordersPersistence.findById(identifier).orElse(null);
		OrderAggregate orderAggregate = getOrderAggregate(orderOutResultDTO);
		log.info("Found aggregate order: " + orderAggregate.getId());
		OrderInResultDTO orderInResultDTO = getOrderInResultDTO(orderAggregate);
		return orderInResultDTO;
	}

	/*
	 * (non-Javadoc)
	 * @see de.odrotbohm.examples.ddd.moduliths.orders.OrderManagement#complete(de.odrotbohm.examples.ddd.moduliths.orders.Order)
	 */
	@Override
	public OrderInResultDTO complete(OrderInCommandDTO orderInDTO) {
		//convert from request DTO to internal model aggregate
		OrderAggregate orderAggregate = getOrderAggregate(orderInDTO);
		//internal processing of the aggregate
		orderAggregate.complete();
		log.info("Order completed:" + orderAggregate.getStatus());
		createOrder(orderAggregate);
		eventPublisher.publish(orderAggregate.completedEvent());
		log.info("Published OrderCompletedEvent for order {}.", orderInDTO.getId());
		//convert the internal aggregate to response DTO
		OrderInResultDTO orderInResultDTO = getOrderInResultDTO(orderAggregate);
		return orderInResultDTO;
	}

	private void createOrder(OrderAggregate orderAggregate){
		OrderOutCommandDTO orderOutRequestDTO = getOrderOutCommandDTO(orderAggregate);
		log.info("Dummy log entry to justify usage of the order properties: " + orderProperties.getOrderSystem());
		ordersPersistence.save(orderOutRequestDTO);
	}

	@Override
	public OrderInResultDTO createOrder(OrderInCommandDTO orderInDTO) {
		//convert from IN request DTO to internal model aggregate
		OrderAggregate orderAggregate = getOrderAggregate(orderInDTO);

		//internal processing of the aggregate
		log.info("Found aggregate order: " + orderAggregate.getId());

		//convert the internal aggregate to OUT request DTO
		OrderOutCommandDTO orderOutCommandDTO = getOrderOutCommandDTO(orderAggregate);
		log.info("Dummy log entry to justify usage of the order properties: " + orderProperties.getOrderSystem());
		ordersPersistence.save(orderOutCommandDTO);

		//convert the OUT response DTO to internal model aggregate
		//nothing to do here

		//convert the internal aggregate to IN response DTO
		OrderInResultDTO orderInResultDTO = getOrderInResultDTO(orderAggregate);
		return orderInResultDTO;
	}


	private OrderOutCommandDTO getOrderOutCommandDTO(OrderAggregate orderAggregate) {
		modelMapper.typeMap(LineItemInCommandDTO.class, LineItemOutCommandDTO.class);
		return this.mapWithStatus(orderAggregate,
				OrderOutCommandDTO.class,
				OrderAggregate.Status.class,
				OrderOutCommandDTO.Status.class);
	}

	private OrderInResultDTO getOrderInResultDTO(OrderAggregate orderAggregate) {
		modelMapper.typeMap(LineItemEntity.class, LineItemInResultDTO.class);
		return this.mapWithStatus(orderAggregate,
				OrderInResultDTO.class,
				OrderAggregate.Status.class,
				OrderInResultDTO.Status.class);
	}

	private OrderAggregate getOrderAggregate(OrderInCommandDTO orderInDTO) {
		modelMapper.typeMap(LineItemInCommandDTO.class, LineItemEntity.class);
		return this.mapWithStatus(orderInDTO,
				OrderAggregate.class,
				OrderInCommandDTO.Status.class,
				OrderAggregate.Status.class);
	}

	private OrderAggregate getOrderAggregate(OrderOutResultDTO orderOutResponseDTO) {
		modelMapper.typeMap(LineItemOutResultDTO.class, LineItemEntity.class);
		return this.mapWithStatus(orderOutResponseDTO,
				OrderAggregate.class,
				OrderOutResultDTO.Status.class,
				OrderAggregate.Status.class);
	}
	<S, T> T mapWithStatus(S source, Class<T> targetClass,
						   Class<?> sourceStatusType, Class<?> targetStatusType) {
		modelMapper.typeMap(source.getClass(), targetClass)
				.addMappings(mapper -> mapper.using(ctx -> {
					try {
						return Enum.valueOf((Class<Enum>) targetStatusType, ((Enum<?>) ctx.getSource()).name());
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					}
				}).map(src -> {
					try {
						return src.getClass().getMethod("getStatus").invoke(src);
					} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}, (dest, v) -> {
					try {
						dest.getClass().getMethod("setStatus", targetStatusType).invoke(dest, v);
					} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}));
		return modelMapper.map(source, targetClass);
	}
}
