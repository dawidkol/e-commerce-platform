package pl.dk.ecommerceplatform.order;

import pl.dk.ecommerceplatform.order.dtos.OrderDto;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.order.dtos.SaveOrderDto;
import pl.dk.ecommerceplatform.order.dtos.UpdateOrderStatusDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long userId, SaveOrderDto saveOrderDto);

    void updateOrderStatus(Long id, UpdateOrderStatusDto updateOrderStatusDto);

    OrderDto getOrder(List<String> credentials, Long orderId, Long userId);

    List<OrderDto> getOrders(List<String> credentials, Long userId, int page, int size);

    OrderValueDto calculateOrderValueWithOtherCurrency(Long orderId, String code);

    OrderDto cancelOrder(Long orderId, Long userId);

}