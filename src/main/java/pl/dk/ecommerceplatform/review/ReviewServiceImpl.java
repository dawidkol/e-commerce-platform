package pl.dk.ecommerceplatform.review;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;

import java.util.List;

@Service
@AllArgsConstructor
class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDtoMapper reviewDtoMapper;

    public ReviewProductDto getAllProductsReviews(Long productId) {
        List<SingleReviewDto> list = reviewRepository.findAllByProduct_Id(productId).stream().map(reviewDtoMapper::map).toList();
        double average = this.getAverage(productId);
        return ReviewProductDto.builder()
                .averageProductReview(average)
                .singleReviewDto(list)
                .build();
    }

    private double getAverage(Long productId) {
        return reviewRepository.getAverageProductRating(productId);
    }

}
