package pl.dk.ecommerceplatform.review;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.product.ProductNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
class ReviewDtoMapper {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SingleReviewDto map(Review review) {
        return SingleReviewDto.builder()
                .id(review.getId())
                .productName(review.getProduct().getName())
                .username(review.getUser().getFirstName())
                .rating(review.getRating())
                .comment(review.getComment())
                .added(review.getAdded())
                .build();
    }

    public Review map(Long userId, CreateReviewDto createReviewDto) {
        return Review.builder()
                .product(productRepository.findById(createReviewDto.productId()).orElseThrow(ProductNotFoundException::new))
                .user(userRepository.findById(userId).orElseThrow(UserNotFoundException::new))
                .rating(createReviewDto.rating())
                .comment(createReviewDto.comment())
                .added(LocalDateTime.now())
                .build();

    }

}
