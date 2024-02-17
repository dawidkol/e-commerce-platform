package pl.dk.ecommerceplatform.statistics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class StatisticsDAOTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private RowMapper<CartProductsDto> rowMapper;

    private StatisticsDAO underTest;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StatisticsDAO(jdbcTemplate, rowMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldReturnTop3() {
        // Given
        CartProductsDto cartProductsDto1 = CartProductsDto.builder().build();
        CartProductsDto cartProductsDto2 = CartProductsDto.builder().build();
        CartProductsDto cartProductsDto3 = CartProductsDto.builder().build();
        String query = """
                SELECT cart_products.product_id, product.name, COUNT(*) AS "amount_of_sold_product" FROM cart_products
                JOIN cart ON cart_products.cart_id = cart.id
                JOIN product ON cart_products.product_id = product.id
                JOIN orders ON cart_products.cart_id = orders.cart_id
                WHERE cart.used = true
                AND orders.status = ?
                OR orders.status = ?
                GROUP BY cart_products.product_id, product.name
                ORDER BY amount_of_sold_product DESC
                LIMIT 3
                """;
        String receivedStatus = OrderStatus.RECEIVED.name();
        String deliveredStatus = OrderStatus.DELIVERED.name();
        when(jdbcTemplate.queryForStream(query, rowMapper, receivedStatus, deliveredStatus)).thenReturn(Stream.of(cartProductsDto1, cartProductsDto2, cartProductsDto3));

        // When
        underTest.getTop3SoldProducts();

        // Then
        verify(jdbcTemplate, times(1)).queryForStream(query, rowMapper, receivedStatus, deliveredStatus);
    }
}