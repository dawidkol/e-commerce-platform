package pl.dk.ecommerceplatform.review;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;

@Service
public interface ReviewService {

    ReviewProductDto getAllProductsReviews(Long productId);
}
