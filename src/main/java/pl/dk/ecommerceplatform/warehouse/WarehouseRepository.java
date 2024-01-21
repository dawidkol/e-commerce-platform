package pl.dk.ecommerceplatform.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByProduct_id(Long name);
    Optional<Item> findByProduct_name(String name);

}
