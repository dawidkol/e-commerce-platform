package pl.dk.ecommerceplatform.review.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.aspectj.weaver.ast.Not;

@Builder
public record CreateReviewDto(
        @NotNull @Positive Long productId,
        @NotNull @Min(1) @Max(5) int rating,
        @Size(max = 30000) String comment) {
}
