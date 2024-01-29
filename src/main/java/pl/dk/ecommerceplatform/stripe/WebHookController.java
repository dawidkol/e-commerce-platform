package pl.dk.ecommerceplatform.stripe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.utils.UtilsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
class WebHookController {
    private static final Logger logger = UtilsService.getLogger(WebHookController.class);

    private final OrderRepository orderRepository;
    @Value("${stripe.webhook.key}")
    String endpointSecret;
    private final ObjectMapper objectMapper;


    @PostMapping("/events")
    public ResponseEntity<?> handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        if (sigHeader == null || payload == null) {
            return ResponseEntity.badRequest().build();
        }
        Event event = null;
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (JsonSyntaxException e) {
            logger.error("Invalid payload. {} ", e.getMessage());
            // Invalid payload
            return ResponseEntity.badRequest().build();
        } catch (SignatureVerificationException e) {
            // Invalid signature
            logger.error("Invalid signature. {} ", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            throw new ServerException();
        }

        switch (event.getType()) {
            case "payment_intent.created", "payment_intent.requires_action": {
                logger.info(event.getType());
                break;
            }
            case "payment_intent.succeeded": {
                logger.info(event.getType());
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("Payment for {} succeed", paymentIntent.getAmount());
                break;
            }
            case "checkout.session.completed": {
                logger.info(event.getType());
                try {
                    Long orderId = this.getOrderId(payload, event.getType());
                    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
                    logger.info("Starting setting status as {}", OrderStatus.PAID);
                    order.setStatus(OrderStatus.PAID);
                    orderRepository.flush();
                    logger.info("Orders status set as {}", OrderStatus.PAID);
                } catch (NumberFormatException ex) {
                    logger.error("OrderId not received");
                }
                break;
            }
            default:
                logger.info("Unhandled event type: " + event.getType());
        }
        return ResponseEntity.ok().build();
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
