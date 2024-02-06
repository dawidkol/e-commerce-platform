package pl.dk.ecommerceplatform.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dk.ecommerceplatform.brand.Brand;
import pl.dk.ecommerceplatform.brand.BrandRepository;
import pl.dk.ecommerceplatform.category.Category;
import pl.dk.ecommerceplatform.category.CategoryRepository;
import pl.dk.ecommerceplatform.error.exceptions.brand.BrandNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryNotFoundException;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;
import pl.dk.ecommerceplatform.product.dtos.SaveProductDto;
import pl.dk.ecommerceplatform.warehouse.Item;
import pl.dk.ecommerceplatform.warehouse.WarehouseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDtoMapperTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    private ProductDtoMapper underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ProductDtoMapper(categoryRepository, brandRepository, warehouseRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldMapToProductByGivenSaveProductDto() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        BigDecimal price = new BigDecimal("99.99");
        SaveProductDto saveProductDto = SaveProductDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(1L)
                .brandId(1L)
                .build();

        Category categoryMock = mock(Category.class);
        Brand brandMock = mock(Brand.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryMock));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandMock));

        // When
        Product product = underTest.map(saveProductDto);

        // Then
        verify(categoryRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).findById(1L);
        assertAll(
                () -> assertThat(product.getId()).isNull(),
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getDescription()).isEqualTo(description),
                () -> assertThat(product.getPrice()).isEqualTo(price),
                () -> assertThat(product.getAdded()).isNotNull()
        );
    }

    @Test
    void itShouldThrowCategoryNotFoundExceptionWhenCategoryIsNotFound() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        BigDecimal price = new BigDecimal("99.99");
        SaveProductDto saveProductDto = SaveProductDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(1L)
                .brandId(1L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CategoryNotFoundException.class, () -> underTest.map(saveProductDto));

    }

    @Test
    void itShouldThrowBrandNotFoundExceptionWhenBrandIsNotFound() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        BigDecimal price = new BigDecimal("99.99");
        SaveProductDto saveProductDto = SaveProductDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .categoryId(1L)
                .brandId(1L)
                .build();

        Category categoryMock = mock(Category.class);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryMock));
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(BrandNotFoundException.class, () -> underTest.map(saveProductDto));

    }
    @Test
    void itShouldMapToProductDtoByGivenProduct() {
        // Given
        String name = "Test productName";
        String description = "Test description";
        Category category = Category.builder().id(1L).build();
        Brand brand = Brand.builder().id(1L).build();
        LocalDate added = LocalDate.now();
        BigDecimal price = new BigDecimal("99.99");
        Long productId = 1L;

        Product product = Product.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .brand(brand)
                .added(added)
                .build();

        Item productItem = mock(Item.class);
        when(warehouseRepository.findByProduct_id(1L)).thenReturn(Optional.of(productItem));

        // When
        ProductDto productDto = underTest.map(product);

        // Then
        verify(warehouseRepository, times(2)).findByProduct_id(1L);
        assertAll(
                () -> assertThat(productDto.id()).isEqualTo(productId),
                () -> assertThat(productDto.name()).isEqualTo(name),
                () -> assertThat(productDto.description()).isEqualTo(description),
                () -> assertThat(productDto.price()).isEqualTo(price),
                () -> assertThat(productDto.added()).isEqualTo(added)
        );
    }
}