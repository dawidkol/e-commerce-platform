package pl.dk.ecommerceplatform.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByProduct_id(Long name);
    Optional<Item> findByProduct_name(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE warehouse SET quantity = (quantity - :productQuantity) WHERE id = :itemId", nativeQuery = true)
    void updateQuantity(Long itemId, Long productQuantity);

}
