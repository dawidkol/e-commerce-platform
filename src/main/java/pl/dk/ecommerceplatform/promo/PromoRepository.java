package pl.dk.ecommerceplatform.promo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface PromoRepository extends JpaRepository<Promo, Long> {
    Page<Promo> findAllByActiveTrueOrderByIdDesc(Pageable pageable);
}
