package pl.dk.ecommerceplatform.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.security.SecurityService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;

    public OrderDto createOrder(Long userId, SaveOrderDto saveOrderDto) {
        Order orderToSave = orderDtoMapper.map(userId, saveOrderDto);
        Order savedOrder = orderRepository.save(orderToSave);
        return orderDtoMapper.map(savedOrder);
    }

}
