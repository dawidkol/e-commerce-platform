package pl.dk.ecommerceplatform.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.stripe.UserNotMatchException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.stripe.dtos.CreatePaymentRequest;
import pl.dk.ecommerceplatform.stripe.dtos.PaymentResponse;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class PaymentService {

    private final OrderRepository orderRepository;
    private static final Logger logger = UtilsService.getLogger(PaymentService.class);

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentResponse createPayment(Long orderId, String emailFromSecurityContext) throws StripeException {
        CreatePaymentRequest paymentRequest = this.createPaymentRequest(orderId);

        if (!emailFromSecurityContext.equals(paymentRequest.customerEmail())) {
            throw new UserNotMatchException("User %s not match with user from Token" .formatted(paymentRequest.customerEmail()));
        }

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.P24)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setCustomerEmail(paymentRequest.customerEmail())
                        .putMetadata("orderId", orderId.toString())
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency(Currency.PLN.name())
                                                        .setUnitAmountDecimal(paymentRequest.amount().multiply(BigDecimal.valueOf(100)))
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Order id: %s" .formatted(orderId))
                                                                        .build())
                                                        .build())
                                        .setQuantity(1L)
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("https://example.com/success")
                        .setCancelUrl("https://example.com/cancel")
                        .build();

        Session session = Session.create(params);
        logger.info(session.getUrl());
        return getPaymentSucceed(session);

    }

    private CreatePaymentRequest createPaymentRequest(Long orderId) {
        Order order = orderRepository.findUnpaidOrder(orderId).orElseThrow(OrderNotFoundException::new);
        return CreatePaymentRequest.builder()
                .customerEmail(order.getUser().getEmail())
                .amount(order.getOrderValue().add(order.getShipping().getShippingCost()))
                .build();
    }

    private PaymentResponse getPaymentSucceed(Session session) {
        return PaymentResponse.builder()
                .message("Payment created")
                .url(session.getUrl())
                .build();
    }
}
