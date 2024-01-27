package pl.dk.ecommerceplatform.order;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.constant.UserRoleConstant;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderAlreadyExistsException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderStatusNotFoundException;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;

import java.util.List;

@Service
@AllArgsConstructor
class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderDtoMapper;

    @Transactional
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
            String orderUpperCase = updateOrderStatusDto.status().toUpperCase();
            newStatus = Enum.valueOf(OrderStatus.class, orderUpperCase);
        } catch (IllegalArgumentException e) {
            throw new OrderStatusNotFoundException();
        }
        Long orderId = updateOrderStatusDto.orderId();
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setStatus(newStatus);
        return orderDtoMapper.map(order);
    }

    public List<OrderDto> getOrders(List<String> credentials, Long userId, int page, int size) {
        List<OrderDto> orders;
        boolean isAdmin = credentials.stream()
                .anyMatch(c -> c.equalsIgnoreCase(UserRoleConstant.ADMIN_ROLE));
        if (isAdmin) {
            orders = this.getAllOrders(page, size);
        } else {
            orders = this.getUserOrders(userId, page, size);
        }
        return orders;
    }

    private List<OrderDto> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(orderDtoMapper::map)
                .toList();
    }

    private List<OrderDto> getUserOrders(Long userId, int page, int size) {
        return orderRepository.findByUser_id(userId)
                .stream()
                .map(orderDtoMapper::map)
                .toList();
    }

}
