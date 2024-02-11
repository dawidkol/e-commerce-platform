package pl.dk.ecommerceplatform.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.category.dtos.CategoryDto;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoMapperTest {

    private CategoryDtoMapper underTest;

    @BeforeEach
    void inti() {
        underTest = new CategoryDtoMapper();
    }

    @Test
    void itShouldMapToCategory() {
        // Given
        SaveCategoryDto categoryToMap = SaveCategoryDto.builder()
                .name("new test category")
                .build();

        // When
        Category category = underTest.map(categoryToMap);

        // Then
        assertAll(
                () -> assertThat(category.getName()).isEqualTo(categoryToMap.name()),
                () -> assertThat(category.getProduct()).isEqualTo(null)
        );
    }

    @Test
    void itShouldMapToCategoryDto() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("categoryName")
                .product(new ArrayList<>())
                .build();

        // When
        CategoryDto categoryDto = underTest.map(category);

        // Then
        assertAll(
                () -> assertThat(categoryDto.id()).isEqualTo(1L),
                () -> assertThat(categoryDto.name()).isEqualTo(category.getName())
        );
    }
}