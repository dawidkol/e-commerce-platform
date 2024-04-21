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
    private final WebHookService webHookService;
    @Value("${stripe.webhook.key}")
    String endpointSecret;

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
            case "charge.succeeded": {
                logger.info(event.getType());
                Charge charge = (Charge) stripeObject;
                logger.info("Charge succeed.Charge id {},  Amount: {}", charge.getId(), charge.getAmount());
                break;
            }
            case "checkout.session.completed": {
                logger.info(event.getType());
                try {
                    logger.info(payload);
                    webHookService.saveStripePayment(payload, event);
                    break;
                } catch (NumberFormatException ex) {
                    logger.error("OrderId not received");
                }
                break;
            }
            case "charge.refunded": {
                logger.info(event.getType());
                Charge charge = (Charge) stripeObject;
                String paymentIntent = charge.getPaymentIntent();
                webHookService.refund(paymentIntent);
                break;
            }
            default:
                logger.info("Unhandled event type: " + event.getType() + "payload = " + payload);
        }
        return ResponseEntity.ok().build();
    }
}
