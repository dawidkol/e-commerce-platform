package pl.dk.ecommerceplatform.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles(value = "default")
@ComponentScan(value = "pl.dk.ecommerceplatform.config")
@Import(value = CartProductsDAO.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartProductsDAOTest {

    private JdbcTemplate jdbcTemplate;
    private CartProductsDAO cartProductsDAO;

    @BeforeEach
    void init() {
        jdbcTemplate = mock(JdbcTemplate.class);
        cartProductsDAO = new CartProductsDAO(jdbcTemplate);
    }

    @Test
    void itShouldInsertProductToCart() {
        // Given
        Long cartId = 1L;
        Long productId = 2L;

        // When
        cartProductsDAO.insertProductToCart(cartId, productId);

        // Then
        verify(jdbcTemplate, times(1)).update(anyString(), eq(cartId), eq(productId));
    }

    @Test
    void itShouldDeleteProductFromCart() {
        // Given
        Long cartId = 1L;
        Long productId = 2L;

        // When
        cartProductsDAO.deleteProductsInCart(cartId, productId);

        // Then
        verify(jdbcTemplate, times(1)).update(anyString(), eq(cartId), eq(productId));
    }

    @Test
    void itShouldReturnProductQuantityInCart() {
        // Given
        Long productId = 1L;
        Long cartId = 2L;
        Long expectedQuantity = 10L;

        // Mocking the behavior of JdbcTemplate
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq(productId), eq(cartId))).thenReturn(expectedQuantity);

        // When
        Long quantity = cartProductsDAO.getQuantity(productId, cartId);

        // Then
        assertThat(quantity).isEqualTo(expectedQuantity);
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Long.class), eq(productId), eq(cartId));
    }
}