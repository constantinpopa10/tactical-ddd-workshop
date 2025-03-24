package de.odrotbohm.examples.ddd.modulith.orders.infrastructure.adapter.in.rest;



import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.LineItemInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.command.OrderInCommandDTO;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.OrderManagement;
import de.odrotbohm.examples.ddd.modulith.orders.core.ports.in.dto.result.OrderInResultDTO;
import lombok.RequiredArgsConstructor;
import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PrimaryAdapter
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final ModelMapper modelMapper;
    private final OrderManagement orderManagement;

    @PostMapping
    public ResponseEntity<OrderInResultDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderInCommandDTO orderInDTO = getOrderInDTO(orderDTO);
        
        OrderInResultDTO createdOrder = orderManagement.createOrder(orderInDTO);

        return ResponseEntity.ok(createdOrder);
    }

    private OrderInCommandDTO getOrderInDTO(OrderDTO orderDTO) {
        modelMapper.typeMap(OrderDTO.class, OrderInCommandDTO.class)
                .addMappings(mapper -> mapper.using(ctx -> OrderInCommandDTO.Status.valueOf(((OrderDTO.Status) ctx.getSource()).name()))
                        .map(OrderDTO::getStatus, OrderInCommandDTO::setStatus));
        modelMapper.typeMap(LineItemDTO.class, LineItemInCommandDTO.class);

        OrderInCommandDTO orderInDTO = modelMapper.map(orderDTO, OrderInCommandDTO.class);
        return orderInDTO;
    }
}