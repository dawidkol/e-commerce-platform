package pl.dk.ecommerceplatform.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import pl.dk.ecommerceplatform.currency.CurrencyCode;
import pl.dk.ecommerceplatform.error.exceptions.order.OrderNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.stripe.PaymentException;
import pl.dk.ecommerceplatform.error.exceptions.stripe.RefundException;
import pl.dk.ecommerceplatform.error.exceptions.stripe.UserNotMatchException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.order.OrderService;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.order.dtos.OrderValueDto;
import pl.dk.ecommerceplatform.stripe.dtos.CreatePaymentRequest;
import pl.dk.ecommerceplatform.stripe.dtos.PaymentResponse;
import pl.dk.ecommerceplatform.stripe.dtos.StripePaymentDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.math.BigDecimal;
import java.util.List;

import static com.stripe.param.checkout.SessionCreateParams.*;

@Service
//@RequiredArgsConstructor
class PaymentService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final RestClient restClient;
    private final StripePaymentRepository stripePaymentRepository;
    private static final Logger logger = UtilsService.getLogger(PaymentService.class);

    public PaymentService(OrderRepository orderRepository,
                          OrderService orderService,
                          RestClient.Builder restClient,
                          StripePaymentRepository stripePaymentRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.restClient = restClient
                .baseUrl("https://api.stripe.com")
                .defaultHeader("Authorization", "Bearer " + stripeApiKey)
                .build();
        this.stripePaymentRepository = stripePaymentRepository;
    }

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    @Transactional
    public PaymentResponse createPayment(Long orderId, String emailFromSecurityContext, String currencyCode) {
        OrderValueDto orderValueDto = orderService.calculateOrderValueWithOtherCurrency(orderId, currencyCode);
        CreatePaymentRequest paymentRequest = this.createAndValidatePaymentRequest(orderId);

        if (!emailFromSecurityContext.equals(paymentRequest.customerEmail())) {
            throw new UserNotMatchException("User %s not match with user from Token".formatted(paymentRequest.customerEmail()));
        }

        CurrencyCode currency = CurrencyCode.getCurrency(currencyCode);
        List<PaymentMethodType> list = currency.getPaymentMethods()
                .stream()
                .toList();

        SessionCreateParams params =
                builder()
                        .addAllPaymentMethodType(list)
                        .setCustomerEmail(paymentRequest.customerEmail())
                        .putMetadata("orderId", orderId.toString())
                        .addLineItem(
                                LineItem.builder()
                                        .setPriceData(
                                                LineItem.PriceData.builder()
                                                        .setCurrency(orderValueDto.currencyCode().name())
                                                        .setUnitAmountDecimal(orderValueDto.orderValue().multiply(BigDecimal.valueOf(100)))
                                                        .setProductData(
                                                                LineItem.PriceData.ProductData.builder()
                                                                        .setName("Order id: %s".formatted(orderId))
                                                                        .build())
                                                        .build())
                                        .setQuantity(1L)
                                        .build())
                        .setMode(Mode.PAYMENT)
                        .setSuccessUrl("https://example.com/success")
                        .setCancelUrl("https://example.com/cancel")
                        .build();

        try {
            Session session = Session.create(params);
            logger.info(session.getUrl());
            return getPaymentSucceed(session);
        } catch (StripeException ex) {
            throw new ServerException();
        }
    }

    private CreatePaymentRequest createAndValidatePaymentRequest(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        if (order.getStatus() != OrderStatus.NEW) {
            throw new PaymentException("StripePayment creation request for order = %s rejected. Current order status = %s"
                    .formatted(orderId, order.getStatus()));
        }
        CreatePaymentRequest build = CreatePaymentRequest.builder()
                .customerEmail(order.getUser().getEmail())
                .amount(order.getOrderValue().add(order.getShipping().getShippingCost()))
                .build();
        order.setStatus(OrderStatus.PAYMENT_IN_PROGRESS);
        return build;
    }

    private PaymentResponse getPaymentSucceed(Session session) {
        return PaymentResponse.builder()
                .message("StripePayment created")
                .url(session.getUrl())
                .build();
    }

    public void refund(Long orderId) {
        StripePayment stripePayment = stripePaymentRepository.findByOrder_id(orderId)
                .orElseThrow(() -> new RefundException("StripePayment for orderId %s not found".formatted(orderId)));
        RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(stripePayment.getPaymentIntent()).build();
        Refund refund;
        try {
            refund = Refund.create(params);
        } catch (StripeException ex) {
            throw new ServerException();
        }
        restClient.post()
                .uri("/v1/refunds")
                .header("Authorization", "Bearer " + stripeApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(refund);
    }

    public List<StripePaymentDto> getAllPaymentsToRefund() {
        return stripePaymentRepository.findAllByRefundIsTrueAndRefundedIsFalse()
                .stream()
                .map(StripePaymentMapper::map)
                .toList();
    }
}
