package pl.dk.ecommerceplatform.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.ecommerceplatform.category.Category;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles(value = "test")
@ComponentScan("pl.dk.ecommerceplatform.config")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;
    private Logger logger;

    @BeforeEach
    void init() {
        logger = UtilsService.getLogger(this.getClass());
    }

    @Test
    void itShouldFindAllAppleProductsByGivenName() {
        // Given
        String productName = "Apple";

        // When
        List<Product> appleProducts = underTest.findAllByName(productName);
        logger.debug(appleProducts.toString());

        // Then
        assertThat(appleProducts.size()).isPositive();
    }

    @Test
    void itShouldFindAllAppleProductsByGivenNameAndCategory() {
        // Given
        String productName = "Apple";
        String categoryName = "Electronics";

        // When
        List<Product> appleProducts = underTest.findByNameAndCategory(productName, categoryName);
        logger.debug(appleProducts.toString());

        // Then
        assertThat(appleProducts.size()).isPositive();
    }

    @Test
    void itShouldFindAllProductsByGivenCategoryName() {
        // Given
        String categoryName = "Electronics";

        // When
        List<Product> productsByCategoryName = underTest.findAllByCategory_Name(categoryName, PageRequest.of(0, 99)).getContent();
        logger.debug(productsByCategoryName.toString());

        List<String> resulNameCategory = productsByCategoryName.stream().map(Product::getCategory).map(Category::getName).distinct().toList();

        // Then
        assertAll(
                () -> assertThat(resulNameCategory.size()).isEqualTo(1),
                () -> assertThat(resulNameCategory).contains(categoryName)
        );
    }

    @Test
    void itShouldFindAllProductByGivenName() {
        // Given
        String productName = "Samsung Galaxy Note 20";

        // When
        Optional<Product> productOptional = underTest.findByName(productName);
        logger.debug(productOptional.toString());

        // Then
        assertThat(productOptional).isPresent().hasValueSatisfying(
                product -> assertThat(product.getName()).isEqualTo(productName));
    }

    @Test
    void itShouldFindAllPromotionProducts() {
        // Given
        int page = 0;
        int size = 1;

        PageRequest pageRequest = PageRequest.of(page, size);

        // When
        List<Product> allPromotionProducts = underTest.findAllPromotionProducts(pageRequest);

        // Then
        assertThat(allPromotionProducts).hasAtLeastOneElementOfType(Product.class);
    }
}