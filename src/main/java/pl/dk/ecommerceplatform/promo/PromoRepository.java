package pl.dk.ecommerceplatform.promo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PromoRepository extends JpaRepository<Promo, Long> {
}
