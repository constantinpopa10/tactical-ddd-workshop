package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.out.persistence;

import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.OrderPersistence;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.LineItemOutCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.command.OrderOutCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.out.dto.response.OrderOutResultDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistence {
    private final ModelMapper modelMapper;
    private final OrderRepository repository;

    @Override
    public void save(OrderOutCommandDTO orderOutRequestDTO) {
        OrderTable orderTable = getOrderTable(orderOutRequestDTO);
        repository.save(orderTable);
    }

    @Override
    public Optional<OrderOutResultDTO> findById(UUID identifier) {
        Optional<OrderTable> optionalOrderTable = repository.findById(identifier);
        if (optionalOrderTable.isPresent()){
            OrderOutResultDTO orderOutResponseDTO = getOrderOutResponseDTO(optionalOrderTable.get());
            return Optional.of(orderOutResponseDTO);
        }
        return Optional.empty();
    }

    private OrderTable getOrderTable(OrderOutCommandDTO orderOutRequestDTO) {
        modelMapper.typeMap(OrderOutCommandDTO.class, OrderTable.class)
                .addMappings(mapper -> mapper.using(ctx -> ((OrderOutCommandDTO.Status) ctx.getSource()).name())
                        .map(OrderOutCommandDTO::getStatus, OrderTable::setStatus));
        modelMapper.typeMap(LineItemOutCommandDTO.class, LineItem.class);
        return modelMapper.map(orderOutRequestDTO, OrderTable.class);
    }

    private OrderOutResultDTO getOrderOutResponseDTO(OrderTable orderTable) {
        modelMapper.typeMap(OrderTable.class, OrderOutResultDTO.class).addMappings(mapper -> mapper.using(ctx -> OrderOutResultDTO.Status.valueOf((String) ctx.getSource())).map(OrderTable::getStatus, OrderOutResultDTO::setStatus));
        modelMapper.typeMap(LineItem.class, LineItemOutCommandDTO.class);
        OrderOutResultDTO orderOutResponseDTO = modelMapper.map(orderTable, OrderOutResultDTO.class);
        return orderOutResponseDTO;
    }
}
