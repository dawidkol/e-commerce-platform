package pl.dk.ecommerceplatform.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.ecommerceplatform.category.dtos.CategoryDto;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryExistsException;
import pl.dk.ecommerceplatform.error.exceptions.category.CategoryNotFoundException;
import pl.dk.ecommerceplatform.product.Product;
import pl.dk.ecommerceplatform.product.ProductDtoMapper;
import pl.dk.ecommerceplatform.product.ProductRepository;
import pl.dk.ecommerceplatform.product.dtos.ProductDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductDtoMapper productDtoMapper;
    @Mock
    private CategoryDtoMapper categoryDtoMapper;
    private CategoryService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CategoryService(productRepository, categoryRepository, productDtoMapper, categoryDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldGetProductsByCategory() {
        // Given
        String categoryName = "test category name";
        int pageNumber = 0;
        int pageSize = 10;
        Category category = Category.builder()
                .name(categoryName)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Example Product")
                .description("This is an example description.")
                .price(BigDecimal.valueOf(99.99))
                .category(category)
                .brand(null)
                .added(LocalDate.now())
                .build();

        List<Product> list = List.of(product);
        PageImpl<Product> value = new PageImpl<>(list);


        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));
        when(productRepository.findAllByCategory_Name(categoryName, PageRequest.of(pageNumber, pageSize))).thenReturn(value);
        when(productDtoMapper.map(product)).thenReturn(mock(ProductDto.class));

        // When
        underTest.getProductsByCategory(categoryName, pageNumber, pageSize);

        // Then
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
        verify(productRepository, times(1)).findAllByCategory_Name(categoryName, PageRequest.of(pageNumber, pageSize));
        verify(productDtoMapper, times(1)).map(product);
    }

    @Test
    void itShouldThrowCategoryNotFoundException() {
        // Given
        String categoryName = "test category name";
        int pageNumber = 0;
        int pageSize = 10;

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CategoryNotFoundException.class, () -> underTest.getProductsByCategory(categoryName, pageNumber, pageSize));
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
    }

    @Test
    void itShouldSaveCategory() {
        // Given
        SaveCategoryDto categoryDto = SaveCategoryDto.builder()
                .name("category")
                .build();

        Category category = Category.builder()
                .name(categoryDto.name())
                .product(new ArrayList<>())
                .build();

        Category categoryWithId = Category.builder()
                .id(1L)
                .name(categoryDto.name())
                .product(new ArrayList<>())
                .build();

        when(categoryRepository.findByNameIgnoreCase(categoryDto.name())).thenReturn(Optional.empty());
        when(categoryDtoMapper.map(categoryDto)).thenReturn(category);
        when((categoryRepository.save(category))).thenReturn(categoryWithId);
        when(categoryDtoMapper.map(categoryWithId)).thenReturn(mock(CategoryDto.class));

        // When
        underTest.saveCategory(categoryDto);

        // Then
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryDto.name());
        verify(categoryDtoMapper, times(1)).map(categoryDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryDtoMapper, times(1)).map(categoryWithId);
    }

    @Test
    void itShouldThrowCategoryExistsExceptionWhenCategoryAlreadyExists() {
        // Given
        SaveCategoryDto categoryDto = SaveCategoryDto.builder()
                .name("category")
                .build();

        Category category = Category.builder()
                .name(categoryDto.name())
                .product(new ArrayList<>())
                .build();

        when(categoryRepository.findByNameIgnoreCase(categoryDto.name())).thenReturn(Optional.of(category));

        // When
        // Then
        assertThrows(CategoryExistsException.class, () -> underTest.saveCategory(categoryDto));
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryDto.name());
    }
}