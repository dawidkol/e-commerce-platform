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
public class CartDtoMapper {

    private final CartProductsDAO cartProductsDAO;

    public CartDto map(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .products(this.getCartProductsDto(cart))
                .currentCartValue(this.getCartValue(cart))
                .build();
    }

    public List<CartProductDto> getCartProductsDto(Cart cart) {
        List<Product> list = cart.getProducts().stream().distinct().toList();
        return list.stream()
                .map(product -> this.createCartProductDto(product, cart.getId()))
                .toList();
    }

    private CartProductDto createCartProductDto(Product product, Long cartId) {
        Long quantity = cartProductsDAO.getQuantity(product.getId(), cartId);
        String quantityString = String.valueOf(quantity);
        BigDecimal quantityBigDecimal = new BigDecimal(quantityString);
        BigDecimal productPrice;
        if (product.getPromotionPrice() != null) {
            productPrice = product.getPromotionPrice();
        } else {
            productPrice = product.getPrice();
        }
        BigDecimal totalPrice = productPrice.multiply(quantityBigDecimal);
        return CartProductDto.builder()
                .name(product.getName())
                .quantity(quantity)
                .productPrice(productPrice)
                .totalPrice(totalPrice)
                .build();
    }

    public BigDecimal getCartValue(Cart cart) {
        return cart.getProducts()
                .stream()
                .map(product -> {
                    if (product.getPromotionPrice() != null) {
                        return product.getPromotionPrice();
                    } else {
                        return product.getPrice();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
