package pl.dk.ecommerceplatform.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository underTest;

    @Test
    void itShouldRetrieveOrderByIdAndUserId() {
        // Given
        Long orderId = 1L;
        Long userId = 1L;

        // When
        Optional<Order> optionalOrder = underTest.findByIdAndUser_id(orderId, userId);

        // Then
        assertThat(optionalOrder).isPresent().hasValueSatisfying(
                order -> {
                    assertThat(order.getUser().getFirstName()).isEqualTo("Janusz");
                }
        );
    }

    @Test
    void itShouldRetrieveAllUsersOrdersByUserIdPageable() {
        // Given
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        List<Order> allOrdersByUserId = underTest.findAllByUser_id(userId, pageRequest);

        // Then
        assertThat(allOrdersByUserId).hasSize(1);
    }

    @Test
    void itShouldRetrieveUnpaidUserOrder() {
        // Given
        Long orderId = 1L;

        // When
        Optional<Order> orderOptional = underTest.findUnpaidOrder(orderId);

        // Then
        assertThat(orderOptional).isPresent();
    }

    @Test
    void itShouldRetrieveAllUsersOrdersByUserId() {
        // Given
        Long userId = 1L;

        // When
        List<Order> allOrdersByUserId = underTest.findByUser_id(userId);

        // Then
        assertThat(allOrdersByUserId).hasSize(1);
    }
}