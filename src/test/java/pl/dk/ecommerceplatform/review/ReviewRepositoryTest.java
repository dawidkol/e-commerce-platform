package pl.dk.ecommerceplatform.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository underTest;

    Long productId;
    Long userId;

    @BeforeEach
    void init() {
        productId = 1L;
        userId = 1L;

    }

    @Test
    void itShouldFindAllReviewsByGivenProductId() {
        // Given

        // When
        List<Review> allByProductId = underTest.findAllByProduct_Id(productId);

        // Then
        assertThat(allByProductId.size()).isEqualTo(2);
    }

    @Test
    void itShouldReturnAverageProductRatingByGivenProductId() {
        // Given

        // When
        double averageProductRating = underTest.getAverageProductRating(productId);

        // Then
        assertThat(averageProductRating).isEqualTo(4.5);
    }

    @Test
    void itShouldReturnReviewByGivenUserIdAndProductId() {
        // Given

        // When
        Optional<Review> reviewOptional = underTest.findByUser_idAndProduct_Id(userId, productId);

        // Then
        assertThat(reviewOptional).isPresent().hasValueSatisfying(review -> {
                    assertThat(review.getProduct().getId()).isEqualTo(productId);
                    assertThat(review.getUser().getId()).isEqualTo(userId);
                    assertThat(review.getRating()).isEqualTo(4);
                    assertThat(review.getComment()).isEqualTo("long comment to test product(id: 1) by user 1");
                }
        );
    }
}