package pl.dk.ecommerceplatform.review;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.cart.Cart;
import pl.dk.ecommerceplatform.error.exceptions.review.UserNotBoughtProductException;
import pl.dk.ecommerceplatform.order.Order;
import pl.dk.ecommerceplatform.order.OrderRepository;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.review.dtos.ReviewProductDto;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDtoMapper reviewDtoMapper;
    private final OrderRepository orderRepository;

    @Override
    public ReviewProductDto getAllProductsReviews(Long productId) {
        List<SingleReviewDto> list = reviewRepository.findAllByProduct_Id(productId).stream().map(reviewDtoMapper::map).toList();
        double average = this.getAverage(productId);
        return ReviewProductDto.builder()
                .averageProductReview(average)
                .singleReviewDto(list)
                .build();
    }

    @Override
    public SingleReviewDto createReview(Long userId, CreateReviewDto createReviewDto) {
        List<Long> productsIds = orderRepository.findByUser_id(userId)
                .stream()
                .map(Order::getCart)
                .map(Cart::getProducts)
                .flatMap(Collection::stream)
                .map(Product::getId)
                .toList();

        boolean isUserProduct = productsIds.stream()
                .anyMatch(product -> product.equals(createReviewDto.productId()));

        if (!isUserProduct) {
            throw new UserNotBoughtProductException();
        } else {
            Review reviewToSave = reviewDtoMapper.map(userId, createReviewDto);
            Review savedReview = reviewRepository.save(reviewToSave);
            return reviewDtoMapper.map(savedReview);
        }
    }

    private double getAverage(Long productId) {
        return reviewRepository.getAverageProductRating(productId);
    }

}
