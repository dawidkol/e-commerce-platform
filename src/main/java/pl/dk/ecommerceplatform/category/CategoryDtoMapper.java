package pl.dk.ecommerceplatform.category;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.category.dtos.CategoryDto;
import pl.dk.ecommerceplatform.category.dtos.SaveCategoryDto;

@Service
class CategoryDtoMapper {

    public Category map(SaveCategoryDto saveCategoryDto) {
        return Category.builder()
                .name(saveCategoryDto.name())
                .build();
    }

    public CategoryDto map(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
