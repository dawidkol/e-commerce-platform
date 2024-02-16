package pl.dk.ecommerceplatform.brand;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan("pl.dk.ecommerceplatform.config")
class BrandRepositoryTest {

    @Autowired
    private BrandRepository underTest;

    @Test
    void itShouldFindByNameIgnoreCase() {
        // Given
        String name = "Samsung";

        // When
        Optional<Brand> brandOptional = underTest.findByNameIgnoreCase(name);

        // Then
        assertThat(brandOptional).isPresent().hasValueSatisfying(
                brand -> assertThat(brand.getName()).isEqualTo(name)
        );
    }
}