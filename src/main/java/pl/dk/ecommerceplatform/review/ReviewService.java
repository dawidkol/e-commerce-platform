package pl.dk.ecommerceplatform.review;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;

@Service
public interface ReviewService {

    ReviewProductDto getAllProductsReviews(Long productId);

    SingleReviewDto createReview(Long userId, CreateReviewDto createReviewDto);

    SingleReviewDto getReview(Long id);
}
