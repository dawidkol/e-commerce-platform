package pl.dk.ecommerceplatform.stripe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dk.ecommerceplatform.order.Order;

@Entity
@Table(name = "stripe_payment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StripePayment {

    @Id
    private String paymentIntent;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Boolean refund;
    private Boolean refunded;
}
