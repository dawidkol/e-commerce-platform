package pl.dk.ecommerceplatform.stripe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.stripe.RefundException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.stripe.dtos.StripePaymentDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

@Service
@RequiredArgsConstructor
class WebHookService {

    private static final Logger logger = UtilsService.getLogger(WebHookService.class);
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final StripePaymentRepository stripePaymentRepository;

    @Transactional
    public StripePaymentDto saveStripePayment(String payload, Event event) {
        String type = event.getType();
        DataSessionWrapper dataFromEventSessionCompleted = this.getDataFromEventSessionCompleted(payload, type);
        Order order = orderRepository.findById(dataFromEventSessionCompleted.orderId).orElseThrow(OrderNotFoundException::new);
        order.setStatus(OrderStatus.PAID);

        StripePayment stripePayment = StripePayment.builder()
                .paymentIntent(dataFromEventSessionCompleted.paymentIntent)
                .order(order)
                .refund(false)
                .refunded(false)
                .build();

        StripePayment savedStripePayment = stripePaymentRepository.save(stripePayment);
        return StripePaymentMapper.map(savedStripePayment);
    }

    private DataSessionWrapper getDataFromEventSessionCompleted(String payload, String eventType) {
        JsonNode jsonNode = null;
        logger.info(payload);
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new ServerException();
        }
        JsonNode jsonPatched = jsonNode.path("data")
                .path("object");
        long orderId = jsonPatched.path("metadata").path("orderId").asLong();
        String paymentIntent = jsonPatched.path("payment_intent").asText();
        DataSessionWrapper dataSessionWrapper = new DataSessionWrapper(paymentIntent, orderId);
        logger.info("Data from event {}: {}", eventType, dataSessionWrapper);
        return dataSessionWrapper;
    }

    private record DataSessionWrapper(String paymentIntent, Long orderId) {
    }

    @Transactional
    public void refund(String paymentIntent) {
        StripePayment stripePayment = stripePaymentRepository.findByPaymentIntent(paymentIntent)
                .orElseThrow(() -> new RefundException("Stripe payment intent with paymentIntent = %s not found"));
        stripePayment.setRefunded(true);
        StripePaymentDto stripePaymentDto = StripePaymentMapper.map(stripePayment);
        logger.info("Order with id {} set as refunded", stripePaymentDto.id());
    }

}
