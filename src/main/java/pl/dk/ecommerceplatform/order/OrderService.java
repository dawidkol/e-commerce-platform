package pl.dk.ecommerceplatform.order;

import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long userId, SaveOrderDto saveOrderDto);

    void updateOrderStatus(UpdateOrderStatusDto updateOrderStatusDto);

    OrderDto getOrder(List<String> credentials, Long orderId, Long userId);

    List<OrderDto> getOrders(List<String> credentials, Long userId, int page, int size);

    OrderValueDto calculateOrderValueWithAnotherValue(Long orderId, String code);
}
