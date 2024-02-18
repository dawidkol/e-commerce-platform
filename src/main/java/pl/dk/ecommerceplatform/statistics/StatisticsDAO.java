package pl.dk.ecommerceplatform.statistics;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pl.dk.ecommerceplatform.order.OrderStatus;
import pl.dk.ecommerceplatform.statistics.dtos.AvgOrderDto;
import pl.dk.ecommerceplatform.statistics.dtos.CartProductsDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
class StatisticsDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CartProductsDto> cartProductsDtoRowMapper;

    public List<CartProductsDto> getTop3SoldProducts() {
        String query = """
                SELECT cart_products.product_id, product.name, COUNT(*) AS amount_of_sold_product FROM cart_products
                JOIN cart ON cart_products.cart_id = cart.id
                JOIN product ON cart_products.product_id = product.id
                JOIN orders ON cart_products.cart_id = orders.cart_id
                WHERE cart.used = true
                AND orders.status != 'NEW'
                AND orders.status IS NOT NULL
                GROUP BY cart_products.product_id, product.name
                ORDER BY amount_of_sold_product DESC
                LIMIT 3
                """;
        return jdbcTemplate.queryForStream(query, cartProductsDtoRowMapper).toList();
    }

    public AvgOrderDto getStatsFromLastMonth() {
        BigDecimal averageOrderValue = this.getAverageOrderValueFromLastMonth();
        Long amountOfOrders = this.getAmountOfOrdersFromLastMonth();
        Long totalSoldProducts = this.getTotalSoldProductsFromLastMonth();
        BigDecimal averageProductsPerOrder = this.getAverageProductsPerOrderFromLastMonth();

        return AvgOrderDto.builder()
                .avgOrderValue(averageOrderValue)
                .amountOfOrders(amountOfOrders)
                .totalSoldProducts(totalSoldProducts)
                .averageProductsPerOrder(averageProductsPerOrder)
                .build();
    }

    private BigDecimal getAverageProductsPerOrderFromLastMonth() {
        String query = """
                SELECT ROUND(AVG(num_of_products), 2) AS average_products_per_cart
                FROM (
                    SELECT COUNT(product_id) AS num_of_products
                    FROM cart_products
                    JOIN orders ON cart_products.cart_id = orders.cart_id
                    WHERE orders.status != 'NEW'
                    AND orders.status IS NOT NULL
                    AND EXTRACT(YEAR FROM created) = EXTRACT(YEAR FROM NOW())
                    AND EXTRACT(MONTH FROM created) = EXTRACT(MONTH FROM NOW() - INTERVAL '1' MONTH)
                    GROUP BY cart_products.cart_id
                ) AS products_in_cart;
                """;
        return jdbcTemplate.queryForObject(query, BigDecimal.class);
    }

    private BigDecimal getAverageOrderValueFromLastMonth() {
        String query = """
                SELECT ROUND(AVG(order_value),2) FROM orders
                WHERE orders.status != 'NEW'
                AND orders.status IS NOT NULL
                AND EXTRACT(YEAR FROM created) = EXTRACT(YEAR FROM NOW())
                AND EXTRACT(MONTH FROM created) = EXTRACT(MONTH FROM NOW() - INTERVAL '1' MONTH)
                """;
        return jdbcTemplate.queryForObject(query, BigDecimal.class);
    }

    private Long getAmountOfOrdersFromLastMonth() {
        String query = """
                SELECT COUNT(*) FROM orders
                WHERE orders.status != 'NEW'
                AND orders.status IS NOT NULL
                AND EXTRACT(YEAR FROM created) = EXTRACT(YEAR FROM NOW())
                AND EXTRACT(MONTH FROM created) = EXTRACT(MONTH FROM NOW() - INTERVAL '1' MONTH)
                """;
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    private Long getTotalSoldProductsFromLastMonth() {
        String query = """
                SELECT COUNT(*) AS amount_of_sold_product FROM cart_products
                JOIN cart ON cart_products.cart_id = cart.id
                JOIN orders ON cart_products.cart_id = orders.cart_id
                WHERE cart.used = true
                AND orders.status != 'NEW'
                AND orders.status IS NOT NULL
                AND EXTRACT(YEAR FROM created) = EXTRACT(YEAR FROM NOW())
                AND EXTRACT(MONTH FROM created) = EXTRACT(MONTH FROM NOW() - INTERVAL '1' MONTH)
                """;
        return jdbcTemplate.queryForObject(query, Long.class);
    }

}
