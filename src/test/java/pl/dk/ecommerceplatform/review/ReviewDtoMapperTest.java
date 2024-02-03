package pl.dk.ecommerceplatform.review;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReviewDtoMapperTest {

    private ReviewDtoMapper underTest;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ReviewDtoMapper(productRepository, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
        ;
    }

    @Test
    void itShouldMapToSingleReviewDto() {
        // Given
        String comment = "Comment 1";
        User userMock = mock(User.class);
        Product productMock = mock(Product.class);
        Long reviewId = 1L;
        int rating = 3;
        LocalDateTime added = LocalDateTime.now();

        Review review = Review.builder()
                .id(reviewId)
                .user(userMock)
                .comment(comment)
                .added(added)
                .rating(rating)
                .product(productMock)
                .build();

        // When
        SingleReviewDto singleReviewDto = underTest.map(review);

        // Then
        assertAll(
                () -> assertThat(singleReviewDto.id()).isEqualTo(reviewId),
                () -> assertThat(singleReviewDto.productName()).isEqualTo(productMock.getName()),
                () -> assertThat(singleReviewDto.username()).isEqualTo(userMock.getFirstName()),
                () -> assertThat(singleReviewDto.rating()).isEqualTo(rating),
                () -> assertThat(singleReviewDto.comment()).isEqualTo(comment),
                () -> assertThat(singleReviewDto.added()).isEqualTo(added)

        );
    }

    @Test
    void itShouldMapToReview() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        int rating = 4;
        String comment = "Comment1";

        CreateReviewDto createReviewDto = CreateReviewDto.builder()
                .productId(productId)
                .rating(rating)
                .comment(comment)
                .build();

        Product productMock = mock(Product.class);
        User userMock = mock(User.class);

        when(productRepository.findById(createReviewDto.productId())).thenReturn(Optional.of(productMock));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));

        // When
        Review review = underTest.map(userId, createReviewDto);

        // Then
        assertAll(
                () -> assertThat(review.getId()).isNull(),
                () -> assertThat(review.getUser()).isEqualTo(userMock),
                () -> assertThat(review.getRating()).isEqualTo(rating),
                () -> assertThat(review.getComment()).isEqualTo(comment),
                () -> assertThat(review.getProduct()).isEqualTo(productMock),
                () -> assertThat(review.getAdded()).isInstanceOf(LocalDateTime.class)
        );

    }
}