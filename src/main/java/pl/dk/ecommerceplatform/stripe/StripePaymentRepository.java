package pl.dk.ecommerceplatform.stripe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StripePaymentRepository extends JpaRepository<StripePayment, Long> {

    Optional<StripePayment> findByOrder_id(Long orderId);

    Optional<StripePayment> findByPaymentIntent(String paymentIntent);

    List<StripePayment> findAllByRefundIsTrueAndRefundedIsFalse();
}
