package pl.dk.ecommerceplatform.promo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.ecommerceplatform.constant.PaginationConstant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PromoRepositoryTest {

    @Autowired
    private PromoRepository underTest;

    @Test
    void itShouldFindAllActivePromosCodesOrderByIdDesc() {
        // Given
        Pageable pageable = PageRequest.of(0, 10); // First page with 10 items

        // When
        Page<Promo> promoPage = underTest.findAllByActiveTrueOrderByIdDesc(pageable);

        // Then
        assertAll(
                () -> assertThat(promoPage).isNotNull()
        );
    }
}