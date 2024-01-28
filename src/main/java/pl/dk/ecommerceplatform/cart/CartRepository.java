package pl.dk.ecommerceplatform.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser_id(Long id);

    void deleteAllByIdAndProducts_id(Long cartId, Long productId);

    @Query(value = "SELECT * FROM cart WHERE user_id = :id AND used = FALSE", nativeQuery = true)
    Optional<Cart> findCartByUserIdWhereUsedEqualsFalse(Long id);

}
