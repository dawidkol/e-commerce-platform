package pl.dk.ecommerceplatform.review.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SingleReviewDto(Long id, String productName, String username, int rating, String comment, LocalDateTime added) {
}
