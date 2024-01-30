package pl.dk.ecommerceplatform.stripe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.utils.UtilsService;

@Service
@RequiredArgsConstructor
class WebHookService {

    private static final Logger logger = UtilsService.getLogger(WebHookService.class);
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public void setOrderStatusAsPaid(String payload, Event event) {
        Long orderId = this.getOrderId(payload, event.getType());
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        logger.info("Starting setting status as {}", OrderStatus.PAID);
        order.setStatus(OrderStatus.PAID);
        orderRepository.flush();
        logger.info("Orders status set as {}", OrderStatus.PAID);
    }

    private Long getOrderId(String payload, String event) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new ServerException();
        }
        JsonNode jsonPatched = jsonNode.path("data")
                .path("object")
                .path("metadata");

        logger.error("OrderId id {} from event: {}", jsonPatched.path("orderId").asText(), event);
        return Long.valueOf(jsonPatched.path("orderId").asText());
    }
}
