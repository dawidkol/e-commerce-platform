package pl.dk.ecommerceplatform.review.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewProductDto(double averageProductReview, List<SingleReviewDto> singleReviewDto) {
}
