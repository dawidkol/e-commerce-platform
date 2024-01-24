package pl.dk.ecommerceplatform.cart;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
class CartProductsDAO {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Modifying
    void insertProductToCart(Long cartId, Long productId) {
        jdbcTemplate.update("INSERT INTO cart_products(cart_id, product_id) VALUES (? , ?)", cartId, productId);
    }

    @Transactional
    @Modifying
    void deleteProductsInCart(Long cartId, Long productId) {
        jdbcTemplate.update("DELETE FROM cart_products WHERE cart_id = ? AND product_id = ?", cartId, productId);
    }

}
