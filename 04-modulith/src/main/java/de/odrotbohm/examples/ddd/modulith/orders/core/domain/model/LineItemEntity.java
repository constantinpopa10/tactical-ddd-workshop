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
import lombok.NoArgsConstructor;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LineItemEntity {

	@Identity
	private final UUID id;
	private UUID productId;
	private @Nullable String description;
	private long amount;

	public LineItemEntity(UUID productId, long amount) {
		this.id = UUID.randomUUID();
		this.productId = productId;
		this.amount = amount;
	}

	public boolean belongsToProduct(UUID identifier) {
		return this.productId.equals(identifier);
	}

	public LineItemEntity increaseQuantityBy(long amount) {

		this.amount = this.amount + amount;

		return this;
	}
}
