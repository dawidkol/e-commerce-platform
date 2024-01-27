package pl.dk.ecommerceplatform.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderAlreadyExistsException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderStatusNotFoundException;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;

    public OrderDto createOrder(Long userId, SaveOrderDto saveOrderDto) {
        orderRepository.findByCart_id(saveOrderDto.cartId())
                .ifPresent(o -> {
                    throw new OrderAlreadyExistsException();
                });
        Order orderToSave = orderDtoMapper.map(userId, saveOrderDto);
        Order savedOrder = orderRepository.save(orderToSave);
        return orderDtoMapper.map(savedOrder);
    }

    @Transactional
    public OrderDto updateOrderStatus(UpdateOrderStatusDto updateOrderStatusDto) {
        OrderStatus newStatus;
        try {
            newStatus = Enum.valueOf(OrderStatus.class, updateOrderStatusDto.status());
        } catch (IllegalArgumentException e) {
            throw new OrderStatusNotFoundException();
        }
        Long orderId = updateOrderStatusDto.orderId();
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setStatus(newStatus);
        return orderDtoMapper.map(order);
    }

}
