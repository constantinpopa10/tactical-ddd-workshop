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
package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.out.persistence;

import de.odrotbohm.examples.ddd.modulith.catalog.infrastructure.adapter.out.persistence.ProductTable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * @author Oliver Drotbohm
 */
@Data
@Entity
@AllArgsConstructor
public class LineItem {

	@Id
	private UUID id;
	private UUID productId;
	private @Nullable String description;
	private long amount;

	public LineItem() {

	}
}
