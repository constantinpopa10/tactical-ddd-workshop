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
package de.odrotbohm.examples.ddd.modulith.inventory.core.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.Optional;
import java.util.UUID;


/**
 * @author Oliver Drotbohm
 */
@Data
@AllArgsConstructor
@AggregateRoot
public class InventoryItemAggregate {


	@Identity
	private InventoryItemIdentifier id;
	private UUID productId;
	private long amount;

	public InventoryItemAggregate(UUID productId, long amount) {

		this.id = new InventoryItemIdentifier(UUID.randomUUID());
		this.productId = productId;
		this.amount = amount;
	}

	public InventoryItemAggregate refillBy(long amount) {

		this.amount = this.amount + amount;

		return this;
	}

	public Optional<OutOfStockEvent> reduceStockBy(long amount) {

		if (this.amount < amount) {
			return Optional.of(new OutOfStockEvent(productId));
		}

		this.amount = this.amount - amount;

		return Optional.empty();
	}

	public record InventoryItemIdentifier(UUID itemId) {}

}
