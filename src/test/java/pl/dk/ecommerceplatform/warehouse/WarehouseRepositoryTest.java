package pl.dk.ecommerceplatform.warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WarehouseRepositoryTest {

    @Autowired
    private WarehouseRepository underTest;

    @Test
    void itShouldRetrieveItemByGivenProductId() {
        // Given
        Long productId = 1L;

        // When
        Optional<Item> optionalItem = underTest.findByProduct_id(productId);

        // Then
        assertThat(optionalItem).isPresent().hasValueSatisfying(
                item -> {
                    assertThat(item.getProduct().getId()).isEqualTo(productId);
                    assertThat(item.getQuantity()).isEqualTo(23);
                    assertThat(item.isAvailable()).isEqualTo(true);
                }
        );
    }

    @Test
    void itShouldRetrieveItemByGivenProductName() {
        // Given
        String productName = "Samsung Galaxy Note 20";

        // When
        Optional<Item> optionalItem = underTest.findByProduct_name(productName);

        // Then
        assertThat(optionalItem).isPresent().hasValueSatisfying(
                item -> {
                    assertThat(item.getProduct().getId()).isEqualTo(1L);
                    assertThat(item.getProduct().getName()).isEqualTo(productName);
                    assertThat(item.getQuantity()).isEqualTo(23);
                    assertThat(item.isAvailable()).isEqualTo(true);
                }
        );
    }

    @Test
    void itShouldUpdateItemQuantityByGivenItemIdAndProductQuantity() {
        // Given
        Long itemId = 1L;
        Long subtractionProduct = 5L;

        // When
        underTest.updateQuantity(itemId, subtractionProduct);

        // Then
        Optional<Item> optionalItem = underTest.findById(itemId);
        assertThat(optionalItem).isPresent().hasValueSatisfying(
                item -> {
                    assertThat(item.getQuantity()).isEqualTo(18);
                }
        );
    }
}