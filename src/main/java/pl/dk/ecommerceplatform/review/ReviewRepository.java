package pl.dk.ecommerceplatform.review;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ReviewRepository extends CrudRepository<Review, Long> {

    List<Review> findAllByProduct_Id(Long productId);

    @Query(value = "SELECT ROUND(AVG(rating), 2) FROM review WHERE product_id = :productId", nativeQuery = true)
    double getAverageProductRating(Long productId);

    Optional<Review> findByUser_idAndProduct_Id(Long userId, Long product_id);

}
