package pl.dk.ecommerceplatform.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUser_id(Long id);
    Optional<Order> findByCart_id(Long id);
}
