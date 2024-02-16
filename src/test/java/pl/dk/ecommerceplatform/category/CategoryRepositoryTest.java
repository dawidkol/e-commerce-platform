package pl.dk.ecommerceplatform.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository underTest;

    @Test
    void itShouldReturnCategoryByGivenNameIgnoreCas() {
        // Given
        String categoryName = "electronics";

        // When
        Optional<Category> categoryOptional = underTest.findByNameIgnoreCase(categoryName);

        // Then
        assertThat(categoryOptional).isPresent().hasValueSatisfying(
                category -> {
                    assertThat(category.getId()).isNotNull();
                    assertThat(category.getName()).isEqualToIgnoringCase(categoryName);
                    assertThat(category.getProduct()).isNotEmpty();
                }
        );
    }
}