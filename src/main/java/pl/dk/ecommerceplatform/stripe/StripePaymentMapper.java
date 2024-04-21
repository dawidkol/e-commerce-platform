package pl.dk.ecommerceplatform.stripe;

import pl.dk.ecommerceplatform.stripe.dtos.StripePaymentDto;

class StripePaymentMapper {

    public static StripePaymentDto map(StripePayment stripePayment) {
        return StripePaymentDto.builder()
                .id(stripePayment.getPaymentIntent())
                .orderId(stripePayment.getOrder().getId())
                .customerEmail(stripePayment.getOrder().getUser().getEmail())
                .refund(stripePayment.getRefund())
                .build();
    }
}
