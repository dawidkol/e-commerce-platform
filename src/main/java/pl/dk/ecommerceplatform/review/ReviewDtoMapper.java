package pl.dk.ecommerceplatform.review;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;

@Service
class ReviewDtoMapper {

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
}
