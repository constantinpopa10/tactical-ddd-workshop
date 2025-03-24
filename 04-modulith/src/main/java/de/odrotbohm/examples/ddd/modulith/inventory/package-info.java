@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {"catalog :: core.domain.model",
                "orders :: core.domain.model",
                "orders :: core.ports.in",
                "orders :: core.ports.in.dto.command",
                "orders :: core.ports.in.dto.result"}
)
@org.springframework.lang.NonNullApi
package de.odrotbohm.examples.ddd.modulith.inventory;

