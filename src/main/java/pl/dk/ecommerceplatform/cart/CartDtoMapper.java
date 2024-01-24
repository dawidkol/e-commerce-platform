package pl.dk.ecommerceplatform.cart;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.cart.dtos.CartDto;
import pl.dk.ecommerceplatform.cart.dtos.CartProductDto;
import pl.dk.ecommerceplatform.product.Product;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
class CartDtoMapper {

    private final JdbcTemplate jdbcTemplate;

    public CartDto map(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .products(this.getCartProductsDto(cart))
                .currentCartValue(this.getCartValue(cart))
                .build();
    }
    private List<CartProductDto> getCartProductsDto(Cart cart) {
        List<Product> list = cart.getProducts().stream().distinct().toList();
        return list.stream()
                .map(this::createCartProductDto)
                .toList();
    }

    private CartProductDto createCartProductDto(Product product) {
        Long quantity = this.getQuantity(product.getId());
        String quantityString = String.valueOf(quantity);
        BigDecimal quantityBigDecimal = new BigDecimal(quantityString);
        BigDecimal productPrice = product.getPrice();
        BigDecimal totalPrice = productPrice.multiply(quantityBigDecimal);
        return CartProductDto.builder()
                .name(product.getName())
                .quantity(quantity)
                .productPrice(productPrice)
                .totalPrice(totalPrice)
                .build();
    }

    private Long getQuantity(Long productId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cart_products WHERE product_id = ?",
                Long.class,
                productId);
    }

    private BigDecimal getCartValue(Cart cart) {
        return cart.getProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
