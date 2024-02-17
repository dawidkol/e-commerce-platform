package pl.dk.ecommerceplatform.statistics;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.util.List;

@Repository
@AllArgsConstructor
class StatisticsDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CartProductsDto> rowMapper;
    public List<CartProductsDto> getTop3SoldProducts() {
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
        return jdbcTemplate.queryForStream(query, rowMapper, receivedStatus, deliveredStatus).toList();
    }

}
