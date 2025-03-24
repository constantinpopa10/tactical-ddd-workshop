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
package de.odrotbohm.examples.ddd.modulith.orders.core.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@AggregateRoot
public class OrderAggregate{

	@Identity
	private UUID Id;
	private List<LineItemEntity> lineItems;
	private Status status;

	public OrderAggregate() {

		this.Id = UUID.randomUUID();
		this.lineItems = new ArrayList<>();
		this.status = Status.SUBMITTED;
	}

	public OrderAggregate add(UUID productId, long amount) {

		lineItems.stream()
				.filter(it -> it.belongsToProduct(productId))
				.findFirst()
				.ifPresentOrElse(
						it -> it.increaseQuantityBy(amount),
						() -> lineItems.add(new LineItemEntity(productId, amount)));

		return this;
	}

	public OrderAggregate complete() {
		this.setStatus(Status.COMPLETED);
		return this;
	}

	public OrderCompletedEvent completedEvent() {
		return  new OrderCompletedEvent(Id);
	}

	public enum Status {
		SUBMITTED, COMPLETED;
	}

}
