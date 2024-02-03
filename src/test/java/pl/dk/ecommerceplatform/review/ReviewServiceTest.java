package pl.dk.ecommerceplatform.review;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.error.exceptions.review.ReviewAlreadyExistsException;
import pl.dk.ecommerceplatform.error.exceptions.review.UserNotBoughtProductException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewDtoMapper reviewDtoMapper;
    @Mock
    private OrderRepository orderRepository;
    private ReviewService reviewService;
    Product product;
    User user;
    Review review;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        reviewService = new ReviewServiceImpl(reviewRepository, reviewDtoMapper, orderRepository);
        product = mock(Product.class);
        user = mock(User.class);

        Long productId = 1L;
        String comment = "comment1";
        int rating = 4;
        LocalDateTime added = LocalDateTime.now();

        review = Review.builder()
                .id(productId)
                .product(product)
                .comment(comment)
                .user(user)
                .rating(rating)
                .added(added).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldReturnAllProductReview() {
        // Given
        List<Review> reviewsList = List.of(review);
        Long reviewId = review.getId();
        Long productId = review.getProduct().getId();

        when(reviewRepository.findAllByProduct_Id(reviewId)).thenReturn(reviewsList);
        when(reviewRepository.getAverageProductRating(productId)).thenReturn(4.0);

        // When
        ReviewProductDto allProductsReviews = reviewService.getAllProductsReviews(productId);

        // Then
        assertAll(
                () -> assertThat(allProductsReviews).isInstanceOf(ReviewProductDto.class),
                () -> assertThat(allProductsReviews.averageProductReview()).isEqualTo(4),
                () -> assertThat(allProductsReviews.singleReviewDto()).isNotNull()
        );
    }

    @Test
    void itShouldThrowReviewAlreadyExistsExceptionWhenUserAlreadyCreatedReview() {
        // Given
        CreateReviewDto reviewDto = CreateReviewDto.builder()
                .productId(1L)
                .comment("comment1")
                .rating(4)
                .build();

        // When
        when(reviewRepository.findByUser_idAndProduct_Id(1L, 1L)).thenReturn(Optional.of(review));

        // Then
        assertThrows(ReviewAlreadyExistsException.class, () -> reviewService.createReview(1L, reviewDto));
    }

    @Test
    void itShouldCreateReview() {
        // Given
        when(reviewRepository.findByUser_idAndProduct_Id(1L, 1L))
                .thenReturn(Optional.empty());

        Order order = Order.builder()
                .user(User.builder().id(1L).build())
                .cart(Cart.builder().id(1L).products(List.of(Product.builder().id(1L).build())).build())
                .build();

        CreateReviewDto reviewDto = CreateReviewDto.builder()
                .productId(1L)
                .comment("comment1")
                .rating(4)
                .build();

        when(orderRepository.findByUser_id(1L)).thenReturn(List.of(order));

        // When
        reviewService.createReview(1L, reviewDto);
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);

        // Then
        verify(reviewRepository, times(1)).save(reviewArgumentCaptor.capture());
        verify(reviewDtoMapper, times(1)).map(reviewArgumentCaptor.capture());
    }

    @Test
    void itShouldThrowUserNotBoughtException() {
        // Given
        when(reviewRepository.findByUser_idAndProduct_Id(1L, 1L))
                .thenReturn(Optional.empty());

        CreateReviewDto reviewDto = CreateReviewDto.builder()
                .productId(1L)
                .comment("comment1")
                .rating(4)
                .build();

        when(orderRepository.findByUser_id(1L)).thenReturn(List.of());

        // When
        // Then
        assertThrows(UserNotBoughtProductException.class, () -> reviewService.createReview(1L, reviewDto));
    }

    @Test
    void itShouldReturnReview() {
        // Given
        Long reviewId = 1L;
        Review review = Review.builder().id(1L).build();
        SingleReviewDto singleReviewDto = SingleReviewDto.builder().id(1L).build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewDtoMapper.map(review)).thenReturn(singleReviewDto);

        // When
        reviewService.getReview(reviewId);

        // Then
        verify(reviewDtoMapper, times(1)).map(review);
    }

}