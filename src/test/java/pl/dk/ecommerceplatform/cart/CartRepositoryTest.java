package pl.dk.ecommerceplatform.cart;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "default")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartRepositoryTest {
    
    @Autowired
    private CartRepository underTest;

    @Test
    void itShouldFindCartByGivenUserId() {
        // Given
        Long userId = 1L;

        // When
        Optional<Cart> cartOptional = underTest.findByUser_id(userId);

        // Then
        assertThat(cartOptional).isPresent().hasValueSatisfying(
                cart -> {
                    assertThat(cart.getUser().getId()).isEqualTo(1L);
                    assertThat(cart.getUser().getEmail()).isEqualTo("janusz.kowalski@test.pl");
                    assertThat(cart.getProducts().get(0).getName()).isEqualTo("Samsung Galaxy Note 20");
                    assertThat(cart.getUsed()).isEqualTo(true);
                }
        );
    }

    @Test
    void itShouldNotFindAnyCart() {
        // Given
        Long userId = 1L;

        // When
        Optional<Cart> cartOptional = underTest.findCartByUserIdWhereUsedEqualsFalse(userId);

        // Then
        assertThat(cartOptional).isEmpty();
    }
}