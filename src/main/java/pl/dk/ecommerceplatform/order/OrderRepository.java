package pl.dk.ecommerceplatform.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUser_id(Long id, Long userId);

    List<Order> findAllByUser_id(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM orders WHERE status = 'NEW' AND id = :id LIMIT 1", nativeQuery = true)
    Optional<Order> findUnpaidOrder(Long id);

    List<Order> findByUser_id(Long userId);
}
